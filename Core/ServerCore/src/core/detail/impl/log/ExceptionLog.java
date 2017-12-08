/**
 * ExceptionLog.java 2013-1-28下午4:34:56
 */
package core.detail.impl.log;

import core.*;
import core.db.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ExceptionLog
{
	public DBInt			ServerID;
	public DBDateTime		Tm;
	public DBLong 			RoleID;
	public DBString			ExceptionName;
	public DBString			Detail;
	
	public static void Log(Throwable e, User u)
	{
		StringBuilder s = new StringBuilder();
		if ( e instanceof InvocationTargetException )
		{
			e = ((InvocationTargetException) e).getTargetException();
		}
		StackTraceElement[] ss = e.getStackTrace();
		for (StackTraceElement s1 : ss)
		{
			s.append(s1);
			s.append("\n");
		}
		
		long rid = u == null ? 0 : u.GetRoleGID();
		if (u.GetUserName() != null)
		{
			s.append("[");
			s.append(u.GetUserName());
			s.append("]");
		}
		Log.out.Log(eSystemSQLLogType.SYSTEM_EXCEPTION_RECORD,
				RootConfig.GetInstance().ServerUniqueID,
				eSystemSQLLogType.GetCurrTime(),
				rid,
				e.toString().replace("'", "_"),
				s
				);
	}
}
