/**
 * eOperType.java 2012-6-30上午10:16:30
 */
package core.db.mysql;

import java.sql.*;

import utility.*;

import core.db.mysql.oper.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eOperType
{
	READ_USERNAME,
	CREATE_USERNAME,
	UPDATE_USERNAME,
	
	READ_USERID,
	CREATE_USERID,
	UPDATE_USERID,
	
	READ_ROLEGID,
	READ_ROLENICK,
	READ_ROLEGIDBYBASE,
	CREATE_ROLEID,
	CREATE_ROLEGID,
	INSERT_ROLEID,
	UPDATE_ROLEID,
	
	READ_ALL,
	UPDATA_ALL,
	
	READ_SQL,
	READ_TRANS_SQL,
	EXECUTE_SQL,
	;
	
	public MySQLOper GetOper(Connection conn,Class<?> c)
	{
		switch(this)
		{
		case READ_USERNAME:
			return new MySQLReadOper(conn, c, "UserName");
		case CREATE_USERNAME:
			return new MySQLCreateOper(conn, c, "UserName");
		case UPDATE_USERNAME:
			return new MySQLUpdateOper(conn, c, "UserName");
		
		case READ_USERID:
			return new MySQLReadOper(conn, c, "UserID");
		case CREATE_USERID:
			return new MySQLCreateOper(conn, c, "UserID");
		case UPDATE_USERID:
			return new MySQLUpdateOper(conn, c, "UserID");
			
		case READ_ROLEGID:
			return new MySQLReadOper(conn, c, "RoleID");
		case READ_ROLENICK:
			return new MySQLReadOper(conn, c, "RoleName");
		case READ_ROLEGIDBYBASE:
			return new MySQLReadByBaseRoleIDOper(conn, c);
		case CREATE_ROLEID:
			return new MemoryCreateOper(conn, c, "RoleID");
		case CREATE_ROLEGID:
			return new MemoryCreateOper(conn, c, "GID");
		case INSERT_ROLEID:
			return new MySQLInserOper(conn, c, "*");
			
		case READ_ALL:
			return new MySQLReadAllOper(conn, c, "*");
		case UPDATA_ALL:
			return new MySQLUpdateAllOper(conn, c, "*");
		}
		Debug.Assert(false, "");
		return null;
	}
}
