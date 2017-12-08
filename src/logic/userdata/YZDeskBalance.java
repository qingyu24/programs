package logic.userdata;

import java.util.ArrayList;
import java.util.Iterator;
import core.detail.impl.log.Log;
import core.detail.impl.socket.SendMsgBuffer;
import logic.MyUser;
import logic.eSuanFenType;
import logic.eWanFaType_yz;
import logic.eYouState;
import logic.module.log.eLogicDebugLogType;
import logic.module.room.CardInfo;
import logic.module.room.Room;
import logic.module.room.RoomRule;
import logic.module.room.RoomUser;
import logic.module.room.RoomUser.piaoTag;
import logic.module.room.TestUser;
import manager.CardManager;
import manager.RoomManager;
import test.TestManger;

public class YZDeskBalance extends IDeskBalance<YZDeskBalance> {

	public int zimo; // 自摸;底分1分；
	public int dianpao; // 点炮;();底分2分；

	public int qd; // 七对;2番；
	public int ytl; // 一条龙：2番；

	public int qys; // 清一色;4番；
	public int qysytl;

	public int qgh; // 抢杠胡;2番
	public int hy; // 会儿悠；

	public int mg; // 明杠;0
	public int ag; // 暗杠；

	public int piao;

	public int ispiao;// 有沒有選擇票

	public int koupai;// 扣牌
	public int dajiang;// 大將

	public YZDeskBalance(RoomUser ru) {
		m_user = ru;
		ru.setDeskBalance(this);
	}
	public void packData(SendMsgBuffer buffer) {
		buffer.Add(zimo);
		buffer.Add(dianpao);
		buffer.Add(qd);
		buffer.Add(ytl);
		buffer.Add(qys);
		buffer.Add(qysytl);
		buffer.Add(qgh);
		buffer.Add(hy);
		buffer.Add(piao);
		buffer.Add(mg);
		buffer.Add(ag);
		buffer.Add(ispiao);

		LogRecord(m_user.getUser(), this.toString() + "打包数据时总分" + calScore(m_user));
	}

	public void reset() {
		zimo = 0;
		dianpao = 0;
		qd = 0;
		ytl = 0;
		qys = 0;
		qysytl = 0;
		qgh = 0;
		hy = 0;
		piao = 0;
		koupai = 1;
		dajiang = 1;

		mg = 0;
		ag = 0;
		ispiao = m_user.getPiao() == piaoTag.PIAO ? 1 : 0;
	}

	public int calScore(RoomUser winner) {

		if(qd!=0&&ytl!=0){
			ytl=0;
		}
		int total = zimo + dianpao + hy + qys + qgh + piao + qd + qysytl + ytl;
		int ret = total + mg + ag;
		Room r = RoomManager.getInstance().getRoom(m_user.getUser().getRoomId());
		int max=100;
		if(r!=null){
		if(r.getRoomRule()!=null){
		max = r.getRoomRule().getMaxFan();}}
		if (ret < 0) {
			return Math.max(ret, -max);
		}
		return Math.min(ret, 3 * max);
	}

	@Override
	public void processHu(CardInfo card, int size, ArrayList<RoomUser> users, RoomUser winner) {

			boolean iszimo = null == card;
		boolean _hasHui = m_user.getHuiCount() > 0;
		if (this.m_user.getYou() == eYouState.NOTICE_YOU) {
			iszimo = true;
			// todo 如果是悠牌的话就是自摸;
		}

		RoomRule rr = RoomManager.getInstance().getRoom(m_user.getRoleId()).getRoomRule();
		int _zimo = iszimo ? 1 : 0;
		int _ytl = m_user.isLong(card) ? 1 : 0;
		int _qys = m_user.isQingYise() ? 1 : 0;
		int _qysytl = (_ytl > 0 && _qys > 0) ? 1 : 0;
		int _hy = m_user.getYou() == eYouState.NOTICE_YOU ? 1 : 0;
		/* int _hy=m_user.isYouhu?1:0; */
		int _piao = m_user.getPiao() == piaoTag.PIAO ? 1 : 0;
		int _qd = m_user.isQidui(card) > 0 ? 1 : 0;

		ArrayList<TestUser> list = TestUser.getSingle().getlist();

		for (int i = 0; i < users.size(); i++) {
			list.get(i).set(users.get(i));
		}
		long cardowner = 0;
		if (card != null) {
			cardowner = card.getOwner().getRoleId();
		}
		this.sumHu(_hasHui, _qd, _zimo, _ytl, _qys, _qysytl, _hy, _piao, list, rr, cardowner);
		for (TestUser testUser : list) {
			testUser.changeTo(); // 同步对应的RoomUser中的数据
		}
	}

