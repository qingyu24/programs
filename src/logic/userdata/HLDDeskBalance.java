package logic.userdata;

import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;

import core.detail.impl.log.Log;
import core.detail.impl.socket.SendMsgBuffer;
import logic.MyUser;
import logic.eWanFaType_hld;
import logic.module.log.eLogicDebugLogType;
import logic.module.room.CardInfo;
import logic.module.room.Room;
import logic.module.room.RoomRule;
import logic.module.room.RoomUser;
import logic.module.room.TestUser;
import manager.RoomManager;

public class HLDDeskBalance extends IDeskBalance<HLDDeskBalance> {

	public int zimo; // 自摸;底分1分；
	public int dianpao; // 点炮;();底分2分；
	public int pihu; // 屁胡;
	public int qionghu; //穷胡;
	public int danhu;// 单胡一张;
	public int qidui; // 七对;
	public int piaohu; // 飘胡;
	public int lihu; // 立胡;
	public int bimen; // 闭门;
	public int bimen3; // 三家闭门;
	public int bimen4; // 四家闭门;
	public int gskh; // 杠上开花;
	public int gspao; // 杠上炮；
	public int hdly; // 海底捞月;
	public int huangzhuang; // 晃庄；
	public int mg; // 明杠;
	public int ag; // 暗杠；
	public int cg; // 彩杠;
	public int acg; // 暗彩杠；
	public int liuju;

	public int ren;

	// 葫芦岛玩法的算分规则;
	public HLDDeskBalance(RoomUser ru) {
		m_user = ru;
		ru.setDeskBalance(this);
	}

	@Override
	public void packData(SendMsgBuffer buffer) {
		// TODO Auto-generated method stub


	if(zimo>0||dianpao>0){
		buffer.Add(zimo>0?zimo:0); //自摸;底分1分；
		buffer.Add(dianpao<0?dianpao:0); //点炮;();底分1分；
		buffer.Add(pihu); // 屁胡;
		buffer.Add(danhu);// 单胡一张;
		buffer.Add(qidui>0?qidui:0); // 七对;
		buffer.Add(piaohu>0?piaohu:0); // 飘胡;
		buffer.Add(lihu); // 立胡;
		buffer.Add(m_user.isBimen()?1:0); //闭门;
		buffer.Add(bimen3); // 三家闭门;
		buffer.Add(bimen4); // 四家闭门;
		buffer.Add(gskh); // 杠上开花;
		buffer.Add(gspao>0?gspao:0); // 杠上炮；
		buffer.Add(hdly>0?hdly:0); // 海底捞月);
		buffer.Add(huangzhuang>0?huangzhuang:0); // 晃庄；
		buffer.Add(qionghu>0?qionghu:0);
		
	}else{
		buffer.Add(0); //自摸;底分1分；
		buffer.Add(dianpao<0?dianpao:0); //点炮;();底分1分；
		buffer.Add(0); // 屁胡;
		buffer.Add(0);// 单胡一张;
		buffer.Add(0); // 七对;
		buffer.Add(0); // 飘胡;
		buffer.Add(0); // 立胡;
		buffer.Add(m_user.isBimen()?1:0); //闭门;
		buffer.Add(0); // 三家闭门;
		buffer.Add(0); // 四家闭门;
		buffer.Add(0); // 杠上开花;
		buffer.Add(0); // 杠上炮；
		buffer.Add(0); // 海底捞月);
		buffer.Add(0); // 晃庄；
		buffer.Add(0); //穷胡;
	}

	buffer.Add(mg>0?mg:0); //；
	buffer.Add(ag>0?ag:0); //；
	buffer.Add(cg>0?ag:0); //；
	buffer.Add(acg>0?acg:0); //；
	
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		zimo = 0; // 自摸;底分1分；
		dianpao = 0; // 点炮;();底分2分；
		pihu = 0; // 屁胡;
		danhu = 0; // 单胡一张
		qidui = 0; // 七对;
		piaohu = 0; // 飘胡;
		lihu = 0; // 立胡;
		bimen = 0; // 闭门;
		bimen3 = 0; // 三家闭门;
		bimen4 = 0; // 四家闭门;
		gskh = 0; // 杠上开花;
		gspao = 0; // 杠上炮；
		hdly = 0; // 海底捞月;
		huangzhuang = 0; // 晃庄；
		ag = 0;
		mg = 0;
		cg = 0;
		acg = 0;
		liuju=0;
		ren = 0;
		qionghu = 0;
	}

