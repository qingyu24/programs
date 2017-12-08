package logic;

public enum eRoundType_yz {
	Round1,
	Round2,
	Round4;
	public int ID(){
		switch(this){
		case Round1:return 1;
		case Round2:return 2;
		case Round4:return 4;
		}
		return 0;
	} 
}
