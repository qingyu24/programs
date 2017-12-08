/**
 * MySQLCreateOper.java 2012-6-29下午2:24:17
 */
package core.db.mysql.oper;

import java.lang.reflect.*;
import java.sql.*;

import core.db.*;
import core.detail.*;
import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;
import core.exception.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MySQLCreateOper extends MySQLOper
{
	private Connection			m_Connection;	///<数据库连接
	private Class<?>			m_DataType;		///<产生的类型
	private Field[]				m_Fields;
	private PreparedStatement 	m_Statement;
	private boolean				m_AutoField;
	
	public MySQLCreateOper(Connection conn, Class<?> c,String keyname)
	{
		super(keyname, c);
		m_Connection = conn;
		m_DataType = c;
		m_Fields = c.getFields();
		
		for ( Field f : m_Fields )
		{
			if ( f.getType() == DBAutoInt.class )
			{
				m_AutoField = true;
				break;
			}
			else if ( f.getType() == DBAutoLong.class )
			{
				m_AutoField = true;
				break;
			}
		}
		
		try
		{
			String querySQL = SystemFn.GetSQLInsertKey( SystemFn.GetClassName(c), m_sKeyName );
			m_Statement = m_Connection.prepareStatement(querySQL, Statement.RETURN_GENERATED_KEYS);
		}
		catch (SQLException e)
		{
			throw new WrapRuntimeException(e);
		}
	}

	public <T> T Execute(int p_nUserID)
	{
		ResultSet rs = null;
		T t = null;
		
		try
		{
			m_Statement.setInt(1, p_nUserID);
			long stm = System.currentTimeMillis();
			m_Statement.executeUpdate();
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, m_Statement);
			
			if ( m_AutoField)
			{
				rs = m_Statement.getGeneratedKeys();
			}
			t = _Execute(rs, p_nUserID);
			Use();
		}
		catch (SQLException e)
		{
			Log.out.LogException(e);
		}
		finally
		{
			SystemFn.CloseResultSet(rs);
		}
		return t;
	}
	
	public <T> T Execute(String p_sUserName)
	{
		ResultSet rs = null;
		T t = null;
		try
		{
			m_Statement.setString(1, p_sUserName);
			long stm = System.currentTimeMillis();
			m_Statement.executeUpdate();
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, m_Statement);
			
			if ( m_AutoField )
			{
				rs = m_Statement.getGeneratedKeys();
			}
			t = _Execute(rs, p_sUserName);
			Use();
		}
		catch (SQLException e)
		{
			Log.out.LogException(e);
		}
		finally
		{
			SystemFn.CloseResultSet(rs);
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
		m_DataType = null;
		m_Fields = null;
		SystemFn.ClosePreparedStatement(m_Statement);
		m_Statement = null;
	}

	private <T> T _Execute(ResultSet rs, Object v) throws SQLException
	{
		T t = SystemFn.CreateObject(m_DataType);
		for ( Field f : m_Fields )
		{
			if ( m_AutoField && f.getType() == DBAutoInt.class && rs.next() )
			{
				SystemFn.CreateFieldValue(t, f, rs.getInt("GENERATED_KEY"));
			}
			else if ( m_AutoField && f.getType() == DBAutoLong.class && rs.next() )
			{
				SystemFn.CreateFieldValue(t, f, rs.getLong("GENERATED_KEY"));
			}
			else if ( f.getName().equals(m_sKeyName) )
			{
				SystemFn.CreateFieldValue(t, f, v);
			}
			else
			{
				SystemFn.CreateFieldValue(t, f);
			}
		}
		return t;
	}
}