	@Override
	public int calScore(RoomUser winner) {
		// TODO Auto-generated method stub
		int zimo1 = zimo == 0 ? 1 : zimo;
		int dianpao1 = dianpao == 0 ? 1 : dianpao;
		int pihu1 = pihu == 0 ? 1 : pihu;// +
		int danhu1 = danhu == 0 ? 1 : danhu;// +
		// int qidui = qidui == 0 ? 1 : qidui;//+
		// int piaohu = zimo == 0 ? 1 : zimo;//+
		int lihu1 = lihu == 0 ? 1 : lihu;
		int bimen1 = bimen == 0 ? 1 : bimen;
		int bimen31 = bimen3 == 0 ? 1 : bimen3;
		int bimen41 = bimen4 == 0 ? 1 : bimen4;
		int gskh1 = gskh == 0 ? 1 : gskh;
		int gspao1 = gspao == 0 ? 1 : gspao; // 这个还没弄;
		int hdly1 = hdly == 0 ? 1 : hdly;
		int ren1 = ren == 0 ? 1 : ren;
		int qionghu1 = qionghu == 0 ? 1 : qionghu;
		int hu = danhu1 > lihu1 ? danhu1 : lihu1; //立胡和单胡叠加计算；

		int bei = zimo1 * dianpao1 * pihu1 * bimen31 * bimen41 * gskh1 * gspao1 * hdly1 * ren1 * qionghu1 * danhu1;
		if (zimo == 0 && dianpao == 0) {//屁胡忽略所有的倍数;
			bei = 0;
		}
		if(qidui!=0){
			piaohu=0;
		}
		int plus = qidui+piaohu;
		;
		if (plus != 0) {
			bei = 0;
			
			pihu = 0; // 屁胡;
			danhu = 0; // 单胡一张
			lihu = 0; // 立胡;
			bimen = 0; // 闭门;
			bimen3 = 0; // 三家闭门;
			bimen4 = 0; // 四家闭门;
			gskh = 0; // 杠上开花;
			gspao = 0; // 杠上炮；
			hdly = 0; // 海底捞月;
			huangzhuang = 0; // 晃庄；
			
		}

		int gang = mg + ag + cg + acg;

		return plus + bei + gang + liuju;
	}

	private int biCount(ArrayList<RoomUser> users) {
		int biCount = 0; // 闭门的数量;

		Iterator<RoomUser> it = users.iterator();
		while (it.hasNext()) {
			RoomUser ru = it.next();
			if (ru.getHandCardCount() == 14) {
				biCount++;
			}
		}
		return biCount;
	}

	// 闭门的分数倍数计算
	private void biMen(ArrayList<RoomUser> users, RoomUser winner, CardInfo card) {

		int wb = winner.isBimen() ? 2 : 1;
		int zhuang = winner.getZhuang() == 1 ? 2 : 1;
		Iterator<RoomUser> it = users.iterator();
		HLDDeskBalance loseDb = null;
		if (card != null) {
			loseDb = (HLDDeskBalance) card.getOwner().getDeskBalance();
		}
		while (it.hasNext()) {
			RoomUser ru = (RoomUser) it.next();
			if (ru.getRoleId() != winner.getRoleId()) {
				HLDDeskBalance db = (HLDDeskBalance) ru.getDeskBalance();
				int bei = ru.getZhuang() == 1 ? 2 : 1;
				int bm = ru.isBimen() ? 2 : 1;
				int diaopao = 1;
				if (card != null) {
					long roleId = card.getOwner().getRoleId();
					if (ru.getUser().GetRoleGID() == roleId) {
						diaopao = 2;
					}
				}
				int _ren = bei * wb * zhuang * bm * diaopao;
				LogRecord(m_user.getUser(), "人次明细" + _ren + "=" + "赢家闭门" + wb + "赢家庄" + zhuang + "*自己庄" + bei + "自己闭门"
						+ bm + "pao" + diaopao);

				if (loseDb != null) {
					// 点炮者的全包
					loseDb.ren += _ren;
				} else {
					db.ren = _ren;
				}
				this.ren += _ren;
			}

		}

	}

