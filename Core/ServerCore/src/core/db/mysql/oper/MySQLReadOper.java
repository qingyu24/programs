/**
 * MySQLReadOper.java 2012-6-29下午2:27:15
 */
package core.db.mysql.oper;

import java.sql.*;

import core.db.sql.*;
import core.detail.*;
import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;
import core.exception.*;

import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MySQLReadOper extends MySQLOper
{
	private Connection			m_Connection;	///<数据库连接
	private PreparedStatement 	m_Statement;
	private ParseResult			m_ParseResult;
	
	public MySQLReadOper(Connection conn, Class<?> c,String keyname)
	{
		super(keyname, c);
		Debug.Assert( SystemFn.IsHaveField(c, m_sKeyName), "" );
		m_Connection = conn;
		
		m_ParseResult = new ParseResult(c);
		
		try
		{
			String querySQL = SystemFn.GetSQLSelectAllFields(c, m_sKeyName);
			m_Statement = m_Connection.prepareStatement(querySQL);
		}
		catch (SQLException e)
		{
			throw new WrapRuntimeException(e);
		}
	}
	
	public <T> T Execute(int p_nUserID)
	{
		ResultSet resultSet = null;
		T t = null;
		try
		{
			m_Statement.setInt(1, p_nUserID);
			resultSet = m_Statement.executeQuery();
			
			long stm = System.currentTimeMillis();
			t = m_ParseResult.Read(resultSet);
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, m_Statement);
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_READ_NUM, t == null ? 0 : 1);
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
		return t;
	}
	
	public <T> T[] Execute(long p_lGid)
	{
		ResultSet resultSet = null;
		T[] ts = null;
		try
		{
			long stm = System.currentTimeMillis();
			m_Statement.setLong(1, p_lGid);
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
	
	public <T> T Execute(String p_sUserName)
	{
		ResultSet resultSet = null;
		T t = null;
		try
		{
			m_Statement.setString(1, p_sUserName);
			
			long stm = System.currentTimeMillis();
			resultSet = m_Statement.executeQuery();
			t = m_ParseResult.Read(resultSet);
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, m_Statement);
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_READ_NUM, t == null ? 0 : 1);
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
		return t;
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
