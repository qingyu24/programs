/**
 * LoginInterface.java 2012-6-11下午10:31:35
 */
package logic.module.login;

import core.remote.PI;
import core.remote.PS;
import core.remote.PU;
import core.remote.PUUN;
import core.remote.RCC;
import core.remote.RFC;
import core.remote.TestRFC;
import logic.MyUser;
import logic.Reg;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */

@RCC (ID = Reg.LOGIN)
public interface LoginInterface
{
	static final int MID_ENTER = 0;
	static final int MID_Create = 1;
	static final int MID_Register = 2;
	static final int MID_RESET_ENTER = 3;
	static final int MID_THIRDLOGIN = 4;
	
	@RFC (ID = MID_ENTER, RunDirect = true)
	void Enter(@PU MyUser p_user,
			   @PS String p_username,
			   @PS String p_password,
			   @PI int	  p_nServerID,
			   @PS String p_deviceIdentifier,
			   @PS String p_deviceModel);
	
		
	@RFC (ID = MID_Register, RunDirect = true)
	void Register(@PU MyUser p_user,
			   @PS String p_username,
			   @PS String p_password,
			   @PI int	  p_nServerID,
			   @PS String p_deviceIdentifier,
			   @PS String p_deviceModel);
	
	@RFC (ID = MID_THIRDLOGIN, RunDirect = true)
	void ThirdLogin(@PU MyUser p_user,
			   @PS String p_username,
			   @PS String p_nickname,
			   @PS String p_password,
			   @PI int	  p_nServerID,
			   @PS String p_headurl,
			   @PS String p_sex,
			   @PS String p_deviceIdentifier,
			   @PS String p_deviceModel);
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	//测试用的接口
	/////////////////////////////////////////////////////////////////////////////////////
	
	static final int MID_ECHORECEIVE = 55;
	static final int MID_ECHOSEND = 56;
	static final int MID_ECHO = 57;
	static final int MID_RUNEMPTYSQL = 58;
	static final int MID_READSQL = 59;
	static final int MID_SAVESQL = 60;
	static final int MID_LOG = 61;
	static final int MID_SQLLOG = 62;
	static final int MID_LOADBYUSERNAME = 63;
	static final int MID_CREATEWEBROLE = 64;
	static final int MID_BINARY = 65;
	
	@TestRFC (ID = MID_ECHORECEIVE , RunDirect = true)
	void EchoReceive(@PU MyUser p_user, @PI int p_nHashcode, @PS String p_sInfo);
	
	@TestRFC (ID = MID_ECHOSEND , RunDirect = true)
	void EchoSend(@PU MyUser p_user, @PI int p_nHashcode, @PI int p_nLength);
	
	@TestRFC (ID = MID_ECHO , RunDirect = true)
	void Echo(@PU MyUser p_user, @PI int p_nHashcode, @PS String p_sInfo);
	
	@TestRFC (ID = MID_RUNEMPTYSQL , RunDirect = true)
	void RunEmptySQL(@PU MyUser p_user,@PI int p_nHashcode, @PI int p_nNum);
	
	@TestRFC (ID = MID_LOG , RunDirect = true)
	void Log(@PU MyUser p_user, @PS String p_sLog);
	
	@TestRFC (ID = MID_SQLLOG , RunDirect = true)
	void SQLLog(@PU MyUser p_user);
	
	@TestRFC (ID = MID_LOADBYUSERNAME, RunDirect = false)
	void LoadByUserName(@PU MyUser p_user, @PUUN MyUser p_user1);
	
	@TestRFC (ID = MID_CREATEWEBROLE, RunDirect = true)
	void CreateWebRole(@PU MyUser p_user, 
			@PS String p_username, 
			@PS String p_password,
			@PI int p_nAdult,
			@PI int p_nServerID,
			@PS String p_RoleName,
			@PI int p_TemplateID);
	
	@TestRFC (ID = MID_BINARY, RunDirect = true)
	void Binary(@PU MyUser p_user, @PS String p_binary);
}
