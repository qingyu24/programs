package logic.module.room;

import java.util.ArrayList;
import java.util.Iterator;

import core.detail.impl.log.Log;
import core.detail.impl.socket.SendMsgBuffer;
import logic.MyUser;
import logic.eFanType;
import logic.eHuPaiType;
import logic.eRoundType;
import logic.eRoundType_yz;
import logic.eRoundType_cy;
import logic.eSuanFenType;
import logic.eWanFaType;
import logic.eWanFaType_cy;
import logic.eWanFaType_hld;
import logic.eWanFaType_jz;
import logic.eWanFaType_sx;
import logic.eWanFaType_yz;
import logic.module.log.eLogicDebugLogType;
import manager.CardManager;

public class RoomRule {
	private eRoundType m_round;
	private eRoundType_yz m_roundyz;
	private eRoundType_cy m_roundcy; //朝阳；
	private eFanType m_fan;
	private ArrayList<eWanFaType> m_wanfa;
	private ArrayList<eWanFaType_hld> m_wanfa_hld;
	private ArrayList<eWanFaType_yz> m_wanfa_yz;
	private ArrayList<eWanFaType_jz> m_wanfa_jz;
	private ArrayList<eWanFaType_sx> m_wanfa_sx;
	private ArrayList<eWanFaType_cy> m_wanfa_cy;
	private eHuPaiType m_hupai;
	private eSuanFenType m_jifen;
	private int m_wanfa1;
	private ArrayList<Integer> m_base_wanfa1;
	private ArrayList<Integer> m_base_wanfa2;
	private int m_hdly; // 还得捞月；
	private int m_gskh; // 杠上开花;

