/**
 * KeepAliveSQLRun.java 2012-8-20下午5:29:21
 */
package logic.sqlrun;

import core.DBMgr;
import logic.MyUser;
//import logic.userdata.login.RoleData;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class KeepAliveSQLRun extends MySQLRun
{
	private static final String m_SelectUserData = "SELECT * FROM LoginData WHERE UserID = 99999999";
	
	/* (non-Javadoc)
	 * @see logic.sqlrun.MySQLRun#Execute(logic.MyUser)
	 */
	@Override
	public void Execute(MyUser p_User) throws Exception
	{
//		DBMgr.ReadUserData(99999, new RoleData());
		DBMgr.ExecuteSQL(m_SelectUserData);
	}
}
