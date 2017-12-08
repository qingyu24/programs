/**
 * MySQLLogExecuteOper.java 2013-2-28下午5:33:24
 */
package core.db.mysql.oper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.jdbc.authentication.MysqlOldPasswordPlugin;

import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MySQLLogExecuteOper extends MysqlOldPasswordPlugin
{
	private Connection			m_Connection;	///<数据库连接
	private PreparedStatement 	m_Statement;
	
	public MySQLLogExecuteOper(Connection conn)
	{
		m_Connection = conn;
	}

	public boolean Execute(String sql)
	{
		try
		{
			long stm = System.currentTimeMillis();
			m_Statement = m_Connection.prepareStatement(sql);
			m_Statement.execute();
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, m_Statement);
			m_Statement.close();
			return true;
		}
		catch (SQLException e)
		{
			Log.out.LogException(e);
			return false;
		}
	}
}
