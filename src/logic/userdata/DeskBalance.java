package logic.userdata;

import java.util.ArrayList;
import java.util.Iterator;

import core.detail.impl.log.Log;
import core.detail.impl.socket.SendMsgBuffer;
import logic.MyUser;
import logic.eSuanFenType;
import logic.module.log.eLogicDebugLogType;
import logic.module.room.CardInfo;
import logic.module.room.Room;
import logic.module.room.RoomRule;
import logic.module.room.RoomUser;
import manager.RoomManager;


public class DeskBalance extends IDeskBalance<DeskBalance> {
	
	
	public int zimo; //自摸;底分1分；
	public int dianpao; //点炮;();底分2分；
	
	public int ssy; //十三幺;10番；
	public int qd; //七对;2番；
	public int ytl; //一条龙：2番；
	
	public int qys; //清一色;4番；
	public int hdly; //海底捞月;2番 ；点炮者出3家的钱（不分大包小包3家出）；
	public int gskh; //杠上开花;2番；
	public int qgh; //抢杠胡;2番
	
	public int zg; //直杠;
	public int fzg; //首圈开杠; 庄家给每家+1；
	public int mg; //明杠;
	public int ag; //暗杠；
	
	
	public int zhuangjia; //庄家胡牌翻倍;
	public int firstgang; //首圈庄家被开杠；
	public int koupai; //扣牌；
	public int dajiang; //大将；
	public DeskBalance(RoomUser ru){
		m_user = ru;
		ru.setDeskBalance(this);
	}
	
	public void packData(SendMsgBuffer buffer){
		buffer.Add(zimo);
		buffer.Add(dianpao);
		buffer.Add(ssy);
		buffer.Add(qd);
		buffer.Add(ytl);
		buffer.Add(qys);
		buffer.Add(hdly);
		buffer.Add(gskh);
		buffer.Add(qgh);
		buffer.Add(zg);
		buffer.Add(fzg);
		buffer.Add(mg);
		buffer.Add(ag);
		buffer.Add(genzhuang);
		buffer.Add(zhuangjia);
		buffer.Add(firstgang);
		buffer.Add(koupai);
		buffer.Add(dajiang);
		
	}
	@Override
	public void reset(){
		//
		zimo = 0;
		dianpao = 0;
		ssy = 0;
		qd = 0;
		ytl = 0;
		qys = 0;
		hdly = 0;
		gskh = 0;
		qgh = 0;
		zg = 0;
		fzg = 0;
		mg = 0;
		ag = 0;
		genzhuang = 0;
		zhuangjia = 0;
		firstgang = 0;
		koupai = 0;
		dajiang = 0;
	}
	
