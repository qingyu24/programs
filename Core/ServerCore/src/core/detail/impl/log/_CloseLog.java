/**
 * _CloseLog.java 2013-3-14下午2:45:58
 */
package core.detail.impl.log;

import core.RootConfig;
import core.User;
import core.db.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class _CloseLog
{
	public DBInt		ServerID;
	public DBDateTime	Tm;
	public DBString		UserName;
	public DBLong		RoleID;
	public DBInt		HashCode;
	public DBInt		Reason;
	public DBInt		Ex;
	
	public static void Log(User p_User)
	{
		Log.out.Log(eSystemSQLLogType.SYSTEM_SQL_USER_CLOSE,
				RootConfig.GetInstance().ServerUniqueID,
				eSystemSQLLogType.GetCurrTime(),
				p_User.GetUserName() == null ? "" : p_User.GetUserName(),
				p_User.GetRoleGID(),
				p_User.hashCode(),
				p_User.GetCloseReason(),
				p_User.GetCloseReasonEx()
				);
	}
}
