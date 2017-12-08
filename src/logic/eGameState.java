package logic;

public enum eGameState {
	GAME_START,
	GAME_READY,
	GAME_KOU,
	GAME_PLAYING,//玩儿；
	GAME_OVER,//结束;
	GAME_PAUSE,//暂停;
	GAME_WAIT, // 解散房间的阶段;
	GAME_WAIT_PENG, //等待碰牌;
	GAME_WAIT_GANG,//等待杠牌;
	GAME_WAIT_CHI, //等待吃牌；
	GAME_EMPTY_ROOM, 
	GAME_ALL_OVER;//游戏完全结束
	public int ID(){
		switch(this){
		case GAME_START:return 1;
		case GAME_READY:return 2;
		case GAME_PLAYING:return 3;
		case GAME_OVER:return 4;
		case GAME_PAUSE:return 5;
		case GAME_WAIT:return 6;
		case GAME_WAIT_PENG: return 7; //等待碰牌;
		case GAME_WAIT_GANG: return 8;//等待杠牌;
		case GAME_WAIT_CHI:return 9; //等待吃牌；
		case GAME_EMPTY_ROOM:return 10;
		case GAME_ALL_OVER:return 11;
		}
		return 0;
	}
}
