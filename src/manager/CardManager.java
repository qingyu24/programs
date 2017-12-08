package manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

import core.detail.impl.log.Log;
import logic.MyUser;
import logic.eYouState;
import logic.config.IConfig;
import logic.config.TestCardConfig;
import logic.config.handler.MahJongHandler;
import logic.module.log.eLogicDebugLogType;
import logic.module.room.CardInfo;
import logic.module.room.RoomUser;

public class CardManager {
	private static CardManager _instance;
	public ArrayList<Integer> temp = new ArrayList<Integer>();
	public static boolean canchi = true; // 能否吃牌;(葫芦岛版本需要打开,除益州之外都能吃)
	public static boolean isqidui = true; // 是否允许胡七对;
	public static boolean canhui = true; // 是否有会儿牌：（这个设置是根据游戏的规则变动的）
	public static boolean canpiao = false; // 是否能够飘;(益州的直接选择飘,葫芦岛不能飘)
	public static boolean qufengpai = false; // 去风牌;
	public static boolean siganghuangzhuang = false; // 四缸黄庄
	public static int MS_BASE_LASTCARD = 80; // 剩余牌数;(益州是剩余12张牌)
	public static boolean cankou = false; // 是否能够扣牌;(益州需要直接设置为true)
	public static boolean istingpai = false;
	public static boolean guo = false;
	// 是否听牌;
	public static boolean isTestyou = false;
	public static boolean isTestscore = false;
	public static boolean ishuiTest = false;
	public static boolean isTest = false; // 测试指定的牌型;

	public static boolean ishubazhang = false; // 是否允许胡八张;(这是通过规则来判断的)
	public static boolean ishuludao = false; // 是葫芦岛麻将
	public static boolean isbaoding = false;// 是保定麻将;
	public static boolean isyizhou = false; // 是益州麻将;
	public static boolean isjinzhou = false;// 是锦州麻将;
	public static boolean ischaoyang = false; // 是朝阳麻将;
	public static boolean isshanxi = true; // 是西安麻将;
	public static boolean ishdly = false; // 是否允许海底捞月；（葫芦岛和锦州是允许的）
	public static boolean ishuangz = false; // 荒庄也要给分;

	public static boolean isCheckHuCount = false; // 核算胡牌的数量

	private void init() {
		if (ishuludao) { // 葫芦岛;
			canchi = true; // 能否吃牌;(葫芦岛版本需要打开,除益州之外都能吃)
			isqidui = true; // 是否允许胡七对;
			canhui = true; // 是否有会儿牌：（这个设置是根据游戏的规则变动的）
			canpiao = false; // 是否能够飘;(益州的直接选择飘,葫芦岛不能飘)
			qufengpai = true; // 去风牌;
			MS_BASE_LASTCARD = 20; // 剩余牌数;(益州是剩余12张牌)
			cankou = false; // 是否能够扣牌;(益州需要直接设置为true)
			istingpai = false; // 是否听牌;
			ishdly = true; // 海底捞月;
			ishuangz = true; //
			isCheckHuCount = true;
		} else if (isshanxi) {
			canchi = false; // 能否吃牌;(葫芦岛版本需要打开,除益州之外都能吃)
			isqidui = false; // 是否允许胡七对;
			canhui = false; // 是否有会儿牌：（这个设置是根据游戏的规则变动的）
			canpiao = false; // 是否能够飘;(益州的直接选择飘,葫芦岛不能飘)
			qufengpai = false; // 去风牌;
			MS_BASE_LASTCARD = 20; // 剩余牌数;(益州是剩余12张牌)
			cankou = false; // 是否能够扣牌;(益州需要直接设置为true)
			istingpai = false; // 是否听牌;
		} else if (isbaoding) {
			canchi = true; // 能否吃牌;(葫芦岛版本需要打开,除益州之外都能吃)
			isqidui = true; // 是否允许胡七对;
			canhui = true; // 是否有会儿牌：（这个设置是根据游戏的规则变动的）
			canpiao = false; // 是否能够飘;(益州的直接选择飘,葫芦岛不能飘)
			qufengpai = false; // 去风牌;
			MS_BASE_LASTCARD = 12; // 剩余牌数;(益州是剩余12张牌)
			cankou = false; // 是否能够扣牌;(益州需要直接设置为true)
			istingpai = false; // 是否听牌;
		} else if (isyizhou) {
			canchi = false; // 能否吃牌;(葫芦岛版本需要打开,除益州之外都能吃)
			isqidui = true; // 是否允许胡七对;
			canhui = true; // 是否有会儿牌：（这个设置是根据游戏的规则变动的）
			canpiao = true; // 是否能够飘;(益州的直接选择飘,葫芦岛不能飘)
			qufengpai = false; // 去风牌;
			MS_BASE_LASTCARD = 12; // 剩余牌数;(益州是剩余12张牌)
			cankou = true; // 是否能够扣牌;(益州需要直接设置为true)
			istingpai = false; // 是否听牌;
			ishdly = false;
		} else if (isjinzhou) {
			// 测试指定的牌型;
			canchi = true; // 能否吃牌;(葫芦岛版本需要打开,除益州之外都能吃)
			isqidui = true; // 是否允许胡七对;
			canhui = true; // 是否有会儿牌：（这个设置是根据游戏的规则变动的）
			canpiao = false; // 是否能够飘;(益州的直接选择飘,葫芦岛不能飘)
			qufengpai = true; // 去风牌;
			MS_BASE_LASTCARD = 16; // 剩余牌数;(益州是剩余12张牌)
			cankou = false; // 是否能够扣牌;(益州需要直接设置为true)
			istingpai = false; // 是否听牌;
			ishdly = true; // 海底捞月;
			ishuangz = true; //
			isCheckHuCount = true;
		}

		else if (ischaoyang) {
			canchi = true; // 能否吃牌;(葫芦岛版本需要打开,除益州之外都能吃)
			isqidui = true; // 是否允许胡七对;
			canhui = false; // 是否有会儿牌：（这个设置是根据游戏的规则变动的）
			canpiao = false; // 是否能够飘;(益州的直接选择飘,葫芦岛不能飘)
			qufengpai = false; // 去风牌;
			MS_BASE_LASTCARD = 16; // 剩余牌数;(益州是剩余12张牌)
			cankou = false; // 是否能够扣牌;(益州需要直接设置为true)
			istingpai = false; // 是否听牌;
			isCheckHuCount = true;
		}

		if (!isTest) {
			isTestyou = false;
			isTestscore = false;
			ishuiTest = false;
		}
	}

