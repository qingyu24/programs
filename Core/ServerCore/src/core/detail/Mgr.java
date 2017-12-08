/**
 * Mgr.java 2012-6-11下午10:56:16
 */
package core.detail;

import core.detail.impl.mgr.ManagerMgr;
import core.detail.impl.mgr.methodmgr.MethodType;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Mgr
{
	public static ManagerMgr Get()
	{
		return ManagerMgr.GetInstance();
	}
	
	public static MethodMgr GetRunMethodMgr(MethodType type)
	{
		return type.GetMethodMgr();
	}

	public static LogicMgr GetLogicMgr()
	{
		return Get().GetLogicMgr();
	}

	public static SqlMgr GetSqlMgr()
	{
		return Get().GetSQLMgr();
	}
	
	public static UserMgr GetUserMgr()
	{
		return Get().GetUserMgr();
	}
	
	public static TimerMgr GetTimerMgr()
	{
		return Get().GetTimerMgr();
	}
	
	public static ThreadMgr GetThreadMgr()
	{
		return Get().GetThreadMgr();
	}
	
	public static SQLLogMgr GetSQLLogMgr()
	{
		return Get().GetSQLLogMgr();
	}
	
	public static NetMgr GetNetMgr()
	{
		return Get().GetNetMgr();
	}
}
