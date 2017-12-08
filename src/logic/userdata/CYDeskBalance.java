package logic.userdata;

import java.util.ArrayList;
import java.util.Iterator;

import core.detail.impl.log.Log;
import core.detail.impl.socket.SendMsgBuffer;
import logic.MyUser;
import logic.eWanFaType_cy;
import logic.module.log.eLogicDebugLogType;
import logic.module.room.CardInfo;
import logic.module.room.Room;
import logic.module.room.RoomRule;
import logic.module.room.RoomUser;
import manager.RoomManager;

public class CYDeskBalance extends IDeskBalance<CYDeskBalance> {


	public int zimo; //自摸;底分1分；
	public int dianpao; //点炮;();底分2分；
	public int pihu; //屁胡;
	public int danhu;//单胡一张;
	public int qidui; //七对;
	public int piaohu; //飘胡;
	public int lihu; //立胡;
	public int bimen; //闭门;
	public int mobao; //三家闭门;  //摸宝胡
	public int bimen4; //四家闭门;
	public int gskh; //杠上开花;
	public int gspao; //杠上炮；
	public int hdly; //海底捞月;
	public int huangzhuang; //晃庄；
	public int qys; //清一色；
	public int mg; //明杠;
	public int ag; //暗杠；
	public int cg; //彩杠;
	public int acg; //暗彩杠；
	public int ren; //暗彩杠；
	public int mPiao; //明票；
	public int shuJ;
	public int ispiao;

	//葫芦岛玩法的算分规则;
	public CYDeskBalance(RoomUser ru){
		m_user = ru;
		ru.setDeskBalance(this);
	}

