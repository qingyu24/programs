package logic.module.room;

import java.util.ArrayList;
import java.util.Iterator;
import core.detail.impl.log.Log;
import logic.module.log.eLogicDebugLogType;
import logic.module.room.RoomUser.piaoTag;
import logic.userdata.YZDeskBalance;

public class TestUser {

	private RoomUser m_user;

	private long m_roleId; // 保存一份roleid;
	private Boolean m_piao; // 是否是飘：
	private Boolean m_dianpao; // 是否点炮
	private int m_mg; // 明杠个数;0
	private int m_ag; // 暗杠个数；
	private int m_zg; // 暗杠个数；
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

	public static ArrayList<TestUser> list;
	
	private static TestUser _tu;
	public static TestUser getSingle(){
		if(_tu == null){
			_tu = new TestUser();
		}
		return _tu;
	}

	public ArrayList<TestUser> getlist() {
		if (list != null) {
			return list;
		} else {
			list = new ArrayList<TestUser>();
			list.add(new TestUser());
			list.add(new TestUser());
			list.add(new TestUser());
			list.add(new TestUser());
			return list;
		}
		// TODO Auto-generated method stub

	}

	public TestUser() {
		this.reset();
		// TODO Auto-generated constructor stub
	}

	public TestUser set(RoomUser m_user) {
		this.reset();
		m_roleId = m_user.getRoleId();
		m_piao = (m_user.getPiao() == piaoTag.PIAO);
        this.Log("这个能不能飘："+m_piao);
		this.m_mg = m_user.getMingGang().size();
		this.m_ag = m_user.getAnGang().size();
		this.m_zg = m_user.getZhiGang().size();
		
		this.m_user=m_user;

		return this;
	}

	public TestUser set(long m_roleId, Boolean m_piao, int m_mg, int m_ag) {
		this.reset();
		this.m_roleId = m_roleId;
		this.m_piao = m_piao;

		this.m_mg = m_mg;
		this.m_ag = m_ag;
		return this;
	}

	public void set( Boolean m_piao, int m_mg, int m_ag) {

		this.m_piao = m_piao;

		this.m_mg = m_mg;
		this.m_ag = m_ag;
	
	}
	public int getM_zg() {
		return m_zg;
	}

	public void setM_zg(int m_zg) {
		this.m_zg = m_zg;
	}

	public Boolean getM_dianpao() {
		return m_dianpao;
	}

	public void setM_dianpao(Boolean m_dianpao) {
		this.m_dianpao = m_dianpao;
	}

	public int getM_mg() {
		return m_mg;
	}

	public void setM_mg(int m_mg) {
		this.m_mg = m_mg;
	}

	public int getM_ag() {
		return m_ag;
	}

	public void setM_ag(int m_ag) {
		this.m_ag = m_ag;
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

		mg = 0;
		ag = 0;
	}

	public long getM_roleId() {
		return m_roleId;
	}

	public void setM_roleId(long m_roleId) {
		this.m_roleId = m_roleId;
	}

	public Boolean getM_piao() {
		return m_piao;
	}

	public void setM_piao(Boolean m_piao) {
		this.m_piao = m_piao;
	}

	// 将分数同步到roomUser中
	public void changeTo() {
		YZDeskBalance db = (YZDeskBalance) this.m_user.getDeskBalance();
		db.zimo += zimo;
		db.dianpao += dianpao;
		db.qd += qd;
		db.ytl += ytl;
		db.qys += qys;
		db.qysytl += qysytl;
		db.qgh += qgh;
		db.hy += hy;

		db.piao += piao;


	}
	public void changeGangTo() {
		YZDeskBalance db = (YZDeskBalance) this.m_user.getDeskBalance();
		db.mg = mg;
		db.ag = ag;
	}

	// 输出日志
	public void LogRecord() {
		if (null != this) {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, this.getM_roleId(), this.toString());
		}
	}
	public void Log(String str) {
		if (null != this) {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, this.getM_roleId(),str);
		}
	}

	public void sumGang(ArrayList<TestUser> list) {
		int sanren = 3;
		mg += this.m_mg * 1 * sanren;
		ag += this.m_ag * 2 * sanren;
		mg += this.m_zg * 1 * sanren;

		// 其他的玩家都要-1 * count;
		Iterator<TestUser> it = list.iterator();
		while (it.hasNext()) {
			TestUser ru = it.next();
			if (ru.getM_roleId() != this.getM_roleId()) {

				ru.mg -= this.m_mg * 1;
				ru.ag -= this.m_ag * 2;
				ru.mg -= this.m_zg * 1;
			}
		}

	}

	public void setCard(YZDeskBalance db) {

		zimo = db.zimo;
		dianpao=db.dianpao;
		qd = db.qd;
		ytl = db.ytl;
		qys = db.qys;
		qysytl = db.qysytl;
		qgh = db.qgh;
		hy = db.hy;
		piao = db.piao;

	}

	@Override
	public String toString() {
		int total = zimo + dianpao + hy + qys + qgh + piao + qd + qysytl + ytl;
		int ret = total + mg + ag;
		return "roleId: " + m_roleId + " [ zimo=" + zimo +" ,dianpao="+dianpao+ ", qd=" + qd + ", ytl=" + ytl + ", qys=" + qys + ", qysytl="
				+ qysytl + ", qgh=" + qgh + ", hy=" + hy + ", mg=" + mg + ", ag=" + ag + ", piao=" + piao + "]"+"合计总分"+ret;
	}


}