	@Override
	public void processHu(CardInfo card, int size, ArrayList<RoomUser> users, RoomUser winner) {
		// TODO Auto-generated method stub
		boolean isDianpao = null != card;
		// 计算闭门和庄家项分数
		this.biMen(users, winner, card);
		// 计算门清
		Room room = RoomManager.getInstance().getRoom(m_user.getRoomId());
		if (m_user.isBimen() && room.getRoomRule().hasWanFaHLD(eWanFaType_hld.MENQING)) {
			//this.lihu = 2;//立胡X2
			Iterator<RoomUser> it = users.iterator();
			while (it.hasNext()) {
				RoomUser roomUser = (RoomUser) it.next();
				if(roomUser.getRoleId() != m_user.getRoleId()){
					HLDDeskBalance deskBalance = (HLDDeskBalance) roomUser.getDeskBalance();
					deskBalance.lihu = 0;
				}
			}
		}
		
		this.qionghu = this.m_user.getHuiCount() > 0 ? 0 : 2;
		
		if (isDianpao) {

			HLDDeskBalance db = (HLDDeskBalance) card.getOwner().getDeskBalance();
			db.qionghu = this.qionghu;
			db.dianpao = -1;
			this.dianpao = 1;
			// 庇护单胡先不算
			if (this.m_user.danhu) {
				this.danhu = 2;
				db.danhu = 2;
			} else {
				this.pihu = 1;
				db.pihu = 1;
			}
			int qiduiCount = m_user.isQidui(card);
			if (qiduiCount > 0) {
				this.qidui = 80;
				db.qidui = -80;
			}
			// 是否是飘胡;
			// 手上都是碰牌和杠牌的时候才是飘牌
			if ((this.m_user.piaohu)&& !this.m_user.hasChiCard()){
				this.piaohu = 70;
				db.piaohu = -70;
			}
			// 是否是立胡;
			/*
			 * int handCount = this.m_user.getHandCardCount(); if (handCount ==
			 * 14) { this.lihu = 2; db.lihu = 2; } //
			 */ int biCount = 0; // 闭门的数量;

			biCount = room.getbiCount(m_user);

			if (biCount >= 3) {

				RoomRule rr = room.getRoomRule();
				if (rr.hasWanFaHLD(eWanFaType_hld.SANJIABI)) {
					// 如果选择了三家闭;
					db.bimen3 = 2;
					this.bimen3 = 2;
				}
				if (rr.hasWanFaHLD(eWanFaType_hld.SIJIABI) && m_user.isBimen()) {
					db.bimen4 = 2;
					this.bimen4 = 2;
				}
			}
			// 杠上炮；
			
			if (card.getFromGang()) {
				this.gspao = 2;
				db.gspao = 2;
			}
			

		} else {
			// 是自摸;

			this.zimo = 2;
			int qiduiCount = m_user.isQidui(card);
			boolean zhuang = m_user.getZhuang() == 1;

			Iterator<RoomUser> it = users.iterator();
			
			if(this.m_user.getHuiCount() <= 0)
			{
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						HLDDeskBalance db = (HLDDeskBalance) ru.getDeskBalance();
						db.qionghu = 2;
					}
				}
			}
			if (this.m_user.danhu) {
				this.danhu = 2;
				it = users.iterator();
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						HLDDeskBalance db = (HLDDeskBalance) ru.getDeskBalance();
						db.danhu = 2;
						db.zimo = -2;
					}
				}

			} else {
				this.pihu = 1;
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						HLDDeskBalance db = (HLDDeskBalance) ru.getDeskBalance();
						db.pihu = 1;
						db.zimo = -2;
					}
				}
			}
			if (qiduiCount > 0) {
				this.qidui = 40 * 3;
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						HLDDeskBalance db = (HLDDeskBalance) ru.getDeskBalance();
						db.qidui = -40;
					}
				}
			}

				if ((this.m_user.getHandCardCount() == 2 && !this.m_user.hasChiCard())||(this.m_user.piaohu)) {
				this.piaohu = 30 * 3;
				it = users.iterator();
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						HLDDeskBalance db = (HLDDeskBalance) ru.getDeskBalance();
						db.piaohu = -30;
					}
				}
			}
			int handCount = this.m_user.getHandCardCount();
			/*
			 * if (handCount == 14) { this.lihu = 2 ; it = users.iterator();
			 * while (it.hasNext()) { RoomUser ru = it.next(); if
			 * (ru.getRoleId() != this.m_user.getRoleId()) { HLDDeskBalance db =
			 * (HLDDeskBalance) ru.getDeskBalance(); db.lihu = 2; } } }
			 */
			//
			int biCount = room.getbiCount(m_user); // 闭门的数量;
			if (biCount >= 3) {

				RoomRule rr = room.getRoomRule();
				if (rr.hasWanFaHLD(eWanFaType_hld.SANJIABI)) {
					// 如果选择了三家闭;

					it = users.iterator();
					while (it.hasNext()) {
						RoomUser ru = it.next();

						HLDDeskBalance db = (HLDDeskBalance) ru.getDeskBalance();
						db.bimen3 = 2;

					}
				}

				if (rr.hasWanFaHLD(eWanFaType_hld.SIJIABI) && m_user.isBimen()) {
					it = users.iterator();
					while (it.hasNext()) {
						RoomUser ru = it.next();
						HLDDeskBalance db = (HLDDeskBalance) ru.getDeskBalance();
						db.bimen4 = 2;

					}
				}
			}
			// this.m_user.isGangShang(); //是否是杠上开花;
			if (this.m_user.isGangShang()) {

				it = users.iterator();
				while (it.hasNext()) {
					RoomUser ru = it.next();
					HLDDeskBalance db = (HLDDeskBalance) ru.getDeskBalance();
					db.gskh = 2;

				}
			}
			// 海底捞月;
			if (this.m_user.isHaiDi()) {

				it = users.iterator();
				while (it.hasNext()) {
					RoomUser ru = it.next();

					HLDDeskBalance db = (HLDDeskBalance) ru.getDeskBalance();
					db.hdly = 2;

				}
			}
		}
		String str = "";

		str += String.format("\tzimo:%d\t ", zimo); // 自摸;底分1分；
		str += String.format("dianpao:%d\t", dianpao); // 点炮;();底分1分；
		str += String.format("pihu:%d\t", pihu); // 屁胡;
		str += String.format("danhu:%d\t", danhu);// 单胡一张;
		str += String.format("qidui:%d\t", qidui); // 七对;
		str += String.format("piaohu:%d\t", piaohu); // 飘胡;
		str += String.format("lihu:%d\t", lihu); // 立胡;
		str += String.format("bimen:%d\t", bimen); // 闭门;
		str += String.format("bimen3:%d\t", bimen3); // 三家闭门;
		str += String.format("bimen4:%d\t", bimen4); // 四家闭门;
		str += String.format("gskh:%d\t", gskh); // 杠上开花;
		str += String.format("gspao:%d\t", gspao); // 杠上炮；
		str += String.format("hdly:%d\t", hdly); // 海底捞月);
		str += String.format("慌庄:%d\t", huangzhuang); // 海底捞月);
		str += String.format("renci:%d\t", ren); // 海底捞月);
		str += String.format("mg:%d\t", mg);
		str += String.format("ag:%d\t", ag);
		str += String.format("cg:%d\t", cg);
		str += String.format("acg:%d\t", acg);
		str += String.format("qionghu:%d\t", qionghu);
		str += String.format("合计总分:%d\t", calScore(null));
		LogRecord(this.m_user.getUser(), str);
	}
