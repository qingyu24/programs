/**
 * LoginRtnInterface.java 2012-7-12上午10:03:26
 */
package test.robot.module.login;

import logic.*;

import core.remote.*;
import test.robot.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
@RCC (ID = Reg.LOGIN)
public interface LoginRtnInterface
{
	final int MID_LOGINRES = 0;
	final int MID_NOROLE = 1;
	final int MID_ENTERWORLD = 2;
	final int MID_CREATEROLE = 4;
	
	static final int MID_ECHORECEIVE = 55;
	static final int MID_ECHOSEND = 56;
	static final int MID_ECHO = 57;
	static final int MID_RUNEMPTYSQL = 58;
	static final int MID_READSQL = 59;
	static final int MID_SAVESQL = 60;
	
	@RFC ( ID = MID_LOGINRES)
	void LoginRes(@PU Robot r, @PI int res);
	
	@RFC ( ID = MID_NOROLE )
	void NoRoleRes(@PU Robot r, @PL long roleid, @PI int roletemplateid);
	
	@RFC ( ID = MID_CREATEROLE)
	void CreateRoleRes(@PU Robot r, @PI int res);
	
	@RFC ( ID = MID_ENTERWORLD )
	void EnterWorld(@PU Robot r, @PL long roleid,@PI int roletemplateid, @PS String rolename);
	
	@RFC ( ID = MID_ECHORECEIVE)
	void EchoRecieveRes(@PU Robot r, @PI int hashcode);
	
	@RFC ( ID = MID_ECHOSEND)
	void EchoSendRes(@PU Robot r, @PI int hashcode, @PS String p_info);
	
	@RFC ( ID = MID_ECHO)
	void EchoRes(@PU Robot r, @PI int hashcode, @PS String p_info);
	
	@RFC ( ID = MID_RUNEMPTYSQL)
	void RunEmptySQLRes(@PU Robot r, @PI int hashcode);
	
	@RFC ( ID = MID_READSQL)
	void ReadSQLRes(@PU Robot r, @PI int hashcode);
	
	@RFC ( ID = MID_SAVESQL)
	void SaveSQLRes(@PU Robot r, @PI int hashcode);
}
