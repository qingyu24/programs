package logic;

public enum eHuPaiType {
	HUANSANZ,
	YIJIUJIANGD,
	TIANDIHU;
	
	public int ID(){
		switch(this){
		case HUANSANZ:return 1;
		case YIJIUJIANGD:return 2;
		case TIANDIHU:return 3;
		}
		return 0;
	} 
}
