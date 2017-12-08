/**
 * GlobalData.java 2013-7-8下午9:03:13
 */
package modifyroleid.db;

import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBLong;
import core.db.DBString;
import core.db.RoleDataBase;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class GlobalData extends RoleDataBase 
{
	public DBLong RoleID;
	public DBInt	TemplateID;
	public DBString NickName;
	public DBInt	RoleLV;
	public DBDateTime LoginTime;
	public DBInt	TempVipLevel;
	public DBInt	VipLevel;
}
