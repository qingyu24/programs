/**
 * CreateRoleLog.java 2012-10-30上午11:16:48
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
public class CreateRoleLog
{
	public DBInt			ServerID;
	public DBDateTime		Tm;
	public DBLong			UserID;
	public DBLong			RoleID;
	public DBInt			RoleTemplateID;
}