	private int MAX(int v1, int v2, int v3){
		int maxFan1 = Math.max(Math.max(v1, v2), v3);
		int maxFan2 = Math.min(Math.min(v1, v2), v3);
		if(maxFan1 == 0){
			return maxFan2;
		}
		return maxFan1;
	}
	@Override
	public int calScore(RoomUser winner){
		
		//如果是庄家赢
		
		int maxFan = MAX(ssy, qd, ytl);
		int total = zimo + dianpao + maxFan + qys + hdly + gskh + qgh ;
		int ret =  total + zg + fzg + mg + ag + genzhuang + firstgang;
		
		
		Room r = RoomManager.getInstance().getRoom(m_user.getUser().getRoomId());
		RoomRule rr = r.getRoomRule();
		int max = rr.getMaxFan();
		if(ret < 0){
			return Math.max(ret, -max);
		}
		
		return Math.min(ret, max);
	}
	@Override
	public void processHu(CardInfo card, int size, ArrayList<RoomUser> users, RoomUser winner){
		int difen = 1; // 底分;
		int sanren = 3; //3个人的分都要给赢家;
		zhuangjia = m_user.getZhuang() == 1 ? 2 : 0;
		int _zhuangjia = m_user.getZhuang() == 1 ? 2 : 1; // 不把庄家X2加到其他的番上面；
		
		boolean isDianpao = null != card;
		///////////////////////////////////////////////////////
		if(isDianpao){
			difen = 2;
		}
		zimo = card == null ? 2 * _zhuangjia * difen : 0;
		ssy = m_user.isShiSanyao(card) ? 10 * _zhuangjia * difen: 0; //十三幺;
		int _count = m_user.isQidui(card);//七对;
		switch(_count){
		case 1:
			qd = 2 * _zhuangjia * difen;
		case 2:
			qd = 4 * _zhuangjia * difen;
			break;
		case 3:
			qd = 8 * _zhuangjia * difen;
			break;
		case 4:
			qd = 16 * _zhuangjia * difen;
			break;
		}
		
		ytl = m_user.isLong(card) ? 2 * _zhuangjia * difen: 0; //一条龙;
		qys = m_user.isQingYise() ? 4 * _zhuangjia * difen: 0;//清一色;
		qgh = this.isQiangGangHu(card, users) ? 2 * _zhuangjia * difen: 0; //抢杠胡;
		
		Room r = RoomManager.getInstance().getRoom(this.m_user.getRoomId());
    	RoomRule rr = r.getRoomRule();
		hdly = (size <= Room.ms_lastcard + 4 && rr.hasHdly()) ? 2 * _zhuangjia * difen : 0; //海底捞月;
		gskh = m_user.isGangShang() ? 2 * _zhuangjia * difen: 0; // 杠上开花：
		
		//int _zhuangjia =  1; // 不把庄家X2加到其他的番上面；
		
		//-------------处理自摸;
		//zimo *= _zhuangjia; //庄家 * 2;
		
		koupai = (int) Math.pow(2, m_user.getKouCount());
		dajiang = (int) Math.pow(2, m_user.getDajiangCount());
		if(koupai == 1)
			koupai = 0;
		if(dajiang == 1)
			dajiang = 0;
		
		//赢家把3个人的钱都加上;
		if(this.m_user.getZhuang() == 0){
			//如果赢家不是庄，需要把庄多输的钱也加上;
			sanren = 4; //多一个人的分数;
		}
		Iterator<RoomUser> it = users.iterator();
		
		if(isDianpao){ //如果是点炮
			if(rr.getSuanFen() == eSuanFenType.DABAO || rr.getSuanFen() == eSuanFenType.XIAOBAO){ //如果是大包，需要包其他两家的钱，
				if(rr.getSuanFen() == eSuanFenType.XIAOBAO){
					sanren = 1;
				}
				//如果赢家不是庄家，就包四个人的钱，如果是庄家就需要包六个人的钱;
				DeskBalance db = (DeskBalance) card.getOwner().getDeskBalance();
				db.zimo -= zimo * sanren;
				db.ssy -= ssy * sanren;
				db.qd -= qd * sanren;
				db.ytl -= ytl * sanren;
				db.qys -= qys * sanren;
				db.qgh -= qgh * sanren;
				db.hdly -= hdly * sanren;
				db.gskh -= gskh * sanren;
				db.koupai -= koupai * sanren;
				db.dajiang -= dajiang * sanren;
			}
		}
		//
		if(!isDianpao && rr.getSuanFen() == eSuanFenType.SANJIACH){
			//自摸的情况下的处理;
			
			while(it.hasNext()){
				RoomUser ru = it.next();
				if(ru.getRoleId() != m_user.getRoleId()){
					int bei = 1;
					if(ru.getZhuang() == 1){
						bei = 2;
					}
					DeskBalance db = (DeskBalance) ru.getDeskBalance();
					db.zimo -= zimo * bei;
					db.ssy -= ssy * bei;
					db.qd -= qd * bei;
					db.ytl -= ytl * bei;
					db.qys -= qys * bei;
					db.qgh -= qgh * bei;
					db.hdly -= hdly * bei;
					db.gskh -= gskh * bei;
					db.koupai -= koupai * bei;
					db.dajiang -= dajiang * bei;
				}
			}
		}
		zimo *= sanren;
		ssy *= sanren;
		qd *= sanren;
		ytl *= sanren;
		qys *= sanren;
		qgh *= sanren;
		hdly *= sanren;
		gskh *= sanren;
		koupai *= sanren;
		dajiang *= sanren;
		
		
		if(null != card){
			//找到是谁点炮的,点炮的玩家-3;
			assert(card.getOwner() != null);
			DeskBalance db = (DeskBalance) card.getOwner().getDeskBalance();
			if(rr.getSuanFen() == eSuanFenType.DABAO){
				db.dianpao = -(2 * _zhuangjia + 4);
				this.dianpao = 2 * _zhuangjia + 4;

			}else if(rr.getSuanFen() == eSuanFenType.XIAOBAO){
				db.dianpao = -(2 * _zhuangjia);
				this.dianpao = 2 * _zhuangjia;
			}else if(rr.getSuanFen() == eSuanFenType.SANJIACH){
				this.dianpao = 6 * _zhuangjia;
				if(m_user.getZhuang() == 1){
					this.dianpao = 12;
				}else{
					this.dianpao = 6;
				}
				it = users.iterator();
				while(it.hasNext()){
					RoomUser ru = it.next();
					if(ru.getRoleId() != m_user.getRoleId()){
						if(ru.getZhuang() == 1){
							db.dianpao -= 4;
						}else{
							db.dianpao -= 2;
						}
					}
				}
			}
		}
		
		String str = "";
		str += String.format("zimo:%d\n", zimo);
		str += String.format("ssy:%d\n", ssy);
		str += String.format("qd:%d\n", qd);
		str += String.format("ytl:%d\n", ytl);
		str += String.format("qys:%d\n", qys);
		str += String.format("qgh:%d\n", qgh);
		str += String.format("hdly:%d\n", hdly);
		str += String.format("gskh:%d\n", gskh);
		str += String.format("koupai:%d\n", koupai);
		str += String.format("dajiang:%d\n", dajiang);
		str += String.format("dianpao:%d\n", dianpao);
		LogRecord(this.m_user.getUser(), str);
	}
	