	public RoomRule(int round, int fan, int wanfa, ArrayList<Integer> list, int jifen, ArrayList<Integer> list2) {

		m_base_wanfa1 = list;
		m_base_wanfa2 = list2;
		if (CardManager.isbaoding) {
			// 是保定的玩法;
			m_round = eRoundType.values()[round - 1];
			m_fan = eFanType.values()[fan - 1];
			m_wanfa = new ArrayList<eWanFaType>();
			m_wanfa1 = wanfa;
			m_wanfa.add(eWanFaType.values()[wanfa - 1]);

			for (int i = 0; i < list.size(); ++i) {
				int c = list.get(i);
				if (c > 0) {
					m_wanfa.add(eWanFaType.values()[c + 1]);
				}
			}
			m_jifen = eSuanFenType.values()[jifen - 1];
			m_hdly = list2.get(0);
			m_gskh = list2.get(1);

			String str = null;
			str += String.format("\n是否是推到胡:%s\n", this.hasWanFa(eWanFaType.TUIDAOHU));
			str += String.format("是否是胡八章:%s\n", this.hasWanFa(eWanFaType.HUBAZHANG));
			str += String.format("是否是扣牌:%s\n", this.hasWanFa(eWanFaType.KOUPAI));
			str += String.format("是否是大将:%s\n", this.hasWanFa(eWanFaType.DAJIANG));
			str += String.format("最大圈数:%s\n", this.m_round);
			str += String.format("最大翻数:%s\n", this.m_fan);
			str += String.format("算分规则:%s\n", this.m_jifen);

			LogRecord(null, str);
		}

		if (CardManager.ishuludao) {
			// 如果是葫芦岛的玩法;
			m_wanfa_hld = new ArrayList<eWanFaType_hld>();
			m_round = eRoundType.values()[round - 1];
			m_fan = eFanType.values()[fan - 1];
			m_jifen = eSuanFenType.values()[jifen - 1];
			m_wanfa1 = wanfa;

			// m_wanfa_hld.add(eWanFaType_hld.values()[m_wanfa1 - 1]);

			for (int i = 0; i < list2.size(); ++i) {
				int c = list2.get(i);
				if (c > 0) {
					m_wanfa_hld.add(eWanFaType_hld.values()[i]);
				}
			}
			LogRecord(null, "客户端发来的玩法规则"+list2.toString());
			LogRecord(null, "玩法规则对应"+"	SHANGFANHUI, ;QUFENGPAI, QIDUI,	MENQING,SANJIABI,	SIJIABI;");
			String str = null;
			str += String.format("\n能否上翻会儿:%s\n", this.hasWanFaHLD(eWanFaType_hld.SHANGFANHUI));
			str += String.format("是否去风牌:%s\n", this.hasWanFaHLD(eWanFaType_hld.QUFENGPAI));
			str += String.format("是否三家闭:%s\n", this.hasWanFaHLD(eWanFaType_hld.SANJIABI));
			str += String.format("是否四家闭:%s\n", this.hasWanFaHLD(eWanFaType_hld.SIJIABI));
			str += String.format("是否门清:%s\n", this.hasWanFaHLD(eWanFaType_hld.MENQING));
			str += String.format("是否七对儿:%s\n", this.hasWanFaHLD(eWanFaType_hld.QIDUI));

			str += String.format("最大圈数:%s\n", this.m_round);
			str += String.format("最大翻数:%s\n", this.m_fan);
			LogRecord(null, str);
		}
		if (CardManager.isshanxi) {
			// 如果是陕西;
			m_wanfa_sx = new ArrayList<eWanFaType_sx>();
			m_round = eRoundType.values()[round - 1];
			m_fan = eFanType.values()[fan - 1];
			m_jifen = eSuanFenType.values()[jifen - 1];
			m_wanfa1 = wanfa;

			// m_wanfa_hld.add(eWanFaType_hld.values()[m_wanfa1 - 1]);

			if (list.get(0) == 1)
				m_wanfa_sx.add(eWanFaType_sx.DAIFENG);
			else
				m_wanfa_sx.add(eWanFaType_sx.BUDAIFENG);
			if (list.get(1) == 1)
				m_wanfa_sx.add(eWanFaType_sx.DIANPAOYIXIANG);
			else
				m_wanfa_sx.add(eWanFaType_sx.DIANPAOSHIXIANG);
			if (list2.get(0) == 1)
				m_wanfa_sx.add(eWanFaType_sx.SHIGUO);
			if (list2.get(1) == 1)
				m_wanfa_sx.add(eWanFaType_sx.CHIPAI);

			String str = null;
			str += String.format("\nBUDAIFENG:%s\n", this.hasWanFaSX(eWanFaType_sx.BUDAIFENG));
			str += String.format("CHIPAI:%s\n", this.hasWanFaSX(eWanFaType_sx.CHIPAI));
			str += String.format("DAIFENG:%s\n", this.hasWanFaSX(eWanFaType_sx.DAIFENG));
			str += String.format("DIANPAOSHIXIANG:%s\n", this.hasWanFaSX(eWanFaType_sx.DIANPAOSHIXIANG));
			str += String.format("DIANPAOYIXIANG:%s\n", this.hasWanFaSX(eWanFaType_sx.DIANPAOYIXIANG));
			str += String.format("SHIGUO:%s\n", this.hasWanFaSX(eWanFaType_sx.SHIGUO));
			str += String.format("最大圈数:%s\n", this.m_round);
			str += String.format("最大翻数:%s\n", this.m_fan);
			LogRecord(null, str);
		}
		if (CardManager.isyizhou) {
			m_wanfa_yz = new ArrayList<eWanFaType_yz>();
			m_roundyz = eRoundType_yz.values()[round - 1];
			m_fan = eFanType.values()[fan - 1];
			m_jifen = eSuanFenType.values()[jifen - 1];

			for (int i = 0; i < list2.size(); ++i) {
				int c = list2.get(i);
				if (c > 0) {
					m_wanfa_yz.add(eWanFaType_yz.values()[i]);
				}
			}

			for (int i = 0; i < list.size(); ++i) {
				int c = list.get(i);
				if (c > 0) {
					m_wanfa_yz.add(eWanFaType_yz.values()[i + list2.size()]);
				}
			}

			//
			// SHANGXIAHUI, // 上下会儿;
			// HUBAOZHANG,
			// TUIDAOHU,
			// KOUPAI,
			// DAJIANG,
			// HAIDILAOYUE,
			// GANGSHANGKAIHUA
			String str = null;
			str += String.format("\n能否上下会儿:%s\n", this.hasWanFaYZ(eWanFaType_yz.SHANGXIAHUI));
			str += String.format("能否胡八张:%s\n", this.hasWanFaYZ(eWanFaType_yz.HUBAOZHANG));
			str += String.format("能否推到胡:%s\n", this.hasWanFaYZ(eWanFaType_yz.TUIDAOHU));
			str += String.format("能否扣牌:%s\n", this.hasWanFaYZ(eWanFaType_yz.KOUPAI));
			str += String.format("能否大将:%s\n", this.hasWanFaYZ(eWanFaType_yz.DAJIANG));
			str += String.format("能否海底捞月:%s\n", this.hasWanFaYZ(eWanFaType_yz.HAIDILAOYUE));
			str += String.format("能否杠上开花:%s\n", this.hasWanFaYZ(eWanFaType_yz.GANGSHANGKAIHUA));
			str += String.format("四杠黄庄:%s\n", this.hasWanFaYZ(eWanFaType_yz.SIGANGHUANGZHUABG));
			str += String.format("最大圈数:%s\n", this.m_roundyz);
			str += String.format("最大翻数:%s\n", this.m_fan);
			str += String.format("算分规则:%s\n", this.m_jifen);
			LogRecord(null, str);
		}

		if (CardManager.isjinzhou) {
			// 如果是葫芦岛的玩法;
			m_wanfa_jz = new ArrayList<eWanFaType_jz>();
			m_round = eRoundType.values()[round - 1];
			m_fan = eFanType.values()[fan - 1];
			m_jifen = eSuanFenType.values()[jifen - 1];
			m_wanfa1 = wanfa;

			// m_wanfa_jz.add(eWanFaType_jz.values()[m_wanfa1 - 1]);

			for (int i = 0; i < list2.size(); ++i) {
				int c = list2.get(i);
				if (c > 0) {
					m_wanfa_jz.add(eWanFaType_jz.values()[i]);
				}
			}

			String str = null;
			str += String.format("\n能否上翻会儿:%s\n", this.hasWanFaJZ(eWanFaType_jz.SHANGFANHUI));
			str += String.format("天地胡:%s\n", this.hasWanFaJZ(eWanFaType_jz.TIANDIHUI));
			str += String.format("七小对:%s\n", this.hasWanFaJZ(eWanFaType_jz.QIXIAODUI));
			str += String.format("是否四家闭:%s\n", this.hasWanFaJZ(eWanFaType_jz.SIJIABI));
			str += String.format("比对比:%s\n", this.hasWanFaJZ(eWanFaType_jz.BIDUIBI));
			str += String.format("是不是封顶:%s\n", this.hasWanFaJZ(eWanFaType_jz.FENGDING));

			str += String.format("最大圈数:%s\n", this.m_round);
			str += String.format("最大翻数:%s\n", this.m_fan);
		}
		
		if (CardManager.ischaoyang) {
			// 如果是朝阳的玩法
			m_wanfa_cy = new ArrayList<eWanFaType_cy>();
			m_roundcy = eRoundType_cy.values()[round -1];
			m_fan = eFanType.values()[fan - 1];
			m_jifen = eSuanFenType.values()[jifen - 1];
			m_wanfa1 = wanfa;

			
			for (int i = 0; i < list2.size(); ++i) {
				int c = list2.get(i);
				if (c > 0) {
					m_wanfa_cy.add(eWanFaType_cy.values()[i]);
				}
			}

			String str = null;
			str += String.format("\nzuoche:%s\n", this.hasWanFaCY(eWanFaType_cy.zuoche));
			str += String.format("baopai:%s\n", this.hasWanFaCY(eWanFaType_cy.baopai));
			str += String.format("sijiabi:%s\n", this.hasWanFaCY(eWanFaType_cy.sijiabi));
			str += String.format("sanjiabi:%s\n", this.hasWanFaCY(eWanFaType_cy.sanjiabi));
			str += String.format("mingpiaosuanbimen:%s\n", this.hasWanFaCY(eWanFaType_cy.mingpiaosuanbimen));
			str += String.format("lihu:%s\n", this.hasWanFaCY(eWanFaType_cy.lihu));
			str += String.format("jiahu:%s\n", this.hasWanFaCY(eWanFaType_cy.jiahu));
			str += String.format("yijiapao:%s\n", this.hasWanFaCY(eWanFaType_cy.yijiapao));
			str += String.format("qingyise:%s\n", this.hasWanFaCY(eWanFaType_cy.qingyise));
			str += String.format("baohougang:%s\n", this.hasWanFaCY(eWanFaType_cy.baohougang));

			str += String.format("最大圈数:%s\n", this.m_roundcy);
			str += String.format("最大翻数:%s\n", this.m_fan);
			LogRecord(null, str);
		}

	}

