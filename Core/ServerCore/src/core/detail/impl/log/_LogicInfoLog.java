/**
 * _LogicInfoLog.java 2013-2-1下午3:08:15
 */
package core.detail.impl.log;

import core.RootConfig;
import core.db.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class _LogicInfoLog
{
	public DBInt		ServerID;
	public DBDateTime	Tm;
	public DBLong		ProcessLogicNum;
	public DBLong		ProcessLogicTotalNum;
	public DBInt		SQLListNum;
	public DBInt		LogicListNum;
	public DBLong		UpdateMaxTime;
	
	public static void Log(long processlogicnum, long processtotalnum, int sqllistnum, int listnum, long updatetime)
	{
		if ( processlogicnum == 0 )
		{
			return;
		}
		Log.out.Log(eSystemSQLLogType.SYSTEM_LOGIC_INFO,
				RootConfig.GetInstance().ServerUniqueID,
				eSystemSQLLogType.GetCurrTime(),
				processlogicnum,
				processtotalnum,
				sqllistnum,
				listnum,
				updatetime
				);
	}
}
