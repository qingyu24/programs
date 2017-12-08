/**
 * _LogicDetailInfoLog.java 2013-2-1下午4:24:59
 */
package core.detail.impl.log;

import core.db.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class _LogicDetailInfoLog
{
	public DBInt		ServerID;
	public DBDateTime	Tm;
	public DBLong		UseTime;
	public DBString		Detail;
	
	public static void LogicLog(long usetime, int linknum, int usernum, String s)
	{
		if ( usetime == 0 )
		{
			return;
		}
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_DETAIL, "逻辑", usetime, linknum, usernum, s);
//		Log.out.Log(eSystemSQLLogType.SYSTEM_LOGIC_DETAIL_INFO,
//				RootConfig.GetInstance().ServerUniqueID,
//				eSystemSQLLogType.GetCurrTime(),
//				usetime,
//				s
//				);
	}
	
	public static void SQLLog(long usetime, int linknum, int usernum, String s)
	{
		if ( usetime == 0 )
		{
			return;
		}
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_DETAIL, "SQL执行", usetime, linknum, usernum, s);
//		Log.out.Log(eSystemSQLLogType.SYSTEM_LOGIC_DETAIL_INFO,
//				RootConfig.GetInstance().ServerUniqueID,
//				eSystemSQLLogType.GetCurrTime(),
//				usetime,
//				s
//				);
	}
}
