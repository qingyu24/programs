package logic;

public enum eErrorType {
	Network_Error,
	Logic_Error;//；
	
	public int ID(){
		switch(this){
		case Network_Error:return 1;
		case Logic_Error:return 2;
		}
		return 0;
	}
}