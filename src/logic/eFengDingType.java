package logic;

public enum eFengDingType {
	_WU,
	_40,
	_80,
	_120;
	public int ID(){
		switch(this){
		case _WU:return 1;
		case _40:return 2;
		case _80:return 3;
		case _120:return 4;
		}
		return 0;
	} 
}