	public void packData(SendMsgBuffer buffer) {

		if (CardManager.isbaoding) {
			buffer.Add(m_round.ID());
			buffer.Add(m_fan.ID());
			buffer.Add(this.m_wanfa1);
			if (this.m_wanfa.size() == 1) {
				buffer.Add((short) 1);
				buffer.Add(0);
			} else {
				buffer.Add((short) (this.m_wanfa.size() - 1));
				for (int i = 1; i < this.m_wanfa.size(); ++i) {
					buffer.Add(this.m_wanfa.get(i).ID());
				}
			}

			buffer.Add(m_jifen.ID());
			Iterator<Integer> it = this.m_base_wanfa2.iterator();
			buffer.Add((short) this.m_base_wanfa2.size());
			while (it.hasNext()) {
				buffer.Add(it.next());
			}
		}
		if (CardManager.ishuludao) {
			buffer.Add(m_round.ID());
			buffer.Add(m_fan.ID());
			buffer.Add(this.m_wanfa1);
			Iterator<Integer> it = this.m_base_wanfa1.iterator();
			buffer.Add((short) this.m_base_wanfa1.size());
			while (it.hasNext()) {
				buffer.Add(it.next());
			}

			buffer.Add(m_jifen.ID());
			it = this.m_base_wanfa2.iterator();
			buffer.Add((short) this.m_base_wanfa2.size());
			while (it.hasNext()) {
				buffer.Add(it.next());
			}
		}
		if (CardManager.isyizhou) {
			buffer.Add(this.m_roundyz.ID());
			buffer.Add(m_fan.ID());
			buffer.Add(this.m_wanfa1);
			Iterator<Integer> it = this.m_base_wanfa1.iterator();
			buffer.Add((short) this.m_base_wanfa1.size());
			while (it.hasNext()) {
				buffer.Add(it.next());
			}

			buffer.Add(m_jifen.ID());
			it = this.m_base_wanfa2.iterator();
			buffer.Add((short) this.m_base_wanfa2.size());
			while (it.hasNext()) {
				buffer.Add(it.next());
			}
		}

		if (CardManager.isshanxi) {
			buffer.Add(m_round.ID());
			buffer.Add(m_fan.ID());
			buffer.Add(this.m_wanfa1);
			Iterator<Integer> it = this.m_base_wanfa1.iterator();
			buffer.Add((short) this.m_base_wanfa1.size());
			while (it.hasNext()) {
				buffer.Add(it.next());
			}

			buffer.Add(m_jifen.ID());
			it = this.m_base_wanfa2.iterator();
			buffer.Add((short) this.m_base_wanfa2.size());
			while (it.hasNext()) {
				buffer.Add(it.next());
			}
		}
		if (CardManager.isjinzhou) {
			buffer.Add(m_round.ID());
			buffer.Add(m_fan.ID());
			buffer.Add(this.m_wanfa1);
			Iterator<Integer> it = this.m_base_wanfa1.iterator();
			buffer.Add((short) this.m_base_wanfa1.size());
			while (it.hasNext()) {
				buffer.Add(it.next());
			}

			buffer.Add(m_jifen.ID());
			it = this.m_base_wanfa2.iterator();
			buffer.Add((short) this.m_base_wanfa2.size());
			while (it.hasNext()) {
				buffer.Add(it.next());
			}
		}
		if (CardManager.ischaoyang) {
			buffer.Add(m_roundcy.ID());
			buffer.Add(m_fan.ID());
			buffer.Add(this.m_wanfa1);
			Iterator<Integer> it = this.m_base_wanfa1.iterator();
			buffer.Add((short) this.m_base_wanfa1.size());
			while (it.hasNext()) {
				buffer.Add(it.next());
			}

			buffer.Add(m_jifen.ID());
			it = this.m_base_wanfa2.iterator();
			buffer.Add((short) this.m_base_wanfa2.size());
			while (it.hasNext()) {
				buffer.Add(it.next());
			}
		}
	}

