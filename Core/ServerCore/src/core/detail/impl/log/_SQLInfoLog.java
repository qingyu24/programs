/**
 * _SQLInfoLog.java 2013-2-1下午3:49:04
 */
package core.detail.impl.log;

import core.RootConfig;
import core.db.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class _SQLInfoLog
{
	public DBInt		ServerID;
	public DBDateTime	Tm;
	public DBLong		ProcessSQLNum;
	public DBLong		ProcessSQLTotalNum;
	public DBInt		ListNum;
	public DBLong		UpdateMaxTime;
	
	public static void Log(long processsqlnum, long processtotalnum, int listnum, long updatetime)
	{
		if ( processsqlnum == 0 )
		{
			return;
		}
		Log.out.Log(eSystemSQLLogType.SYSTEM_SQL_INFO,
				RootConfig.GetInstance().ServerUniqueID,
				eSystemSQLLogType.GetCurrTime(),
				processsqlnum,
				processtotalnum,
				listnum,
				updatetime
				);
	}
}
