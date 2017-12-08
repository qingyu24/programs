package logic.module.proxy;

import core.remote.PI;
import core.remote.PL;
import core.remote.PU;
import core.remote.RCC;
import core.remote.RFC;
import logic.MyUser;
import logic.Reg;



@RCC (ID = Reg.PROXY)
public interface ProxyInterface {
	static final int MID_GETALLUSER = 1;
	static final int MID_ADDSCORE = 2;
	static final int MID_GETUSER = 3;
	static final int MID_GET_CHARGE_RECORD = 4;
	static final int MID_UPDATE_SCORE = 5;
	
	@RFC (ID = MID_GETALLUSER)
    void GetAllUser(@PU (Index = Reg.ROOM) MyUser p_user, @PI int page);
    
    @RFC (ID = MID_ADDSCORE)
    void AddScore(@PU (Index = Reg.ROOM) MyUser p_user, @PL long roleId, @PI int score);
    
    @RFC (ID = MID_GETUSER)
    void GetUser(@PU (Index = Reg.ROOM) MyUser p_user, @PI int tid);
    
    @RFC (ID = MID_GET_CHARGE_RECORD)
    void GeChargeRecord(@PU (Index = Reg.ROOM) MyUser p_user, @PI int page);
}
