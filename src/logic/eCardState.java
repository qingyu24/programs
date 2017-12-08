package logic;

public enum eCardState {
	PENG,
	GANG,
	HU,
	CHI,
	NONE,
	ZIMO,
	ANGANG,
	YOU,
	TING;
	public int ID(){
		switch(this){
		case PENG:return 3;
		case GANG:return 5;
		case HU:return 7;
		case CHI:return 1;
		case NONE:return 4;
		case ZIMO:return 9;
		case ANGANG:return 10;
		case YOU:return 11;
		case TING:return 12;
		}
		return 0;
	}
}