	public CardManager() {
		this.init();
	}

	private ArrayList<CardInfo> _list = new ArrayList<CardInfo>();

	public static CardManager getInstance() {
		if (_instance != null) {
			return _instance;
		}

		return _instance = new CardManager();
	}

	public void testCard(ArrayList<Integer> list) {
		isTest = list.size() > 0;
		temp = list;
	}

	// 洗一把牌;
	public ArrayList<CardInfo> createCardGroup(int hui) {
		MahJongHandler handler = (MahJongHandler) ConfigManager.getInstance().getHandler(ConfigManager.MahJongConfig);
		ArrayList<CardInfo> list = handler.Random(hui);
		if (hui > 0 && hui < list.size()) {
			/* CardInfo info = list.remove(hui); */
			for (int i = 0; i < list.size(); i++) {
				CardInfo cardInfo = list.get(i);
				if (cardInfo.getTid() == hui) {
					list.remove(cardInfo);
					String fstr = String.format("删除掉了会儿皮:%s\n", cardInfo.getName());
					LogRecord(null, fstr);
					break;
				}
			}
		}
		// *************************************************
		// *************************************************
		// *************************************************
		// *************************************************
		// *************************************************
		/*
		 * temp.add(1); temp.add(2);
		 * 
		 * temp.add(5); temp.add(6);
		 * 
		 * temp.add(9); temp.add(10);
		 * 
		 * temp.add(13); temp.add(14);
		 * 
		 * temp.add(17); temp.add(18);
		 * 
		 * temp.add(21); temp.add(22); temp.add(25);
		 * 
		 * temp.add(50);
		 */
		if (isTest) {
			TestCardConfig mc = (TestCardConfig) ConfigManager.getInstance().getConfig("TestCardConfig");

			temp.add(mc.c1);
			temp.add(mc.c2);
			temp.add(mc.c3);
			temp.add(mc.c4);
			temp.add(mc.c5);
			temp.add(mc.c6);
			temp.add(mc.c7);
			temp.add(mc.c8);
			temp.add(mc.c9);
			temp.add(mc.c10);
			temp.add(mc.c11);
			temp.add(mc.c12);
			temp.add(mc.c13);
			temp.add(mc.c14);
	/*		temp.add(1);
			temp.add(2);
			temp.add(3);
			temp.add(5);
			temp.add(6);
			temp.add(7);
			temp.add(9);
			temp.add(10);
			temp.add(11);
			temp.add(13);
			temp.add(14);
			temp.add(15);
			temp.add(19);
			temp.add(20);*/
			
			

			// temp.add(102);

			/*
			 * Iterator<CardInfo> it = list.iterator();
			 * 
			 * while (it.hasNext()) { CardInfo ci = it.next(); for (int i = 0; i
			 * < temp.size(); ++i) { if (ci.getTid() == temp.get(i)) {
			 * it.remove();
			 * 
			 * } }
			 * 
			 * }
			 */

			ArrayList<CardInfo> addAll = new ArrayList<>();
			addAll.addAll(list);
			Iterator<CardInfo> it = addAll.iterator();

			while (it.hasNext()) {
				CardInfo ci = it.next();
				for (int i = 0; i < temp.size(); ++i) {
					if (ci.getTid() == temp.get(i)) {
						list.remove(ci);

					}
				}

			}
		}

		this.checkCardData(list);
		return list;
	}

