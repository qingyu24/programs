package logic;

public enum eWanFaType {
	TUIDAOHU,
	HUBAZHANG,
	KOUPAI,
	DAJIANG;
	public int ID(){
		switch(this){
		case TUIDAOHU:return 1;
		case HUBAZHANG:return 2;
		case KOUPAI:return 1;
		case DAJIANG:return 2;
		}
		return 0;
	}
}
