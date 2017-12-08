package logic.module.room;

import java.util.ArrayList;
import java.util.Iterator;

import core.detail.impl.log.Log;
import core.detail.impl.socket.SendMsgBuffer;
import core.remote.PI;
import core.remote.PS;
import core.remote.PST;
import core.remote.PU;
import core.remote.PVI;
import core.remote.RFC;
import logic.MyUser;
import logic.PackBuffer;
import logic.Reg;
import logic.eCardOper;
import logic.eCardState;
import logic.eErrorCode;
import logic.eErrorType;
import logic.eGameState;
import logic.eUserState;
import logic.eWanFaType_sx;
import logic.eYouState;
import logic.config.MahJongConfig;
import logic.config.handler.MahJongHandler;
import logic.loader.UserLoader;
import logic.module.log.eLogicDebugLogType;
import logic.module.room.RoomUser.piaoTag;
import logic.userdata.IDeskBalance;
import logic.userdata.YZDeskBalance;
import manager.CardManager;
import manager.ConfigManager;
import manager.LoaderManager;
import manager.RoomManager;

public class RoomImpl implements RoomInterface {

	@Override
	@RFC(ID = 1)
	public void EnterRoom(@PU(Index = 1) MyUser p_user, @PI int roomId, @PS String ip) {
		// TODO Auto-generated method stub
		Room room = RoomManager.getInstance().getRoom(roomId);
		if (null != room) {
			System.err.printf("%d:房间内的人数：%d\n", roomId, room.getUserCount());
			LogRecord(null,String.format("%d:房间内的人数：%d\n", roomId, room.getUserCount()));
			if (room.isFull()) {
				Room rm = RoomManager.getInstance().getRoom(p_user.GetRoleGID());
				if (null != rm) {
					// 说明这个人在房间内;
					SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_RETURN_ROOM);
					p.Add(roomId);
					p.Send(p_user); // 告诉自己都有谁在房间里;
				} else {
					SendMsgBuffer sender = PackBuffer.GetInstance().Clear().AddID(Reg.ERROR,
							eErrorType.Logic_Error.ID());
					sender.Add(eErrorCode.Error_3.ID());
					sender.Send(p_user);
				}
			} else {
				p_user.getCenterData().resetScore();
				RoomManager.getInstance().joinRoom(p_user, roomId);
				RoomUser ru = room.addUser(p_user, ip); // 不给这个;
				room.broadcast(RoomInterface.MID_BROADCAST_ENTER, ru); // 广播给其他人进入房间了;
				SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_ENTER);
				room.packData(p);
				room.packUserData(p);
				// 加入房间的规则;
				System.err.printf("%s - 进入房间：%d\n", p_user.getCenterData().getOpenId(), roomId);
				p.Send(p_user); // 告诉自己都有谁在房间里;
			}

		} else {
			SendMsgBuffer sender = PackBuffer.GetInstance().Clear().AddID(Reg.ERROR, eErrorType.Logic_Error.ID());
			sender.Add(eErrorCode.Error_2.ID());
			sender.Send(p_user);
		}
	}

	@Override
	@RFC(ID = 5)
	public void OperCard(@PU(Index = 1) MyUser p_user, @PI int type, @PVI ArrayList<Integer> list) {
		// TODO Auto-generated method stub
		if (type == eCardOper.MID_GET_CARD) { // 摸牌;
			Room r = RoomManager.getInstance().getRoom(p_user.getRoomId());
			RoomUser ru = r.getRoomUser(p_user);
			if (ru.getState() == eUserState.MOPAI) {
				if (!r.canMoCard(ru)) {
					System.out.printf("禁止摸牌，下一家有碰杠胡的牌 \n");
					return;
				}
				r.startPullCard(ru);
			} else {
				SendMsgBuffer sender = PackBuffer.GetInstance().Clear().AddID(Reg.ERROR, eErrorType.Logic_Error.ID());
				sender.Add(eErrorCode.Error_4.ID());
				sender.Send(p_user);
			}

		} else if (type == eCardOper.MID_SENDCARD) { // 出牌;
			Room r = RoomManager.getInstance().getRoom(p_user.getRoomId());
			RoomUser ru = r.getRoomUser(p_user);
			if (ru.getState() == eUserState.CHUPAI) {
				int cid = list.get(0);
				MahJongHandler handler = (MahJongHandler) ConfigManager.getInstance()
						.getHandler(ConfigManager.MahJongConfig);
				MahJongConfig conf1 = handler.getConfigById(cid);
				CardInfo ci = ru.getCard(cid);
				if(ci != null){
					r.startPushCard(ru, ci);
				}
				
			} else {
				SendMsgBuffer sender = PackBuffer.GetInstance().Clear().AddID(Reg.ERROR, eErrorType.Logic_Error.ID());
				sender.Add(eErrorCode.Error_5.ID());
				sender.Send(p_user);
			}

		} else if (type == eCardOper.MID_SENDEAT) { // 吃牌;
			if (!CardManager.canchi) {
				return;
			}
			Room r = RoomManager.getInstance().getRoom(p_user.getRoomId());
			boolean ret = r.operCard(p_user, list, type);
			if (ret) {
				r.broadcast(p_user, RoomInterface.MID_OPER_CARD, eCardOper.MID_BROADCAST_EADCARD, list);
			}
			// 需要检查是否能吃;

		} else if (type == eCardOper.MID_SENDGANG) { // 杠牌;
			Room r = RoomManager.getInstance().getRoom(p_user.getRoomId());
			for (int i = 0; i < list.size(); i++) {
				System.out.println("");
			}
			
			ArrayList<RoomUser> userInfo = r.getUserInfo();
			Iterator<RoomUser> it = userInfo.iterator();
			RoomUser ru = r.getRoomUser(p_user);
			
			int size = ru.getAnGang().size();
			boolean ret = r.operCard(p_user, list, type);
			int size2 = ru.getAnGang().size();
			if (ret) {
				if(size==size2){
					r.broadcast(p_user, RoomInterface.MID_OPER_CARD, eCardOper.MID_BROADCAST_GANGCARD, list);}
				else{
					r.broadcast(p_user, RoomInterface.MID_OPER_CARD, eCardOper.MID_BROADCAST_ANGANG, list);
				}
			}

		} else if (type == eCardOper.MID_ANGANG) { // 暗杠牌;
			Room r = RoomManager.getInstance().getRoom(p_user.getRoomId());
			boolean ret = r.operCard(p_user, list, type);
			if (ret) {
				r.broadcast(p_user, RoomInterface.MID_OPER_CARD, eCardOper.MID_BROADCAST_ANGANG, list);
			}

		} else if (type == eCardOper.MID_SENDPENG) { // 碰牌;
			Room r = RoomManager.getInstance().getRoom(p_user.getRoomId());
			boolean ret = r.operCard(p_user, list, type);
			if (ret) {
				r.broadcast(p_user, RoomInterface.MID_OPER_CARD, eCardOper.MID_BROADCAST_PENGCARD, list);
			}

		} else if (type == eCardOper.MID_GUO_CARD) { // 过牌;
			Room r = RoomManager.getInstance().getRoom(p_user.getRoomId());
			r.GuoCard(p_user);
		} else if (type == eCardOper.MID_TINGPAI) { // 听牌;
			Room r = RoomManager.getInstance().getRoom(p_user.getRoomId());
			r.TingCard(p_user);
			r.GuoCard(p_user);
		} else if (type == eCardOper.MID_SENDHU) { // 胡牌;
			Room r = RoomManager.getInstance().getRoom(p_user.getRoomId());
			int tid = list.get(0);

			eCardState ret = r.huCard(p_user);
			String fstr = String.format("\n接收到胡牌的消息:%d", tid);
			LogRecord(p_user, fstr);
			if (null != ret) {
				RoomUser roomUser = r.getRoomUser(p_user);
				if(roomUser.getYou() == eYouState.NOTICE_YOU && (tid == 0 || tid == -1)){
					//如果是通知过你u牌，但是你选择了胡牌，那就随机的再发一张牌给你;
					CardInfo ci = r.pullCard(p_user);
					roomUser.setCurrentCard(ci);
				}
				
				roomUser.setYou(eYouState.NONE);
				fstr = String.format("\n胡牌成立，开始进行结算，悠牌的状态被设置为None了");
				
				LogRecord(p_user, fstr);
				if (ret == eCardState.HU) {
					r.processResult(p_user, tid, false);// 这里计算分数了;
					//如果点炮  将最后一张牌 加到手牌中
					roomUser.addCard(tid);
					r.broadcast(p_user, RoomInterface.MID_OPER_CARD, eCardOper.MID_BROADCAST_HUCARD, list);
				} else {
					r.processResult(p_user, -1, false);// 这里计算分数了;
					//如果点炮  将最后一张牌 加到手牌中
					roomUser.addCard(tid);
					list.remove(0);
					list.add(-1);
					r.broadcast(p_user, RoomInterface.MID_OPER_CARD, eCardOper.MID_BROADCAST_HUCARD, list);
				}
			}
		}
	}

	@Override
	@RFC(ID = 4)
	public void InitCard(@PU(Index = 1) MyUser p_user, @PI int roomId, @PS String ip) {
		// TODO Auto-generated method stub
		Room room = RoomManager.getInstance().getRoom(roomId);
		RoomUser ru = new RoomUser(p_user, ip);
		room.initCard();
		room.broadcast(RoomInterface.MID_LAST_CARD, room.getCardCount());
	}

	@Override
	@RFC(ID = 6)
	public void GameReady(@PU(Index = 1) MyUser p_user, @PI int roomId) {
		// TODO Auto-generated method stub
		Room room = RoomManager.getInstance().getRoom(roomId);
        LogRecord(null,"收到游戏准备");
        if (!room.gameOver) {
			room.changUserState(p_user, eUserState.READY, true);
			room.broadcast(RoomInterface.MID_READY, p_user.GetRoleGID());
			System.err.printf("游戏准备：%s\n", p_user.GetNick());
            LogRecord(null,"游戏准备");
		} else {
			room.broadcast(-100, 0);
		LogRecord(null,"收到游戏准备消息————————准备失败");
		}
	}

	@Override
	@RFC(ID = 8)
	public void LeaveRoom(@PU(Index = 1) MyUser p_user, @PI int roomId) {
		// TODO Auto-generated method stub
		Room room = RoomManager.getInstance().getRoom(roomId);
		if(room!=null) {
            room.leaveRoom(p_user);
            SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_LEAVEROOM);
            p.Add(1);
            p.Send(p_user); // 告诉自己都有谁在房间里;
            RoomUser ru = room.getRoomUser(p_user);
            if (null != ru && ru.getZhuang() == 1) {
                if (room.getState() != eGameState.GAME_WAIT) {
                    if (room.getState().ID() <= eGameState.GAME_PLAYING.ID()) { // 如果游戏已经开始了，就需要其他的玩家的同意才能解散;
                        // 直接解散房间;
                        room.broadcast(RoomInterface.MID_BROADCAST_DISMISS, 1);
                        RoomManager.getInstance().removeRoom(roomId);
                        return;
                    }
                }
            }
            if (room.getUserCount() == 0) {
                RoomManager.getInstance().removeRoom(roomId);
            }

        }
		
		
	}

	@Override
	@RFC(ID = 12)
	public void Reconnect(@PU(Index = 1) MyUser p_user, @PI int roomId) {
		// TODO Auto-generated method stub
		Room r = RoomManager.getInstance().getRoom(roomId);
		if (null != r) {
			RoomUser ru = r.getRoomUser(p_user);
			if (null != ru) {
				ru.updateUser(p_user);
				ru.setRoomId(roomId);
				ru.tagLeaveRoom(false);
				eGameState state = r.getState();
				LogRecord(p_user, "现在游戏状态是"+state);
		
				if(state == eGameState.GAME_OVER){
					r.checkGameOver();
					if (CardManager.isshanxi&&r.getRoomRule().hasWanFaSX(eWanFaType_sx.SHIGUO)) {
						if(r.checkover(true)){
							r.backGuo();
						}
					}
					SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_ENTER);
					r.packData(p);
				/*	if(CardManager.isjinzhou){
					r.addHui(p);}*/
					r.packUserData(p);
						if(CardManager.isjinzhou){
					r.addHui(p);}
					p.Send(p_user); // 告诉自己都有谁在房间里;
					//胡牌的消息;
					r.TellUserHu(ru.getUser(), r.getWinner());
					r.broadcast(RoomInterface.MID_BROADCAST_SATTUS, ru.getRoleId(), 2);
				}else if(state == eGameState.GAME_START){
					r.broadcast(RoomInterface.MID_BROADCAST_ENTER, ru); // 广播给其他人进入房间了;
					SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_ENTER);
					LogRecord(p_user, "广播给其他人有人重新进入房间");
					r.packData(p);
					r.packUserData(p);
					p.Send(p_user); // 告诉自己都有谁在房间里;
				} else{
					SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_RECONNECT);
					r.packRoomInfo(p, p_user.GetRoleGID());
					
					p.Send(p_user);
					// 恢复当前用户的状态；
					r.resumeCardState(ru);
				}
				

			}
		} else {
			SendMsgBuffer sender = PackBuffer.GetInstance().Clear().AddID(Reg.ERROR, eErrorType.Logic_Error.ID());
			sender.Add(eErrorCode.Error_6.ID());
			sender.Send(p_user);
		}
	}

	@Override
	@RFC(ID = 13)
	public void ApplyDismiss(@PU(Index = 1) MyUser p_user, @PI int roomId) {
		// TODO Auto-generated method stub
		String fstr2 = String.format("有人申请解散房间:%d - %d", p_user.GetRoleGID(), roomId);
		System.err.printf(fstr2);

		Room r = RoomManager.getInstance().getRoom(roomId);
		if (null != r && r.getState() != eGameState.GAME_WAIT) {
			if (r.getState().ID() >= eGameState.GAME_PLAYING.ID()) { // 如果游戏已经开始了，就需要其他的玩家的同意才能解散;
				r.broadcast(RoomInterface.MID_BROADCAST_APPLYDISMISS, p_user.GetRoleGID());
				r.changeRoomState(eGameState.GAME_WAIT);
				r.dismissRoom(p_user, 1);
			} else {
				// 直接解散房间;
				r.broadcast(RoomInterface.MID_BROADCAST_DISMISS, 1);
				RoomManager.getInstance().removeRoom(roomId);
			}
		}
	}

	@Override
	@RFC(ID = 14)
	public void AgreeDismiss(@PU(Index = 1) MyUser p_user, @PI int roomId, @PI int result) {
		// TODO Auto-generated method stub
		Room r = RoomManager.getInstance().getRoom(roomId);
		if (null != r) {
			r.dismissRoom(p_user, result);
		}

	}

	@Override
	@RFC(ID = 15)
	public void DismissRoom(@PU(Index = 1) MyUser p_user, @PI int roomId) {
		// TODO Auto-generated method stub

	}

	@Override
	@RFC(ID = 0)
	public void CreateRoom(@PU(Index = 1) MyUser p_user, @PST int round, @PST int fan, @PST int wanfa,
			@PVI ArrayList<Integer> list, @PI int jifen, @PVI ArrayList<Integer> list2) {
		// TODO Auto-generated method stub
		Room room = RoomManager.getInstance().createRoom(p_user);
		RoomRule rr = new RoomRule(round, fan, wanfa, list, jifen, list2);
		room.setRoomRule(rr);
		if (null != room) {
			p_user.getCenterData().resetScore();
			int count = p_user.getCenterData().getRoomCard();
			int need = rr.getCostKa();
			if (count < need&&!CardManager.isshanxi) {
				SendMsgBuffer sender = PackBuffer.GetInstance().Clear().AddID(Reg.ERROR, eErrorType.Logic_Error.ID());
				sender.Add(eErrorCode.Error_1.ID());
				sender.Send(p_user);
			} else {
				// p_user.getCenterData().changeRoomCard(-need);
				SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, 0);
				room.packData(p);
				
				p.Send(p_user);
			}
		}
	}

	@Override
	@RFC(ID = 17)
	public void TestCard(@PU(Index = 1) MyUser p_user, @PVI ArrayList<Integer> list, @PVI ArrayList<Integer> list2) {
		// TODO Auto-generated method stub
		// CardManager.getInstance().testCard(list);
		SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_TEST_CARD);
		RoomUser ru = new RoomUser(p_user, "");
		Iterator<Integer> it = list.iterator();
		ArrayList<CardInfo> mylist = new ArrayList<CardInfo>();
		while (it.hasNext()) {
			int tid = it.next();
			MahJongHandler handler = (MahJongHandler) ConfigManager.getInstance()
					.getHandler(ConfigManager.MahJongConfig);
			MahJongConfig conf = handler.getConfigById(tid);
			CardInfo ci = new CardInfo(ru);
			ci.init(conf);
			mylist.add(ci);
			Iterator<Integer> it2 = list2.iterator();
			while (it2.hasNext()) {
				if (it2.next() == tid) {
					ci.setHui(true);
				}
			}
		}
		ru.initCard(mylist);
		boolean ret = ru.getHuCard(null, true, false, false);
		p.Add(ret ? 1 : 0);
		p.Send(p_user);
	}

	@Override
	@RFC(ID = 19)
	public void SendChat(@PU(Index = 1) MyUser p_user, @PI int roomId, @PI int type, @PS String id) {
		// TODO Auto-generated method stub
		Room r = RoomManager.getInstance().getRoom(roomId);
		r.BroadcastChat(RoomInterface.MID_BROADCAST_CHAT, p_user.GetRoleGID(), type, id);
	}

	@Override
	@RFC(ID = 23)
	public void KouCard(@PU(Index = 1) MyUser p_user, @PI int roomId, @PVI ArrayList<Integer> list) {
		// TODO Auto-generated method stub
		Room r = RoomManager.getInstance().getRoom(roomId);
		RoomUser ru = r.getRoomUser(p_user);
		if (ru != null) {

			ru.kouCard(list);
	
		}
		
	
		r.broadcast(RoomInterface.MID_BROAD_KOU_CARD, p_user.GetRoleGID(), list);
		int ret = r.changeKouRound(p_user, list.size() > 1);
		if (ret == 3) {
			r.changeRoomState(eGameState.GAME_PLAYING);
			r.broadcast(RoomInterface.MID_KOU_OVER, p_user.GetRoleGID());
			LogRecord(p_user, "扣牌结束");
		} else if (ret == 2) {
			r.broadcast(RoomInterface.MID_KOU_ROUND_OVER, 1);
		}
	}

	@Override
	@RFC(ID = 31)
	public void GetList(@PU(Index = 1) MyUser p_user, @PI int page) {
		// TODO Auto-generated method stub
		SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_GET_LIST);
		UserLoader loader = (UserLoader) LoaderManager.getInstance().getLoader(LoaderManager.Users);
		loader.packRanking(p, p_user, page);
		p.Send(p_user);
	}

	@Override
	@RFC(ID = 29)
	public void SendPiao(@PU(Index = 1) MyUser p_user, @PI int roomId, @PI int type) {
		// TODO Auto-generated method stub
		Room r = RoomManager.getInstance().getRoom(roomId);
		RoomUser ru = r.getRoomUser(p_user);
		ru.setPiao(piaoTag.values()[type - 1]);
		r.broadcast(RoomInterface.MID_BROADCAST_PIAO,ru, type);
	/*	r.broadcasts(RoomInterface.MID_BROADCAST_PIAO,ru, type);*/
		if (r.checkPiaoState()) {
			r.initCard();
		}
/*		System.out.println("======================="+type);
		r.broadcasts(RoomInterface.MID_BROADCAST_PIAO,ru,type);*/

	}

	/**
	 * 收到了悠的消息，直接胡牌;
	 */
	@Override
	@RFC(ID = 35)
	public void GetYou(@PU(Index = 1) MyUser p_user, @PI int roomId, @PI int ret) {
		// TODO Auto-generated method stub
		Room r = RoomManager.getInstance().getRoom(roomId);
		RoomUser ru = r.getRoomUser(p_user);
		if (ret == 0) {
			ru.setYou(eYouState.NONE);

		} else if (ret == 1) {// 点的悠
			eCardState result = r.huCard(p_user);
			System.err.print("\n接收到悠牌的消息!!");
			if (null != result) {
				System.err.print("\n悠牌胡牌了！！！！！！！！！！！！！！！！");
				r.processResult(p_user, ret, true);// 这里计算分数了;
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(-1);
				r.broadcast(p_user, RoomInterface.MID_OPER_CARD, eCardOper.MID_BROADCAST_HUCARD, list);
			}
		}

		SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_GET_YOU);
		p.Add(1);
		p.Send(p_user);
	}

	private void LogRecord(MyUser user, String record) {
		if (null != user) {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, user.GetRoleGID(), record);
		} else {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, 0l, record);
		}
	}

	@Override
	@RFC(ID = 38)
	public void TestJieSuan(@PU(Index = 1) MyUser p_user, @PI int round, @PST int fan, @PI int wanfa,
			@PVI ArrayList<Integer> list, @PI int jifen, @PVI ArrayList<Integer> list2, @PI int zimo, @PI int dianpao,
			@PI int ytl, @PI int qys, @PI int qysytl, @PI int hy, @PI int mg, @PI int ag, @PI int piao) {
		// TODO Auto-generated method stub
		RoomUser roomUser = new RoomUser(p_user, "100");
		IDeskBalance<YZDeskBalance> db = (YZDeskBalance) roomUser.getDeskBalance();
		RoomRule rr = new RoomRule(round, fan, wanfa, list, jifen, list2);
		int qd = 0;
		boolean HasHui = false;
		ArrayList<TestUser> tlist = TestUser.getSingle().getlist();
		tlist.get(0).set(roomUser);
		tlist.get(1).set(1, false, 0, 0);
		tlist.get(2).set(2, false, 0, 0);
		tlist.get(3).set(3, false, 0, 0);

		Iterator<TestUser> it = tlist.iterator();
		// 先算杠才能才能有正确的输出日志
		while (it.hasNext()) {
			TestUser obj = it.next();
			obj.sumGang(tlist);

		}
		Iterator<TestUser> its = tlist.iterator();
		while (its.hasNext()) {
			TestUser obj = its.next();
			if (obj.getM_roleId() == roomUser.getRoleId()) {
				obj.changeGangTo();
			}
		}

		db.sumHu(HasHui, qd, zimo, ytl, qys, qysytl, hy, tlist.get(0).getM_piao() ? 1 : 0, tlist, rr, 1);
	}

	@Override
	@RFC(ID = 40)
	public void AutoCard(@PU(Index = 1) MyUser p_user, @PI int roomId) {
		// TODO Auto-generated method stub
		Room r = RoomManager.getInstance().getRoom(roomId);
		if (null != r) {
			RoomUser ru = r.getRoomUser(p_user);
			System.err.print("\n接收到自动摸牌出牌的消息!! \n");
			/*
			 * if (null != ru && ru.getYou() == eYouState.NOTICE_YOU) {
			 * ru.setYou(eYouState.NONE); CardInfo ci = r.pullCard(p_user);
			 * ru.setCurrentCard(ci); r.broadcast(RoomInterface.MID_LAST_CARD,
			 * r.getCardCount()); LogRecord(p_user, "剩余牌数" + r.getCardCount());
			 * System.err.printf("\n接收到自动摸:%s \n", ci.getName());
			 * r.startPushCard(ru, ci);
			 * }
			 */
		/*	r.startPullCard(ru);*/
			/*	OperCard(p_user, type, list);*/
		}

	}

	@Override // 取消解散房间
	public void cenceldis(@PU(Index = 1) MyUser p_user, @PI int roomId) {

		Room room = RoomManager.getInstance().getRoom(roomId);
		if (room != null) {
			room.cancleRoom();
		}
	}

}