	public void checkCardData(ArrayList<CardInfo> list) {
		for (int i = 0; i < list.size() - 1; ++i) {
			for (int j = i + 1; j < list.size(); ++j) {
				if (list.get(i).getTid() == list.get(j).getTid()) {
					assert (false);
					break;
				}
			}
		}
	}

	public boolean canEat() {

		return false;
	}

	// 是否是十三幺;
	public boolean isShiSanYao(ArrayList<CardInfo> list) {
		int[] ssy = { 1, 9, 10, 18, 19, 27, 28, 29, 30, 31, 32, 33, 34 };

		int totalFind = 0;
		for (int i = 0; i < ssy.length; ++i) {
			int findCount = 0;
			Iterator<CardInfo> it = list.iterator();
			while (it.hasNext()) {
				CardInfo card = it.next();
				if (card.getNumber() == ssy[i]) {
					findCount++;

				}
			}
			// System.err.printf("getnumber: - %d\n", findCount);
			if (findCount == 2) {
				totalFind++;
			}
			if (findCount > 2 || findCount < 1) {
				return false;
			}
		}
		return totalFind == 1;

	}

	public int isQiDui(ArrayList<CardInfo> list, int huiCount, boolean need) {
		String p = "";
		for (int i = 0; i < list.size(); ++i) {
			p += list.get(i).getName() + ",";
		}

		String fstr = String.format("七对判断 我的牌:%s 我的会儿牌:%d\n", p, huiCount);
		LogRecord(null, fstr);
		if (list.size() + huiCount < 12 && !need) {
			return 0;
		}
		int total = 0;
		boolean flag = true;
		_list.clear();
		for (int i = 0; i < list.size(); i++) {
			flag = true;
			for (int j = 0; j < _list.size(); j++) {
				if (_list.get(j).sameTo(list.get(i))) {
					flag = false;
					break;
				}
			}
			if (flag) {
				_list.add(list.get(i));
				Iterator<CardInfo> it = list.iterator();
				int count = 0;
				while (it.hasNext()) {
					CardInfo cur = it.next();
					if (cur.sameTo(list.get(i))) {
						count++;
					}
				}
				if (count == 4) {
					total++;
				} else if ((count == 1 || count == 3) && huiCount > 0) {
					huiCount--;
					if (count == 3) {
						total++;
					}
				} else if (count != 2) {
					return 0;
				}
			}
		}

		String str = String.format("七对判断：总数量:%d\n", total);
		LogRecord(null, fstr);

		return Math.max(1, total);
	}

	// 是否是清一色;
	public boolean isQingYiSe(ArrayList<CardInfo> list) {
		CardInfo temp = list.get(0);
		for (int i = 1; i < list.size(); ++i) {
			if (list.get(i).getType() > 3) {
				return false;
			}
			if (temp.getType() != list.get(i).getType() && (list.get(i).getType() <= 3 || list.get(i).getType() <= 3)) {
				return false;
			}
		}
		return true;
	}

	private void logCard(RoomUser r, ArrayList<CardInfo> list) {

		Iterator<CardInfo> it3 = list.iterator();
		String p = "判断一条龙";
		while (it3.hasNext()) {
			CardInfo card = it3.next();

			p += card.getName() + ",";
		}
		// TODO Auto-generated method stub
		LogRecord(r.getUser(), p);
	}

