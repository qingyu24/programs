package logic;

public enum eYouState {
	NONE,
	CANYOU,
	NOTICE_YOU;
	public int ID(){
		switch(this){
		case NONE:return 0;
		case CANYOU:return 1;
		case NOTICE_YOU:return 2;
		}
		return 0;
	}
}
