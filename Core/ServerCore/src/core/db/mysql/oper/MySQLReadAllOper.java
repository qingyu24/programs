/**
 * MySQLReadAllOper.java 2012-8-23上午10:53:31
 */
package core.db.mysql.oper;

import java.sql.*;

import core.db.sql.*;
import core.detail.*;
import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;
import core.exception.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MySQLReadAllOper extends MySQLOper
{
	private Connection			m_Connection;	///<数据库连接
	private PreparedStatement 	m_Statement;
	private ParseResult			m_ParseResult;
	
	public MySQLReadAllOper(Connection conn, Class<?> c,String keyname)
	{
		super(keyname, c);
		
		m_Connection = conn;
		m_ParseResult = new ParseResult(c);
		
		try
		{
			String querySQL = SystemFn.GetSQLSelectAll( SystemFn.GetClassName(c) );
			m_Statement = m_Connection.prepareStatement(querySQL);
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
		m_ParseResult.Release();
		m_ParseResult = null;
		
	}
}
