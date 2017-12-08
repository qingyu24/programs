package test.robot.module.chat;

import test.robot.Robot;
import logic.Reg;
import core.remote.PS;
import core.remote.PST;
import core.remote.PU;
import core.remote.RCC;
import core.remote.RFC;

@RCC (ID = Reg.CHAT)
public interface ChatRtnInterface 
{
	final int MID_ChatRES = 0;
	
	@RFC ( ID = MID_ChatRES)
	public void ChatRes(@PU Robot r, @PST short channel,@PS String res);

}