	public void testprocessHu(CardInfo card, RoomUser user, boolean zimo) {
		boolean iszimo = TestManger.iszimo();
		boolean _hasHui = m_user.getHuiCount() > 0;
		if (this.m_user.getYou() == eYouState.NOTICE_YOU) {
			iszimo = true;
			// todo 如果是悠牌的话就是自摸;
		}
		RoomRule rr = new RoomRule(1, 3, 3, new ArrayList<Integer>(), TestManger.is_bao, new ArrayList<Integer>());
		int _zimo = iszimo ? 1 : 0;
		int _ytl = m_user.isLong(card) ? 1 : 0;
		int _qys = m_user.isQingYise() ? 1 : 0;
		int _qysytl = (_ytl > 0 && _qys > 0) ? 1 : 0;
		int _hy = m_user.getYou() == eYouState.NOTICE_YOU ? 1 : 0;
		int _piao = m_user.getPiao() == piaoTag.PIAO ? 1 : 0;
		int _qd = m_user.isQidui(card) > 0 ? 1 : 0;
		int _qgh = m_user.isQiangGangHu(card) ? 1 : 0;
		ArrayList<TestUser> list = TestManger.getList(m_user);

		long cardowner = TestManger.dianpao_id;
		if (card != null) {
			/* cardowner = card.getOwner().getRoleId() */;
		}

		for (int i = 0; i < list.size(); i++) {
			list.get(i).sumGang(list);
			if (this.m_user.getRoleId() == list.get(i).getM_roleId()) {
				list.get(i).changeGangTo();
			}
		}
		for (int i = 0; i < list.size(); i++) {

			if (this.m_user.getRoleId() == list.get(i).getM_roleId()) {
				list.get(i).changeGangTo();
			}
		}
		this.sumHu(_hasHui, _qd, _zimo, _ytl, _qys, _qysytl, _hy, list.get(0).getM_piao() ? 1 : 0, list, rr, cardowner);

	}

	@Override
	public void processGang(CardInfo card, ArrayList<RoomUser> users, RoomUser winner) {
		ArrayList<TestUser> list = TestUser.getSingle().getlist();
		for (int i = 0; i < users.size(); i++) {
			list.get(i).set(users.get(i));
		}
		for (int i = 0; i < users.size(); i++) {
			list.get(i).sumGang(list);
		}

		for (TestUser testUser : list) {
			testUser.changeGangTo();

		}

	}