	@Override
	public void packData(SendMsgBuffer buffer) {
		// TODO Auto-generated method stub
         
		buffer.Add(zimo>0?zimo:0); //自摸;底分1分；
		buffer.Add(dianpao<0?dianpao:0); //点炮;();底分1分；
		buffer.Add(pihu); //屁胡;
		buffer.Add(danhu);//单胡一张;
		buffer.Add(qidui); //七对;
		buffer.Add(ispiao); //飘胡;
		buffer.Add(lihu); //立胡;
		buffer.Add(m_user.isBimen()?1:0); //闭门;
		buffer.Add(mobao); //三家闭门;
		buffer.Add(bimen4); //四家闭门;
		buffer.Add(gskh); //杠上开花;
		buffer.Add(gspao); //杠上炮；
		buffer.Add(hdly); //海底捞月);
		buffer.Add(huangzhuang); //晃庄；
		buffer.Add(mg>0?mg:0); //；
		buffer.Add(ag>0?ag:0); //；
		buffer.Add(cg); //；
		buffer.Add(acg); //；
		

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		zimo = 0; //自摸;底分1分；
		dianpao = 0; //点炮;();底分2分；
		pihu = 0; //屁胡;
		danhu = 0; //单胡一张
		qidui = 0; //七对;
		piaohu = 0; //飘胡;
		lihu = 0; //立胡;
		bimen = 0; //闭门;
		mobao = 0; //三家闭门;
		bimen4 = 0; //四家闭门;
		gskh = 0; //杠上开花;
		gspao = 0; //杠上炮；
		hdly = 0; //海底捞月;
		huangzhuang = 0; //晃庄；
		ag = 0;
		mg = 0;
		cg = 0;
		acg = 0;
		ispiao=0;
		shuJ=0;
	}

	
	//根据玩法选择做变动
	private void cyLocal() {
RoomRule rr = RoomManager.getInstance().getRoom(m_user.getRoomId()).getRoomRule();
if(!rr.hasWanFaCY(eWanFaType_cy.jiahu)){
	danhu=0;
}
if(!rr.hasWanFaCY(eWanFaType_cy.lihu)){
	lihu=0;
}
if(rr.hasWanFaCY(eWanFaType_cy.qingyise)){
if(m_user.isQingYise()){
	
}
}

	}
	
	
	@Override
	public int calScore(RoomUser winner) {
		cyLocal();
		
		// TODO Auto-generated method stub
		int zimo1 = zimo == 0 ? 1 : zimo;
		int dianpao1 = dianpao == 0 ? 1 : dianpao;
		int pihu1 = pihu == 0 ? 1 : pihu;//+
		int danhu1 = danhu == 0 ? 1 : danhu;// +
		// int qidui = qidui == 0 ? 1 : qidui;//+
		// int piaohu = zimo == 0 ? 1 : zimo;//+
		int lihu1 = lihu == 0 ? 1 : lihu;
		int bimen1 = bimen == 0 ? 1 : bimen;
		int mobao1 = mobao == 0 ? 1 : mobao;
		int bimen41 = bimen4 == 0 ? 1 : bimen4;
		int gskh1 = gskh == 0 ? 1 : gskh;
		int gspao1 = gspao == 0 ? 1 : gspao; 
		int hdly1 = hdly == 0 ? 1 : hdly;
		int ren1 = ren == 0 ? 1 : ren;

		int hu=danhu1>lihu1?danhu1:lihu1;
		if(piaohu>0){
			danhu=0;
			danhu1=1;
			lihu=0;
			lihu1=1;
			hu=piaohu;
		}

		if(zimo==0&&dianpao==0){
			dianpao1=shuJ;
		}
		int bei = zimo1 * dianpao1 * hu*pihu1  * mobao1 * bimen41 * gskh1 * gspao1 * hdly1 ;

		if(bei>=4||bei<=-4){
			bei=bei/4*5;
		}
		bei=bei*ren1;
		if(zimo==0&&dianpao==0&&shuJ==0) {
			bei=0;
		}       
		/*	int plus = qidui>piaohu?qidui:piaohu;;
		if(plus>0){
			bei=0;
		}*/


		int gang = mg + ag + cg + acg;

		int ret=  bei + gang;
		if (ret < 0) {
			return Math.max(ret, -150);
		}
		return Math.min(ret, 3 * 150);
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
	private void biMen(ArrayList<RoomUser> users,RoomUser winner,CardInfo card) {

		int  wb=winner.isBimen()?2:1;
		int zhuang=winner.getZhuang()==1?2:1;
		Iterator<RoomUser> it = users.iterator();
		CYDeskBalance loseDb=null; 
		if(card!=null){
			loseDb = (CYDeskBalance) card.getOwner().getDeskBalance();
		}
		while (it.hasNext()) {
			RoomUser ru = (RoomUser) it.next();
			if(ru.getRoleId()!=winner.getRoleId()){
				CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
				int bei =ru.getZhuang()==1?2:1;
				int bm =ru.isBimen()?2:1;
				int  diaopao=1;
				if(card!=null){
					long roleId = card.getOwner().getRoleId();
					if(ru.getUser().GetRoleGID()==roleId){
						diaopao=2;
					}
				}
				int bb=1;//比对比
			
				int   _ren =bei*wb*zhuang*bm*diaopao;
				LogRecord(m_user.getUser(),"人次明细"+_ren+"="+"赢家闭门"+wb+"赢家庄"+zhuang+"*自己庄"+bei+"自己闭门"+bm +"是否点炮"+diaopao+"闭对比"+bb);

				if(loseDb!=null&&RoomManager.getInstance().getRoom(ru.getRoomId()).getRoomRule().hasWanFaCY(eWanFaType_cy.zuoche)){
					//点炮者的全包
					loseDb.ren+=_ren;
				}else{
					db.ren =_ren;
				}
				this.ren+= _ren;
			}

		}

	}

	@Override
	public void processHu(CardInfo card, int size, ArrayList<RoomUser> users, RoomUser winner) {
		// TODO Auto-generated method stub
		boolean isDianpao = null != card;
		
		//计算闭门和庄家项分数
		this.biMen(users, winner, card);
		Room room = RoomManager.getInstance().getRoom(m_user.getRoomId());
		if (isDianpao&&room.getRoomRule().hasWanFaCY(eWanFaType_cy.zuoche)) {

			CYDeskBalance db = (CYDeskBalance) card.getOwner().getDeskBalance();


			db.dianpao = -1;
			this.dianpao = 1;

			if (this.m_user.danhu) {
				this.danhu = 2;
				db.danhu = 2;
			} else {
				this.pihu = 1;
				db.pihu = 1;
			}
			int qiduiCount = m_user.isQidui(card);
			if (qiduiCount > 0) {
				this.qidui = 8;
				db.qidui = 8;
			}
			// 是否是飘胡;
			// 手上都是碰牌和杠牌的时候才是飘牌
			if (this.m_user.getHandCardCount() == 2 && !this.m_user.hasChiCard()) {
				this.piaohu = 4;
				db.piaohu = 4;
				this.ispiao=1;
			}else	if(this.m_user.piaohu){
				this.piaohu = 2;
				db.piaohu = 2;	
			}
			// 是否是立胡;
			/*	int handCount = this.m_user.getHandCardCount();
			if (handCount == 14) {
				this.lihu = 2;
				db.lihu = 2;
			}
			//
			 */			int biCount = 0; // 闭门的数量;

			 biCount=room.getbiCount(m_user);

			 if (biCount >= 3) {

				 RoomRule rr = room.getRoomRule();
				 /*if (rr.hasWanFaJZ(eWanFaType_jz.SIJIABI)) {
					// 如果选择了三家闭;
					db.mobao = 2;
					this.mobao = 2;


				}*/
				 if (rr.hasWanFaCY(eWanFaType_cy.sijiabi) && m_user.isBimen()) {
					 db.bimen4 = 2;
					 this.bimen4 = 2;
				 }
			 }
			 // 杠上炮；
			 if (card.getFromGang()) {

				 this.gspao = 2;
				 db.gspao = 2;
				 if(card.getFromGangAn()){
					 this.gspao = 4;
					 db.gspao = 4;
				 }
			 }

		}else if(isDianpao){
			CYDeskBalance dbs = (CYDeskBalance) card.getOwner().getDeskBalance();
			dbs.dianpao = -1;
			this.dianpao = 1;
			int qiduiCount = m_user.isQidui(card);
			boolean zhuang = m_user.getZhuang()==1; 

			Iterator<RoomUser> it = users.iterator();

			if (this.m_user.danhu) {
				this.danhu = 2;
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
						db.danhu = 2;
						db.shuJ=-1;
					}
				}

			} else  {
				this.pihu = 1 ;
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
						db.pihu = 1;
						db.shuJ = -1;
					}
				}
			}
		/*	if (qiduiCount > 0) {
				this.qidui = 40*3 ;
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
						db.qidui = -40;
					}
				}
			}*/

			if ((this.m_user.getHandCardCount() == 2 && !this.m_user.hasChiCard())||(this.m_user.piaohu)) {
				this.piaohu = 2;
				it = users.iterator();
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
						db.piaohu = 2;
					}
				}
			}
			
		
					int biCount = 0; // 闭门的数量;

			 biCount=room.getbiCount(m_user);

			 if (biCount >= 3) {

				 RoomRule rr = room.getRoomRule();

				 if (rr.hasWanFaCY(eWanFaType_cy.sijiabi) && m_user.isBimen()) {
					 this.bimen4 = 2;
				 }
			 }
			 // 杠上炮；
			 if (card.getFromGang()) {

				 this.gspao = 2;

				 if(card.getFromGangAn()){
					 this.gspao = 4;
			
				 }
			 }
			 Iterator<RoomUser> its = users.iterator();
				while (its.hasNext()) {
					RoomUser ru = its.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
						db.bimen4 = this.bimen4;
						db.gspao=this.gspao;
					}
				}

			 
			 
		} 
		else {
			// 是自摸;
           
			this.zimo = 2;
			
			int qiduiCount = m_user.isQidui(card);
			boolean zhuang = m_user.getZhuang()==1; 

			Iterator<RoomUser> it = users.iterator();

			if (this.m_user.danhu) {
				this.danhu = 2;
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
						db.danhu = 2;
						db.zimo=-2;
					}
				}

			} else  {
				this.pihu = 1 ;
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
						db.pihu = 1;
						db.zimo = -2;
					}
				}
			}
			if (qiduiCount > 0) {
				this.qidui = 40*3 ;
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
						db.qidui = -40;
					}
				}
			}

			if ((this.m_user.getHandCardCount() == 2 && !this.m_user.hasChiCard())||(this.m_user.piaohu)) {
				this.piaohu = 2;
				it = users.iterator();
				while (it.hasNext()) {
					RoomUser ru = it.next();
					if (ru.getRoleId() != this.m_user.getRoleId()) {
						CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
						db.piaohu = 2;
					}
				}
			}
			
		
		
			int handCount = this.m_user.getHandCardCount();
	
			int biCount = room.getbiCount(m_user); // 闭门的数量;
			if (biCount >= 3) {

				RoomRule rr = room.getRoomRule();
			
				if (rr.hasWanFaCY(eWanFaType_cy.sijiabi)&& m_user.isBimen()) {
					it = users.iterator();
					while (it.hasNext()) {
						RoomUser ru = it.next();
						CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
						db.bimen4 = 2;

					}
				}
			}
			// this.m_user.isGangShang(); //是否是杠上开花;
			if (this.m_user.isGangShang()) {
				int fen=2;
				if(this.m_user.isCaiGangShang()){
					fen=4;
				}

				it = users.iterator();
				while (it.hasNext()) {
					RoomUser ru = it.next();
					CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
					db.gskh = fen;
				}
			}
			// 海底捞月;
			if (this.m_user.isHaiDi()) {

				it = users.iterator();
				while (it.hasNext()) {
					RoomUser ru = it.next();

					CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();
					db.hdly = 2;

				}
			}
		}
        int bao=0;
        //选择听牌了
		if(winner.hasTing){
			bao=2;
		}
		
		//计算门清
		if(m_user.isBimen()){
			Iterator<RoomUser> it = users.iterator();
			while (it.hasNext()) {
				RoomUser roomUser = (RoomUser) it.next();
				CYDeskBalance deskBalance = (CYDeskBalance) roomUser.getDeskBalance();
				deskBalance.lihu=2;
				//计算摸宝
				deskBalance.mobao=bao;
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
		str += String.format("mobao:%d\t", mobao); // 三家闭门;
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
		str += String.format("合计总分:%d\t", calScore(null));
		LogRecord(this.m_user.getUser(), str);
	}
	


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
		str += String.format("摸宝胡:%d\t", mobao); // 三家闭门;
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
		str += String.format("合计总分:%d\t", calScore(null));

		LogRecord(this.m_user.getUser(), str);

	}




	@Override
	public void processGang(CardInfo card, ArrayList<RoomUser> users, RoomUser winner) {
		// TODO Auto-generated method stub
		int sanren = 3;
		int fb1 = 0;// 明
		int fb2 = 0; // 暗杠

		Iterator<CardInfo> iterator = m_user.getMingGang().iterator();
		while (iterator.hasNext()) {
			CardInfo cardInfo = (CardInfo) iterator.next();

			fb1 += 1;

		}
		Iterator<CardInfo> it2 = m_user.getAnGang().iterator();
		while (it2.hasNext()) {
			CardInfo cardInfo = (CardInfo) it2.next();

			fb2 += 1;
		}


		// 把直杠和明杠合并了;
		mg += 2 * sanren * fb1;

		ag += 5 * sanren * fb2;


		// 处理直杠;

		// 其他的玩家都要-1 * count;
		Iterator<RoomUser> it = users.iterator();
		while (it.hasNext()) {
			RoomUser ru = it.next();
			if (ru.getRoleId() != this.m_user.getRoleId()) {
				CYDeskBalance db = (CYDeskBalance) ru.getDeskBalance();

				db.mg -= fb1 * 2;
				// db.mg -= m_user.getZhiGang().size() * 2;

				db.ag -= fb2 * 5;

			}
		}


		String str = "\t";
		str += String.format("mg:%d\t", mg);
		str += String.format("ag:%d\t", ag);
		str += String.format("cg:%d\t", cg);
		str += String.format("acg:%d\t", acg);
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
