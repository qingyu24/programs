/**
 * eLogicSQLLogType.java 2012-10-25上午10:14:46
 */
package logic.module.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import core.LogType;
import core.SQLBuild;
import logic.module.log.sql.CreateRoleLog;
import logic.module.log.sql.LoginLog;
import logic.module.log.sql.LogoutLog;
import logic.module.log.sql.OperationLog;
import logic.module.log.sql.RegisterLog;
import logic.module.log.sql.roomcardlog;
import logic.module.log.sql.roominfolog;
import utility.Debug;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eLogicSQLLogType implements LogType
{
	LOGIC_SQL_REGISTER,
	LOGIC_SQL_LOGIN,
	LOGIC_SQL_CREATE_ROLE,
	LOGIC_GM,
	LOGIC_SQL_LOGOUT,					//登出日志		>done
	LOGIC_SQL_OPERATION,                   //操作日志
	LOGIC_SQL_ROOMINFO,
	LOGIC_SQL_ROOMCARD,
	
	;
	/* (non-Javadoc)
	 * @see core.LogType#Serialize()
	 */
	@Override
	public String Serialize()
	{
		switch (this)
		{
			case LOGIC_SQL_CREATE_ROLE:		return SQLBuild.GetInstance().GetSQL(new CreateRoleLog());
			case LOGIC_SQL_LOGIN:			return SQLBuild.GetInstance().GetSQL(new LoginLog());
			case LOGIC_SQL_LOGOUT:			return SQLBuild.GetInstance().GetSQL(new LogoutLog());
			case LOGIC_SQL_REGISTER:		return SQLBuild.GetInstance().GetSQL(new RegisterLog());
			case LOGIC_SQL_OPERATION:       return SQLBuild.GetInstance().GetSQL(new OperationLog());
			case LOGIC_SQL_ROOMINFO:       return SQLBuild.GetInstance().GetSQL(new roominfolog());
			case LOGIC_SQL_ROOMCARD:       return SQLBuild.GetInstance().GetSQL(new roomcardlog());
			
			default: Debug.Assert(false , "未执行的类型:" + this);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see core.LogType#SystemLog()
	 */
	@Override
	public boolean SystemLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#LogicLog()
	 */
	@Override
	public boolean LogicLog()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see core.LogType#SQLLog()
	 */
	@Override
	public boolean SQLLog()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see core.LogType#DebugLog()
	 */
	@Override
	public boolean DebugLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#InfoLog()
	 */
	@Override
	public boolean InfoLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#WarningLog()
	 */
	@Override
	public boolean WarningLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#ErrorLog()
	 */
	@Override
	public boolean ErrorLog()
	{
		return false;
	}

	public static String GetCurrTime()
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return f.format(c.getTime());
	}
}
