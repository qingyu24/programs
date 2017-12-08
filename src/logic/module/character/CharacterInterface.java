package logic.module.character;

import core.remote.PU;
import core.remote.RCC;
import core.remote.RFC;
import logic.MyUser;
import logic.Reg;

@RCC (ID = Reg.CHARACTER)
public interface CharacterInterface
{
    static final int MID_REQUESTBASEINFO = 0;	///< 获取属性
    
    @RFC (ID = MID_REQUESTBASEINFO)
    void RequestBaseInfo(@PU (Index = Reg.CHARACTER) MyUser p_user);
}
