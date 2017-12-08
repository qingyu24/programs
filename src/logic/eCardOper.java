package logic;

public class eCardOper {
	public static final int MID_SENDEAT = 1; //吃牌：
	//long roleid, int type, int tid1, int tid2, int tid3;
	public static final int MID_BROADCAST_EADCARD = 2; //广播吃牌：
    
	public  static final int MID_SENDPENG = 3; //碰牌：
	//long roleid, int tid1, int tid2, int tid3;
	public static final int MID_BROADCAST_PENGCARD = 4; //广播碰牌：
    
	public static final int MID_SENDGANG = 5; //杠牌：
	//long roleid, int tid1, int tid2, int tid3, int tid4; int type;(0,1)
	public static final int MID_BROADCAST_GANGCARD = 6; //广播杠牌：
    
	public static final int MID_SENDHU = 7; //胡牌：
	//long roleid, int tid;
	public static final int MID_BROADCAST_HUCARD = 8; //广播胡牌：
    
	public static final int MID_SENDCARD = 9; //出牌;
	//long roleid, int tid;
	public static final int MID_BROADCAST_SENDCARD = 10; // 广播出牌；
	
	public static final int MID_GET_CARD = 11; //摸牌;
	
	public static final int MID_SENDTING = 12; // 听牌;
	
	public static final int MID_BORADCAST_TINGCARD = 13; //广播听牌;
	
	//public static final int MID_LIUJU = 14; // 流局；
	
	public static final int MID_ANGANG = 15; // 暗杠；
	
	public static final int MID_BROADCAST_ANGANG = 16; // 广播暗杠；
	
	public static final int MID_GUO_CARD = 17; // 过牌;
	
	public static final int MID_TINGPAI = 18; //听牌;
	
	public static final int MID_BROADCAST_TING = 19; //广播听牌;
	
	public static final int MID_ZIMO = 20;  //自摸胡牌;
	
	
}
