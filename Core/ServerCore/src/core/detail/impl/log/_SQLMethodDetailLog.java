/**
 * _SQLMethodDetailLog.java 2013-2-1下午8:24:16
 */
package core.detail.impl.log;

import core.*;
import core.db.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
@SuppressWarnings("unused")
public class _SQLMethodDetailLog
{
	public DBInt		ServerID;
	public DBDateTime	Tm;
	public DBString		MethodName;
	public DBInt		Type;
	public DBLong		UseTm;
	public DBInt		ListNum;
	public DBInt		ExecuteNum;
	
	public static void Log(MethodEx m , eSQLMethodType type, long usetm, int listnum, int elistnum)
	{
//		if ( m == null )
//		{
//			return;
//		}
//		
//		Log.out.Log(eSystemSQLLogType.SYSTEM_SQL_DETAIL_INFO,
//				RootConfig.GetInstance().ServerUniqueID,
//				eSystemSQLLogType.GetCurrTime(),
//				m.toString(),
//				type.ID(),
//				usetm,
//				listnum,
//				elistnum
//				);
	}
}