	@Override
	public void sumHu(boolean _hasHui, int _qd, int _zimo, int _ytl, int _qys, int _qysytl, int _hy, int _piao,
			ArrayList<TestUser> users, RoomRule rr, long dpID) {
		if (rr.hasWanFaYZ(eWanFaType_yz.KOUPAI)) {
			koupai = (int) Math.pow(2, m_user.getKouCount());
		
			if(rr.hasWanFaYZ(eWanFaType_yz.DAJIANG)){
			dajiang = (int) Math.pow(2, m_user.getDajiangCount());
			
		}
		}
		if(koupai==0){
			koupai=1;
		}
		if(dajiang==0){
			dajiang=1;
		}
        LogRecord(m_user.getUser(), "扣牌倍 "+koupai+"大將倍數"+dajiang);
		if (_zimo == 1) {
			// 如果是自摸的话：
			int iszimo = _hasHui ? 1 : 2;
			int ishy = (_hy == 0) ? 1 : 2;
			int bei = iszimo * ishy * koupai * dajiang;

			ytl = (_ytl == 1) ? 4 : 0;
			ytl = ytl * bei;

			qd = (_qd == 1) ? 4 : 0;
			qd = qd * bei;

			if (_qys == 1) {
				if (_ytl == 1) {
					qys = 6 * bei;
					ytl = 4 * bei;
				}
				if (_qd == 1) {
					qd = 4 * bei;
					qys = 6 * bei;
				}
				if (_ytl != 1 && _qd != 1) {
					qys = 6;
					qys = qys * bei;
				}
			}
			int total = qys + qgh + qd + qysytl + ytl;
			if (total == 0) {
				if (_zimo == 1) {
					if (_hasHui) {
						if (_hy == 1) {
							hy = 2*koupai*dajiang;
						} else {
							zimo = 1*koupai*dajiang;
						}

					} else {
						zimo = 2*koupai*dajiang;
					}
				} else {

					dianpao = 1*koupai*dajiang;
				}
			}

			Iterator<TestUser> it = users.iterator();
			int piaoBei = (_piao == 1) ? 2 : 0;
			while (it.hasNext()) {
				TestUser ru = it.next();
				if (ru.getM_roleId() != this.m_user.getRoleId()) {
					if (ru.getM_piao()) {
						ru.piao -= (2 + piaoBei);
						piao += (2 + piaoBei);
					} else {
						ru.piao -= piaoBei;
						piao += piaoBei;
					}
				}
			}
			it = users.iterator();
			while (it.hasNext()) {
				TestUser ru = it.next();
				if (ru.getM_roleId() != this.m_user.getRoleId()) {
					ru.zimo -= zimo;
					ru.dianpao -= dianpao;
					ru.ytl -= ytl;
					ru.qd -= qd;
					ru.qys -= qys;
					ru.qysytl -= qysytl;
					ru.qgh -= qgh;
					ru.hy -= hy;
				}
			}
			zimo *= 3;
			dianpao *= 3;
			qd *= 3;
			ytl *= 3;
			qys *= 3;
			qysytl *= 3;
			qgh *= 3;
			hy *= 3;

		} else {
		
			int bei =  koupai * dajiang;
			ytl = (_ytl == 1) ? 4 : 0;
			ytl = ytl * bei;
			qd = (_qd == 1) ? 4 : 0;
			qd = qd * bei;
			if (_qys == 1) {
				if (_ytl == 1) {
					qys = 6 * bei;
					ytl = 4 * bei;
				} else if (_qd == 1) {
					qd = 4 * bei;
					qys = 6 * bei;
				} else {
				
					qys = 6 * bei;
				}
			}
			int total = qys + qgh + qd + qysytl + ytl;
			if (total == 0) {
				dianpao = 1*bei;
			}

			if (rr.getSuanFen() == eSuanFenType.DABAO) {

				for (int i = 0; i < users.size(); i++) {
					if (users.get(i).getM_roleId() == dpID) {
						TestUser user = users.get(i);
						user.zimo -= zimo * 3;
						user.dianpao -= dianpao * 3;
						user.ytl -= ytl * 3;
						user.qd -= qd * 3;
						user.qys -= qys * 3;
						user.qysytl -= qysytl * 3;
						user.hy -= hy * 3;

						int piaoBei = (_piao == 1) ? 2 : 0;
						Iterator<TestUser> its = users.iterator();
						while (its.hasNext()) {
							TestUser ru = its.next();
							if (ru.getM_roleId() != this.m_user.getRoleId()) {
								if (ru.getM_piao()) {
									user.piao -= (2 + piaoBei);
									piao += (2 + piaoBei);
								} else {
									user.piao -= piaoBei;
									piao += piaoBei;
								}
							}
						}

					}
				}

			} else {
				Iterator<TestUser> it = users.iterator();
				while (it.hasNext()) {
					TestUser ru = it.next();
					if (ru.getM_roleId() != this.m_user.getRoleId()) {
						ru.zimo -= zimo;
						ru.dianpao -= dianpao;
						ru.ytl -= ytl;
						ru.qd -= qd;
						ru.qys -= qys;
						ru.qysytl -= qysytl;
						ru.hy -= hy;
					}
				}

				int piaoBei = (_piao == 1) ? 2 : 0;
				Iterator<TestUser> its = users.iterator();
				while (its.hasNext()) {
					TestUser ru = its.next();
					if (ru.getM_roleId() != this.m_user.getRoleId()) {
						if (ru.getM_piao()) {
							ru.piao -= (2 + piaoBei);
							piao += (2 + piaoBei);
						} else {
							ru.piao -= piaoBei;
							piao += piaoBei;
						}
					}
				}
			}
			zimo *= 3;
			dianpao *= 3;
			qd *= 3;
			ytl *= 3;
			qys *= 3;
			qysytl *= 3;
			qgh *= 3;
			hy *= 3;
		}
		if (CardManager.isTest) {
			Iterator<TestUser> its = users.iterator();

			for (TestUser testUser : users) {
				if (testUser.getM_roleId() == this.m_user.getRoleId()) {
					this.LogRecord(m_user.getUser(), this.toString());
				} else {
					testUser.LogRecord();
				}

			}
		}
	}

	@Override
	public String toString() {
		int total = zimo + dianpao + hy + qys + qgh + piao + qd + qysytl + ytl;
		int ret = total + mg + ag;
		String str = "roleId: "+ this.m_user.getRoleId() + "合计总分" + calScore(m_user) + " [zimo=" + zimo + " ,diaopao=" + dianpao + ", qd=" + qd
				+ ", ytl=" + ytl + ", qys=" + qys + ", qysytl=" + qysytl + ", qgh=" + qgh + ", hy=" + hy + ", mg=" + mg
				+ ", ag=" + ag + ", piao=" + piao + "]"
				 ;
		return str;
	}

	private void LogRecord(MyUser user, String record) {
		if (null != user) {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, user.GetRoleGID(), record);
		} else {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, 0l, record);
		}
	}
	public void logScore() {};
	
}
