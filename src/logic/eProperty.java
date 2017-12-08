package logic;

public enum eProperty {
	ROOM_CARD;
	//其他的定义的；
	public int ID(){
		switch(this){
		case ROOM_CARD:return 1;
		}
		return 0;
	}

}
