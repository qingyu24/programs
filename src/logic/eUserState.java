package logic;

public enum eUserState {
	ENTER, // 进入房间;
	READY, //准备;
	WAIT, // 等待;
	MOPAI, // 摸牌;
	CHUPAI, // 出牌;
	NOTHING, // 不处理;
	HU; // 胡牌;
	public int ID(){
		switch(this){
		case ENTER:return 1;
		case READY:return 2;
		case WAIT:return 3;
		case MOPAI:return 4;
		case CHUPAI:return 5;
		case NOTHING:return 6;
		case HU:return 7;
		}
		return 0;
	}
	
}
