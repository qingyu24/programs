/**
 * LoginLog.java 2012-10-30下午2:58:02
 */
package logic.module.log.sql;

import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBLong;
import core.db.DBString;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LoginLog
{
	public DBInt			ServerID;
	public DBDateTime		Tm;
	public DBLong 			UserID;
	public DBString			UserName;
	public DBString			IP;
	public DBString			Device;
	public DBString			DeviceModel;
	public DBInt			Type;
}