	// 是否是一条龙；(判断龙牌的前提是必须是胡牌是成立的)
	public boolean isLong(ArrayList<CardInfo> list, int huiCount, RoomUser user) {
		if (list.size() + huiCount < 10) {
			return false;
		}

		logCard(user, list);
		for (int i = 0; i < list.size() - 1; ++i) {
			int hui = huiCount;
			ArrayList<CardInfo> list2 = new ArrayList<CardInfo>();
			int k = list.get(i).getNumber() % 9;
			if (k - 1 <= hui) {
				if (k == 1) {
					list2.add(list.get(i));
				} else {
					for (int m = 0; m < k - 1; m++) {
						list2.add(null);
						hui--;
					}
					list2.add(list.get(i));
				}
			}

			for (int j = i + 1; j < list.size(); ++j) {
				if (list.get(i).getType() == list.get(j).getType() && !list2.contains(list.get(j))) {
					int beginId = ((int) list.get(i).getNumber() / 9) * 9 + 1;
					int c = list.get(j).getNumber() - beginId;
					;
					if (c == list2.size()) {
						list2.add(list.get(j));
					} else if (hui >= c - list2.size() && c > list2.size()) {
						int c1 = c - list2.size();
						for (int l = 0; l < c1; l++) {
							list2.add(null);
							hui--;
						}
						list2.add(list.get(j));

					}
				} else if (list2.size() != 9 && list2.size() + hui >= 9 && !list2.contains(list.get(j))) {
					while (list2.size() < 9 && hui > 0) {

						list2.add(null);
						hui--;
					}

				}
				
				if(j==list.size()-1&&list2.size()+hui>=9){
					while (list2.size() < 9 && hui > 0) {

						list2.add(null);
						hui--;
					}
					
				}

				if (list2.size() == 9) {
					ArrayList<CardInfo> arrayList = new ArrayList<>();
					arrayList.addAll(list);
					for (CardInfo cardInfo : list2) {
						if (cardInfo != null) {
							arrayList.remove(cardInfo);
						}
					}

					if (arrayList.size() + hui == 5) {
						boolean checkHuCard = checkhu(arrayList, hui);
						if (checkHuCard) {
							return true;
						}
					} else if (arrayList.size() + hui == 4
							|| arrayList.size() + hui == 1 && user.getYou() == eYouState.NOTICE_YOU) {
						// 悠牌一条龙
						if (hui > 0) {
							hui--;
							return checkKe(arrayList, hui);
						}

					} else if (arrayList.size() + hui == 2) {

						if (hui > 0) {
							return true;
						} else {
							LogRecord(user.getUser(), "一条龙判断到这一步两张牌分别" + arrayList.get(0) + arrayList.get(1));
							if (arrayList.get(0).getType() == arrayList.get(1).getType()
									&& arrayList.get(0).getNumber() == arrayList.get(1).getNumber()) {
								return true;
							}
						}
					}
				}

			}
			// 如果花色不一样了，说明对这个类型的牌的判断结束了，需要看他还需要几张牌老凑成龙;
			/*
			 * if(9 - list2.size() <= hui){ return true; }
			 */

		}

		return false;
	}

	public boolean checkhu(List<CardInfo> list, int huiCount) {
		for (int i = 0; i < list.size(); i++) {
			if (huiCount > 0) {
				ArrayList<CardInfo> list2 = new ArrayList<>();
				huiCount--;
				list2.remove(list.get(i));
				if (this.checkKe(list2, huiCount)) {
					return true;
				}
			}
			for (int j = i + 1; j < list.size(); j++) {
				ArrayList<CardInfo> list2 = new ArrayList<>();
				list2.addAll(list);
				if (list.get(i).getNumber() == list.get(j).getNumber()) {
					list2.remove(list.get(i));
					list2.remove(list.get(j));
					if (this.checkKe(list2, huiCount)) {
						return true;
					}
				}
			}
		}
		return false;

	}

	private boolean checkKe(List<CardInfo> list, int huiCount) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if (list.size() == 0) {
			flag = true;
		} else if (list.size() < 3) {
			// 用两张会儿牌来合成一组牌;
			if (list.size() == 1 && huiCount > 1) {
				huiCount -= 2;
				flag = true;
			} else if (list.size() == 2 && huiCount > 0) {
				if (list.get(0).getType() == list.get(1).getType()
						&& Math.abs(list.get(0).getNumber() - list.get(1).getNumber()) <= 2) {
					flag = true;
					huiCount--;
				}
			}

		}
		if (list.size() == 3) {

			if (list.get(1).getType() == list.get(0).getType() && list.get(2).getType() == list.get(0).getType()) {
				ArrayList<CardInfo> list2 = new RoomUser(new MyUser(), "").sortCard((ArrayList<CardInfo>) list);
				int i = list2.get(0).getNumber();
				int j = list2.get(1).getNumber();
				int k = list2.get(2).getNumber();
				if (k - j == 1 | k - j == 0 && i + k == 2 * j) {
					return true;
				}
			}
		}
		return flag;
	}

	private void LogRecord(MyUser user, String record) {
		if (null != user) {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, user.GetRoleGID(), record);
		} else {
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, 0l, record);
		}
	}

	// 随机的产生会儿牌;
	public int randomHuiCard() {
		if (ishuiTest) {
			return 103;
		}
		if (qufengpai) {
			return (int) (Math.random() * 108) + 1;
		} else {
		}
		MahJongHandler handler = (MahJongHandler) ConfigManager.getInstance().getHandler(ConfigManager.MahJongConfig);
		return (int) (Math.random() * handler.getSize()) + 1;
		// return 116;
	}

}