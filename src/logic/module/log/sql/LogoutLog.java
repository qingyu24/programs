/**
 * LogoutLog.java 2012-10-30下午3:22:57
 */
package logic.module.log.sql;

import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBLong;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LogoutLog
{
	public DBInt			ServerID;
	public DBDateTime		Tm;
	public DBLong			UserID;
	public DBLong			RoleID;
	public DBInt			Level;
	public DBInt			Money;
	public DBInt			Reputation;
	public DBInt			Diamond;
	public DBInt			Tick;
}
