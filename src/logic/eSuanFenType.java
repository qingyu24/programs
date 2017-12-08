package logic;

public enum eSuanFenType {
	XIAOBAO,
	DABAO,
	SANJIACH;
	public int ID(){
		switch(this){
		case XIAOBAO:return 1;
		case DABAO:return 2;
		case SANJIACH:return 3;
		}
		return 0;
	} 
}