	// 获取几回合;
	public int getRound() {
		if (CardManager.isyizhou) {
			return m_roundyz.ID();
		}
		if(CardManager.ischaoyang){
			return m_roundcy.Cost();
		}
		return m_round.ID();
	}

	public int getCostKa() {
		// todo not well to be modify;
		if (CardManager.isyizhou) {
			switch (this.m_roundyz.ID()) {
			case 1:
				return 1;
			case 2:
				return 2;
			case 4:
				return 3;
			}
		}
		if(CardManager.ischaoyang){
			return this.m_roundcy.Cost();
		}
		
		switch (this.m_round.ID()) {
		case 1:
			return 1;
		case 2:
			return 3;
		case 3:
			return 5;
		}
		return 2;
	}

	// 获取最大的番薯;
	public int getMaxFan() {
		int fan = 40;
		switch (m_fan.ID()) {
		case 1:
			fan = 40;
		case 2:
			if (CardManager.ishuludao || CardManager.isyizhou)
				fan = 60;
			else
				fan = 80;
		case 3:
			fan = 120;
		case 4:
			fan = 999999;
		}
		return fan;
	}

	public eSuanFenType getSuanFen() {
		return m_jifen;
	}

	public boolean hasHdly() {
		return this.m_hdly == 1;
	}

