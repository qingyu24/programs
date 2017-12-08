package manager;

import logic.module.room.RoomUser;
import logic.userdata.CYDeskBalance;
import logic.userdata.DeskBalance;
import logic.userdata.HLDDeskBalance;
import logic.userdata.IDeskBalance;
import logic.userdata.JZDeskBalance;
import logic.userdata.XADeskBalance;
import logic.userdata.YZDeskBalance;

public class BalanceFactory {
	private static BalanceFactory _instance;
	
	public static BalanceFactory getInstance(){
		if(_instance != null){
			return _instance;
		}
		
		return _instance = new BalanceFactory();
	}
	
	public IDeskBalance createBalance(RoomUser user){
		//todo 暂时用int类型吧；
		if(CardManager.isbaoding){
			return new DeskBalance(user);
		}else if(CardManager.ishuludao){
			return new HLDDeskBalance(user);
		}/*else if(CardManager.isjinzhou){
			return new HLDDeskBalance(user);
		}*/else if(CardManager.isjinzhou){
			return new JZDeskBalance(user);
		}else if(CardManager.ischaoyang){
			return new CYDeskBalance(user);
		}else if(CardManager.isshanxi){
			return new XADeskBalance(user);
		}else if(CardManager.isyizhou){
			return new YZDeskBalance(user);
		}
		return null;
	}
}
