package logic;

public enum eFanType {
	Fan5,
	Fan6,
	Fan7,
	Fan8;
	public int ID(){
		switch(this){
		case Fan5:return 1;
		case Fan6:return 2;
		case Fan7:return 3;
		case Fan8:return 4;
		}
		return 0;
	} 
}
