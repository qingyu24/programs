/**
 * MySQLUpdateOper.java 2012-6-29下午2:29:00
 */
package core.db.mysql.oper;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;


import core.db.*;
import core.detail.*;
import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;
import core.exception.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 只更新修改了的字段
 */
public class MySQLUpdateOper extends MySQLOper
{
	private Connection	m_Connection;	///<数据库连接
	private String		m_ClassName;
	private Map<String,PreparedStatement> m_AllStatement = new HashMap<String,PreparedStatement>();
	
	public MySQLUpdateOper(Connection conn, Class<?> c,String keyname)
	{
		super(keyname, c);
		m_Connection = conn;
		m_ClassName = SystemFn.GetClassName(c);
	}
	
	public <T> boolean Execute(T t, boolean force)
	{
		try
		{
			PreparedStatement s = _GetStatement(t);
			if (s != null)
			{
				_AddRunParam(s, t);
				long stm = System.currentTimeMillis();
				boolean c =  _Execute(s, t);
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, s);
				Use();
				return c;
				
			}
			else
			{
				//没有任何数据改变
				return true;
			}
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
		Iterator<Entry<String, PreparedStatement>> it = m_AllStatement.entrySet().iterator();
		while (it.hasNext())
		{
			SystemFn.ClosePreparedStatement(it.next().getValue());
		}
		m_AllStatement.clear();
	}

	private <T> void _AddRunParam(PreparedStatement s,T t) throws SQLException
	{
		int index = 1;
		DBValue key = null;
		Field[] fs = t.getClass().getFields();
		for ( Field f : fs )
		{
			RefField ref = f.getAnnotation(RefField.class);
			if ( ref != null )
			{
				continue;
			}
			DBValue fd = (DBValue) SystemFn.GetFieldValue(t, f);
			if ( f.getName().equals(m_sKeyName) )
			{
				key = fd;
				continue;
			}
			if ( fd.IsChanged() )
			{
				fd.SetStatement(index++, s);
			}
		}
		key.SetStatement(index,s);
	}
	
	private <T> boolean _Execute(PreparedStatement s, T t)
	{
		try
		{
			int result = s.executeUpdate();
			if ( result > 0 )
			{
				SystemFn.ClearChangeFlag(t, false);
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			Log.out.LogException(e);
			return false;
		}
	}

	private <T> PreparedStatement _GetStatement(T t) throws SQLException
	{
		String key = SystemFn.GetAllChangeFieldName(t);
		if ( key.isEmpty() )
		{
			return null;
		}
		if ( m_AllStatement.containsKey(key) )
		{
			return m_AllStatement.get(key);
		}
		String querySQL = SystemFn.GetSQLUpdateChange(t, m_ClassName, m_sKeyName, true);
		if ( querySQL == null )
		{
			return null;
		}
		PreparedStatement s = m_Connection.prepareStatement(querySQL);
		m_AllStatement.put(key, s);
		return s;
	}
}
