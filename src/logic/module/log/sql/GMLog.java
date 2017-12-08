/**
 * GMLog.java 2013-5-23上午10:40:57
 */
package logic.module.log.sql;

import core.RootConfig;
import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBShort;
import core.db.DBString;
import core.detail.impl.log.Log;
import logic.module.log.eLogicSQLLogType;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class GMLog
{
	public DBInt		ServerID;
	public DBDateTime	Tm;
	public DBShort		MID;
	public DBString		MParam;
	public DBShort		MResult;
	
	public static void Log(int mid, String param, int result)
	{
		Log.out.Log(eLogicSQLLogType.LOGIC_GM, 
				RootConfig.GetInstance().ServerUniqueID,
				eLogicSQLLogType.GetCurrTime(),
				mid,
				param,
				result
				);
	}
}
