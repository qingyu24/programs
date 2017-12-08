package logic;

import manager.CardManager;

public enum eRoundType {
	Round1,
	Round2,
	Round4;
	public int ID(){
		if(CardManager.isshanxi){
			switch(this){
			case Round1:return 1;
			case Round2:return 4;
			case Round4:return 8;
			}
		}else if(CardManager.ishuludao){
			switch(this){
			case Round1:return 4;
			case Round2:return 8;
			}
		}
		else if(CardManager.isjinzhou){
			switch(this){
			case Round1:return 4;
			case Round2:return 8;
			}
		}
		else {
			switch(this){
			case Round1:return 1;
			case Round2:return 5;
			case Round4:return 10;
			}
		}
		
		return 0;
	} 
}
