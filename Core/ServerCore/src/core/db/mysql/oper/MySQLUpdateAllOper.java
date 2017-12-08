/**
 * MySQLUpdateAllOper.java 2012-8-23上午11:00:49
 */
package core.db.mysql.oper;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

import utility.*;
import core.db.*;
import core.db.mysql.*;
import core.db.sql.*;
import core.detail.*;
import core.detail.impl.log.Log;
import core.exception.*;


/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MySQLUpdateAllOper extends MySQLOper
{
	private Connection	m_Connection;	///<数据库连接
	private PreparedStatement m_InsertStatement;
	private PreparedStatement m_UpdateStatement;
	
	public MySQLUpdateAllOper(Connection conn, Class<?> c,String keyname)
	{
		super(keyname, c);
		m_Connection = conn;
		
		try
		{
			String insertSQL = SystemFn.GetSQLInsertAll(c, SystemFn.GetClassName(c), true);
			m_InsertStatement = m_Connection.prepareStatement(insertSQL);
			
			KeyField kf = BindKeyFieldFactory.GetAnyBindKeyField(c, eFieldKey.GID, eFieldKey.RoleID);
			String updateSQL = SystemFn.GetSQLUpdateAll(c, SystemFn.GetClassName(c), kf.key.GetName(), true);
			m_UpdateStatement = m_Connection.prepareStatement(updateSQL);
			
//			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, m_InsertStatement);
//			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, m_UpdateStatement);
		}
		catch (SQLException e)
		{
			throw new WrapRuntimeException(e);
		}
	}

	public <T> void Execute(T[] ts)
	{
		try
		{
			m_Connection.setAutoCommit(false);
			
			Pair<ArrayList<T>,ArrayList<T>> p = _SplitDatas(ts);
			ArrayList<T> inserts = p.first;
			ArrayList<T> updates = p.second;
			
			_AddInsert(inserts);
			_AddUpdate(updates);
			
			m_Connection.commit();
			
			for ( T t : inserts )
			{
				SystemFn.ClearChangeFlag(t, false);
				SystemFn.SetRoleDataBaseFlag(t, false);
			}
			
			for ( T t : updates )
			{
				SystemFn.ClearChangeFlag(t, false);
				SystemFn.SetRoleDataBaseFlag(t, false);
			}
			m_Connection.setAutoCommit(true);
			Use();
		}
		catch (SQLException e)
		{
			try
			{
				m_Connection.rollback();
			}
			catch (SQLException e1)
			{
				Log.out.LogException(e);
			}
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
		SystemFn.ClosePreparedStatement(m_InsertStatement);
		SystemFn.ClosePreparedStatement(m_UpdateStatement);
		m_InsertStatement = null;
		m_UpdateStatement = null;
	}
	
	private <T> void _AddInsert(ArrayList<T> ts) throws SQLException
	{
		if ( !ts.isEmpty() )
		{
			for ( T t : ts )
			{
				int index = 1;
				Field[] fs = t.getClass().getFields();
				for ( Field f : fs )
				{
					RefField ref = f.getAnnotation(RefField.class);
					if (ref != null)
					{
						continue;
					}
					DBValue fd = (DBValue) SystemFn.GetFieldValue(t, f);
					fd.SetStatement(index++,m_InsertStatement);
				}
				m_InsertStatement.addBatch();
			}
			m_InsertStatement.executeBatch();
		}
	}
	
	private <T> void _AddUpdate(ArrayList<T> ts) throws SQLException
	{
		if ( !ts.isEmpty() )
		{
			eFieldKey k = _GetKey(ts.get(0));
			for ( T t : ts )
			{
				int index = 1;
				DBValue key = null;
				Field[] fs = t.getClass().getFields();
				for ( Field f : fs )
				{
					RefField ref = f.getAnnotation(RefField.class);
					if (ref != null)
					{
						continue;
					}
					DBValue fd = (DBValue) SystemFn.GetFieldValue(t, f);
					if ( _IsKey(f, k, key) )
					{
						key = fd;
					}
					else
					{
						fd.SetStatement(index++,m_UpdateStatement);
					}
				}
				key.SetStatement(index,m_UpdateStatement);
				m_UpdateStatement.addBatch();
			}
			m_UpdateStatement.executeBatch();
		}
	}
	
	private <T> eFieldKey _GetKey(T t)
	{
		for ( Field f : t.getClass().getFields() )
		{
			if ( f.getName().equals(eFieldKey.GID.GetName()) )
			{
				return eFieldKey.GID;
			}
		}
		for ( Field f : t.getClass().getFields() )
		{
			if ( f.getName().equals(eFieldKey.RoleID.GetName()) )
			{
				return eFieldKey.RoleID;
			}
		}
		return null;
	}
	
	/**
	 * f字段是否为key
	 */
	private boolean _IsKey(Field f, eFieldKey k, DBValue key)
	{
		//已经有key了，那么不再检测
		if ( key != null )
		{
			return false;
		}
		//如果没确定字段，那么以第一个字段为准
		if ( k == null )
		{
			return true;
		}
		//确定字段，那么以字段名相同的来确定
		if ( k.GetName().equals(f.getName()) )
		{
			return true;
		}
		return false;
	}

	private <T> Pair<ArrayList<T>,ArrayList<T>> _SplitDatas(T[] ts)
	{
		ArrayList<T> inserts = new ArrayList<T>();
		ArrayList<T> updates = new ArrayList<T>();
		for ( T t : ts )
		{
			if ( SystemFn.IsMemeoryData(t))
			{
				inserts.add(t);
			}
			else if ( SystemFn.IsChanged(t, false) )
			{
				updates.add(t);
			}
		}
		return Pair.makePair(inserts, updates);
	}
}