	public boolean hasGskh() {
		if(CardManager.ishuludao){
			return true; //葫芦岛是默认的
		}
		return this.m_gskh == 1;
	}

	// todo;

	public boolean hasWanFa(eWanFaType type) {
		Iterator<eWanFaType> it = this.m_wanfa.iterator();
		while (it.hasNext()) {
			if (it.next() == type) {
				return true;
			}
		}
		return false;
	}

	public boolean hasWanFaHLD(eWanFaType_hld type) {
		Iterator<eWanFaType_hld> it = this.m_wanfa_hld.iterator();
		while (it.hasNext()) {
			if (it.next() == type) {
				return true;
			}
		}
		return false;
	}

	public boolean hasWanFaSX(eWanFaType_sx type) {
		Iterator<eWanFaType_sx> it = this.m_wanfa_sx.iterator();
		while (it.hasNext()) {
			if (it.next() == type) {
				return true;
			}
		}
		return false;
	}

	public boolean hasWanFaJZ(eWanFaType_jz type) {
		Iterator<eWanFaType_jz> it = this.m_wanfa_jz.iterator();
		while (it.hasNext()) {
			if (it.next() == type) {
				return true;
			}
		}
		return false;
	}

	public boolean hasWanFaYZ(eWanFaType_yz type) {
		Iterator<eWanFaType_yz> it = this.m_wanfa_yz.iterator();
		while (it.hasNext()) {
			if (it.next() == type) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasWanFaCY(eWanFaType_cy type) {
		Iterator<eWanFaType_cy> it = this.m_wanfa_cy.iterator();
		while (it.hasNext()) {
			if (it.next() == type) {
				return true;
			}
		}
		return false;
	}

	private void LogRecord(MyUser user, String record) {
		if (null != user) {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, user.GetRoleGID(), record);
		} else {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, 0l, record);
		}
	}

}
