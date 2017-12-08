/**
 * LoginDebugLog.java 2013-1-23下午3:21:04
 */
package logic.module.log.sql;

import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBString;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LoginDebugLog
{
	public DBInt			ServerID;
	public DBDateTime		Tm;
	public DBString			UserName;
	public DBString			IP;
	public DBInt			Type;
	public DBString			ExData;
}
