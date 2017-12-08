/**
 * eLoginErrorCode.java 2013-2-26上午11:35:02
 */
package logic.module.login;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eLoginErrorCode
{
	USERNAMM_EMPTY,
	USERNAME_LONG,
	USERNAME_SAME,
	USERNAME_NOTEXIST,
	USERNAME_ALREADYEXIST,
	
	UNKNOW,
	
	LOGIN_FORBID,
	
	OPPO_KILLED,		///<相同账号登陆,被踢下

	PASSWORD_ERR,
	SYSTEM_TIRED,
	SYSTEM_FORBID,
	
	USER_FULL,
	LINK_FULL,
	
	LOGIN_QUEUE_FULL,
	
	PASSWORD_OK,
	LOGIN_OK,
	REGISTER_OK,
	;
	
	public int ID()
	{
		switch (this)
		{
		case USERNAMM_EMPTY:	return -1;
		case USERNAME_LONG:		return -2;
		case USERNAME_SAME:		return -3;
		case USERNAME_NOTEXIST: return -4;
		case USERNAME_ALREADYEXIST: return -6;
		
		case UNKNOW:			return -5;
		
		case LOGIN_FORBID:		return -9;

		case SYSTEM_FORBID:		return -10;
		case OPPO_KILLED:		return -11;
		
		case PASSWORD_ERR:		return -12;
		case SYSTEM_TIRED:		return -13;
		
		case USER_FULL:			return -14;
		case LINK_FULL:			return -15;
		
		case LOGIN_QUEUE_FULL:	return -16;
		
		case PASSWORD_OK:		return 2;
		case LOGIN_OK:			return 1;
		case REGISTER_OK: 		return 3;
		}
		return -5;
	}
}