	private boolean isQiangGangHu(CardInfo card, ArrayList<RoomUser> users){
		if(null == card){
			return false;
		}
		Iterator<RoomUser> it = users.iterator();
		while(it.hasNext()){
			RoomUser ru = it.next();
			if(ru.getRoleId() != this.m_user.getRoleId() && card.getOwner().getRoleId() != ru.getRoleId()){
				boolean gang = ru.checkGangCard(card, false);
				if(gang){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void processGang(CardInfo card,ArrayList<RoomUser> users, RoomUser winner){
		ArrayList<CardInfo> list = m_user.getZhiGang();//直杠的数量;(杠应该返回数组，有可能是多个杠)
		zg += list.size(); // 开了几个直杠;
		Iterator<RoomUser> it = users.iterator();
		//找到谁打的直杠的牌;
		Iterator<CardInfo> it2 = list.iterator();
		while(it2.hasNext()){
			boolean first = false;
			CardInfo ci = it2.next();
			DeskBalance db = (DeskBalance) ci.getOwner().getDeskBalance();
			db.zg -= 1;//这个人打过直杠的牌，需要扣分;
			if(ci.getOwner().getZhuang() == 1 && ci.getOwner().getMingCardPosition(ci) == 0){
				db.firstgang -= 3;//如果是庄家的首轮开杠;
				//庄家被首圈开杠了;除了庄家的其他的人+1; 注意这个和直杠是分开的，直杠的那个人单独+1;
				it = users.iterator();
				while(it.hasNext()){
					RoomUser ru = it.next();
					if(first){
						if(ru.getZhuang() == 0){
							db.firstgang += 1;
						}
					}
				}
			}
		}
		
		int sanren = 3;
		mg += m_user.getMingGang().size() * 1 * sanren;
		ag += m_user.getAnGang().size() * 2 * sanren;
		
		//其他的玩家都要-1 * count;
		it = users.iterator();
		while(it.hasNext()){
			RoomUser ru = it.next();
			if(ru.getRoleId() != this.m_user.getRoleId()){
				DeskBalance db = (DeskBalance) ru.getDeskBalance();
				db.mg -= m_user.getMingGang().size() * 1;
				db.ag -= m_user.getAnGang().size() * 2;
			}
		}
		
		String str = "";
		str += String.format("zg:%d\n", zg);
		str += String.format("firstgang:%d\n", firstgang);
		str += String.format("mg:%d\n", mg);
		str += String.format("ag:%d\n", ag);
		LogRecord(this.m_user.getUser(), str);
		
	}
	
	private void LogRecord(MyUser user, String record)
	{
		if(null != user){
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, user.GetRoleGID(), record);
		}else{
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, 0l, record);
		}
	}
	
	
	
}

