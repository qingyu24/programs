/**
 * MySQLInserOper.java 2012-6-30下午2:42:38
 */
package core.db.mysql.oper;

import java.lang.reflect.*;
import java.sql.*;

import core.db.*;
import core.detail.SystemFn;
import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;
import core.exception.WrapRuntimeException;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 插入整条数据,并不关心是否只是一部分字段变化
 */
public class MySQLInserOper extends MySQLOper
{
	private Connection			m_Connection;	///<数据库连接
	private String				m_ClassName;
	private PreparedStatement 	m_Statement;
	
	public MySQLInserOper(Connection conn, Class<?> c,String keyname)
	{
		super(keyname, c);
		m_Connection = conn;
		m_ClassName = SystemFn.GetClassName(c);
	}

	public <T> boolean Execute(T t)
	{
		try
		{
			PreparedStatement s = _GetStatement(t);
			_AddRunParam(s,t);
			
			long stm = System.currentTimeMillis();
			boolean c =  _Execute(s, t);
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, s);
			Use();
			return c;
		}
		catch (SQLException e)
		{
			throw new WrapRuntimeException(e);
		}
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
	}
	
	private <T> void _AddRunParam(PreparedStatement s, T t) throws SQLException
	{
		int index = 1;
		Field[] fs = t.getClass().getFields();
		for ( Field f : fs )
		{
			RefField ref = f.getAnnotation(RefField.class);
			if ( ref != null )
			{
				continue;
			}
			DBValue fd = (DBValue) SystemFn.GetFieldValue(t, f);
			fd.SetStatement(index++,s);
		}
	}
	
	private <T> boolean _Execute(PreparedStatement s, T t)
	{
		try
		{
			int result = s.executeUpdate();
			if ( result > 0 )
			{
				SystemFn.ClearChangeFlag(t, false);
				SystemFn.SetRoleDataBaseFlag(t, false);
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			Log.out.LogException(e, s.toString());
			return false;
		}
	}

	private <T> PreparedStatement _GetStatement(T t) throws SQLException
	{
		if ( m_Statement == null )
		{
			String querySQL = SystemFn.GetSQLInsertAll(t.getClass(), m_ClassName, true);
			m_Statement = m_Connection.prepareStatement(querySQL);
		}
		return m_Statement;
	}
}
