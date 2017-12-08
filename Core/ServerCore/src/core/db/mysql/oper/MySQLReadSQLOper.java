/**
 * MySQLReadSQLOper.java 2012-8-24上午11:18:35
 */
package core.db.mysql.oper;

import java.sql.*;

import core.db.sql.*;
import core.detail.SystemFn;
import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;
import core.exception.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MySQLReadSQLOper extends MySQLOper
{
	private Connection			m_Connection;	///<数据库连接
	private PreparedStatement 	m_Statement;
	private ParseResult			m_ParseResult;
	
	public MySQLReadSQLOper(Connection conn, Class<?> c, String keyname, String sql)
	{
		super(keyname, c);
		
		m_Connection = conn;
		m_ParseResult = new ParseResult(c);
		
		try
		{
			m_Statement = m_Connection.prepareStatement(sql);
		}
		catch (SQLException e)
		{
			throw new WrapRuntimeException(e);
		}
	}

	public <T> T[] Execute()
	{
		ResultSet resultSet = null;
		T[] ts = null;
		try
		{
			long stm = System.currentTimeMillis();
			resultSet = m_Statement.executeQuery();
			ts = m_ParseResult.ReadAll(resultSet);
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, m_Statement);
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_READ_NUM, ts.length);
			Use();
		}
		catch (SQLException e)
		{
			Log.out.LogException(e);
		}
		finally
		{
			SystemFn.CloseResultSet(resultSet);
		}
		return ts;
	}

	/* (non-Javadoc)
	 * @see core.db.mysql.oper.MySQLOper#Release()
	 */
	@Override
	public void Release()
	{
		m_Connection = null;
		SystemFn.ClosePreparedStatement(m_Statement);
		m_Statement = null;
		m_Statement = null;
		m_ParseResult.Release();
		m_ParseResult = null;
	}
}
