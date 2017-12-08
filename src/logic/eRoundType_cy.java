package logic;

public enum eRoundType_cy {
	Round4,
	Round8;
	public int ID(){
		switch(this){
		case Round4:return 4;
		case Round8:return 8;
		}
		return 0;
	} 
	public int Cost(){
		switch(this){
		case Round4:return 1;
		case Round8:return 2;
		}
		return 2;
	}
	//
	
}
