/**
 * eSystemSQLLogType.java 2013-1-28下午4:31:04
 */
package core.detail.impl.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import utility.*;
import core.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eSystemSQLLogType implements LogType
{
	SYSTEM_EXCEPTION_RECORD,
	SYSTEM_NET_INFO,
	SYSTEM_LOGIC_INFO,
	SYSTEM_SQL_INFO,
	SYSTEM_SQL_DETAIL_INFO,
	SYSTEM_SQL_USER_CLOSE,
	;
	
	/* (non-Javadoc)
	 * @see core.LogType#Serialize()
	 */
	@Override
	public String Serialize()
	{
		switch (this)
		{
		case SYSTEM_EXCEPTION_RECORD: 	return SQLBuild.GetInstance().GetSQL(new ExceptionLog());
		case SYSTEM_NET_INFO:			return SQLBuild.GetInstance().GetSQL(new _NetInfoLog());
		case SYSTEM_LOGIC_INFO:			return SQLBuild.GetInstance().GetSQL(new _LogicInfoLog());
		case SYSTEM_SQL_INFO:			return SQLBuild.GetInstance().GetSQL(new _SQLInfoLog());
		case SYSTEM_SQL_DETAIL_INFO:	return SQLBuild.GetInstance().GetSQL(new _SQLMethodDetailLog());
		case SYSTEM_SQL_USER_CLOSE:		return SQLBuild.GetInstance().GetSQL(new _CloseLog());
//		case SYSTEM_LOGIC_DETAIL_INFO:	return SQLBuild.GetInstance().GetSQL(new _LogicDetailInfoLog());
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
		return true;
	}

	/* (non-Javadoc)
	 * @see core.LogType#LogicLog()
	 */
	@Override
	public boolean LogicLog()
	{
		return false;
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
		c.setTimeInMillis(TimeMethod.currentTimeMillis());
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return f.format(c.getTime());
	}

}
