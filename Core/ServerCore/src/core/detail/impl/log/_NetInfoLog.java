/**
 * _NetInfoLog.java 2013-2-1下午2:17:03
 */
package core.detail.impl.log;

import core.*;
import core.db.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class _NetInfoLog
{
	public DBInt		ServerID;
	public DBDateTime	Tm;
	public DBLong		Msg;
	public DBLong		MsgTotal;
	public DBLong		Buffer;
	public DBLong		BufferTotal;
	public DBInt		LinkNum;
	public DBInt		UserNum;
	
	public static void Log(long msgnum, long msgtotal, long buffer, long buffertotal, int linknum, int usernum)
	{
		if ( msgnum == 0 )
		{
			return;
		}
		Log.out.Log(eSystemSQLLogType.SYSTEM_NET_INFO,
				RootConfig.GetInstance().ServerUniqueID,
				eSystemSQLLogType.GetCurrTime(),
				msgnum,
				msgtotal,
				buffer,
				buffertotal,
				linknum,
				usernum
				);
	}
}
