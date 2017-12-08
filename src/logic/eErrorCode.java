package logic;

public enum eErrorCode {
	Error_1, //；房卡不足;
	Error_2, // 进入的房间不存在;
	Error_3, // 房间的人数满了;
	Error_4, //摸牌失败;
	Error_5, //出牌失败;
	Error_6;
	public int ID(){
		switch(this){
		case Error_1:return 1;
		case Error_2:return 2;
		case Error_3:return 3;
		case Error_4:return 4;
		case Error_5:return 5;
		case Error_6:return 6;
		}
		return 0;
	}
}