@Override
	public void logScore() {
		String str = "玩家分數" + m_user.getUser().GetUserName();
		str += String.format("\tzimo:%d\t", zimo); // 自摸;底分1分；
		str += String.format("dianpao:%d\t", dianpao); // 点炮;();底分1分；
		str += String.format("pihu:%d\t", pihu); // 屁胡;
		str += String.format("danhu:%d\t", danhu);// 单胡一张;
		str += String.format("qidui:%d\t", qidui); // 七对;
		str += String.format("piaohu:%d\t", piaohu); // 飘胡;
		str += String.format("lihu:%d\t", lihu); // 立胡;
		str += String.format("bimen:%d\t", bimen); // 闭门;
		str += String.format("bimen3:%d\t", bimen3); // 三家闭门;
		str += String.format("bimen4:%d\t", bimen4); // 四家闭门;
		str += String.format("gskh:%d\t", gskh); // 杠上开花;
		str += String.format("gspao:%d\t", gspao); // 杠上炮；
		str += String.format("hdly:%d\t", hdly); // 海底捞月);
		str += String.format("慌庄:%d\t", huangzhuang); // 海底捞月);
		str += String.format("renci:%d\t", ren); // 海底捞月);
		str += String.format("mg:%d\t", mg);
		str += String.format("ag:%d\t", ag);
		str += String.format("cg:%d\t", cg);
		str += String.format("acg:%d\t", acg);
		str += String.format("qionghu:%d\t", qionghu);
		str += String.format("合计总分:%d\t", calScore(null));
		
	ArrayList<Integer> list = new ArrayList<Integer>();
	list.add(zimo>0?zimo:0); //自摸;底分1分；
	list.add(dianpao<0?dianpao:0); //点炮;();底分1分；
	list.add(pihu); // 屁胡;
	list.add(danhu);// 单胡一张;
	list.add(qidui>0?qidui:0); // 七对;
	list.add(piaohu>0?piaohu:0); // 飘胡;
	list.add(lihu); // 立胡;
	list.add(m_user.isBimen()?1:0); //闭门;
	list.add(bimen3); // 三家闭门;
	list.add(bimen4); // 四家闭门;
	list.add(gskh); // 杠上开花;
	list.add(gspao>0?gspao:0); // 杠上炮；
	list.add(hdly>0?hdly:0); // 海底捞月);
	list.add(huangzhuang>0?huangzhuang:0); // 晃庄；
	list.add(mg>0?mg:0); 
	list.add(ag>0?ag:0); 
	list.add(cg>0?ag:0); 
	list.add(acg>0?acg:0); 
		LogRecord(this.m_user.getUser(), str);
		LogRecord(this.m_user.getUser(), list.toString());

	}

	@Override
	public void processGang(CardInfo card, ArrayList<RoomUser> users, RoomUser winner) {
		// TODO Auto-generated method stub
		int sanren = 3;
		int fb1 = 0;// 明杠
		int fbc1 = 0;// 明彩
		int fb2 = 0; // 暗杠
		int fbc2 = 0;// 暗彩
		int fb3 = 0; // 直杠
		Iterator<CardInfo> iterator = m_user.getMingGang().iterator();
		while (iterator.hasNext()) {
			CardInfo cardInfo = (CardInfo) iterator.next();
			if (cardInfo.getType() > 7) {
				fbc1 += 1;
			} else {
				fb1 += 1;
			}
		}
		Iterator<CardInfo> it2 = m_user.getAnGang().iterator();
		while (it2.hasNext()) {
			CardInfo cardInfo = (CardInfo) it2.next();
			if (cardInfo.getType() > 7) {
				fbc2 += 1;
			} else {
				fb2 += 1;
			}
		}
		Iterator<CardInfo> it3 = m_user.getZhiGang().iterator();
		while (it3.hasNext()) {
			CardInfo cardInfo = (CardInfo) it3.next();
			if (cardInfo.getType() > 7) {
				fbc1 += 1;
				//HLDDeskBalance db = (HLDDeskBalance) cardInfo.getOwner().getDeskBalance();
				//db.cg -= 4;
				///cg += 4;
			} else {
				fb1 += 1;
				//fb3 += 1;
				//HLDDeskBalance db = (HLDDeskBalance) cardInfo.getOwner().getDeskBalance();
				//db.mg -= 6; // 这里cg 是直杠
				//mg += 6;
			}
		}
		// 把直杠和明杠合并了;
		mg += 2 * sanren * fb1;
		cg += 4 * sanren * fbc1;
		ag += 4 * sanren * fb2;
		acg += 8 * sanren * fbc2;

		// 处理直杠;

		// 其他的玩家都要-1 * count;
		Iterator<RoomUser> it = users.iterator();
		while (it.hasNext()) {
			RoomUser ru = it.next();
			if (ru.getRoleId() != this.m_user.getRoleId()) {
				HLDDeskBalance db = (HLDDeskBalance) ru.getDeskBalance();
				db.mg -= 2 *  fb1;
				db.cg -= 4 *  fbc1;
				db.ag -= 4 *  fb2;
				db.acg -= 8 *  fbc2;
				
				//db.mg -= fb1 * 2;
				// db.mg -= m_user.getZhiGang().size() * 2;
				//cg -= 4 * fbc1;
				//db.ag -= fb2 * 4;
				//cg -= 8 * sanren * fbc2;
				
			}
		}

		String str = "\t";
		str += String.format("mg:%d\t", mg);
		str += String.format("ag:%d\t", ag);
		str += String.format("cg:%d\t", cg);
		str += String.format("acg:%d\t", acg);
		LogRecord(this.m_user.getUser(), str);
	}

	private void LogRecord(MyUser user, String record) {
		if (null != user) {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, user.GetRoleGID(), record);
		} else {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, 0l, record);
		}
	}

	public void packLiuju(SendMsgBuffer b) {
		// TODO Auto-generated method stub
		b.Add(mg > 0 ? mg : 0);
		b.Add(ag > 0 ? ag : 0);
		b.Add(cg > 0 ? cg : 0);
		b.Add(acg > 0 ? acg : 0);

	}
}
