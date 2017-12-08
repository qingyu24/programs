/**
 * MemoryCreateOper.java 2012-6-30上午11:37:02
 */
package core.db.mysql.oper;

import java.lang.reflect.*;
import java.sql.*;

import utility.*;

import core.db.*;
import core.db.mysql.*;
import core.db.sql.*;
import core.detail.*;
import core.exception.WrapRuntimeException;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MemoryCreateOper extends MySQLOper
{
	private Class<?>		m_DataType;		///<产生的类型
	private Field[]			m_Fields;		///<所有的字段
	private boolean			m_BindExData;	///<是否绑定了其他的数据结构
	private KeyField	m_BindKeyField;	///<当绑定了其他的数据结构，利用这个对象来获取绑定的对象
	
	public MemoryCreateOper(Connection conn, Class<?> c, String keyname)
	{
		super(keyname, c);
		m_DataType = c;
		m_Fields = m_DataType.getFields();
		m_BindExData = SystemFn.IsNeedBindExData(c);
		if ( m_BindExData )
		{
			m_BindKeyField = BindKeyFieldFactory.GetAnyBindKeyField(c, eFieldKey.GID, eFieldKey.RoleID);
		}
	}

	public <T> T Execute()
	{
		T t = null;
		if ( !m_BindExData )
		{
			t = _CreateSimpleData();
		}
		else
		{
			t = _CreateBindData();
		}
		SystemFn.SetRoleDataBaseFlag(t, true);
		Use();
		return t;
	}
	
	public <T> T Execute(long p_lRoleID)
	{
		T t = null;
		if ( !m_BindExData )
		{
			t = _CreateSimpleData(p_lRoleID);
		}
		else
		{
			t = _CreateBindData(p_lRoleID);
		}
		SystemFn.SetRoleDataBaseFlag(t, true);
		Use();
		return t;
	}
	
	/* (non-Javadoc)
	 * @see core.db.mysql.oper.MySQLOper#Release()
	 */
	@Override
	public void Release()
	{
		m_DataType = null;
		m_Fields = null;
		m_BindKeyField = null;
	}
	
	private <T> T _CreateBindData()
	{
		T t = SystemFn.CreateObject(m_DataType);
		for ( Field f : m_Fields )
		{
			RefField ref = f.getAnnotation(RefField.class);
			if ( ref == null )
			{
				Object fo = SystemFn.CreateFieldValue(t, f);
				m_BindKeyField.FetchValue(f.getName(), (DBValue) fo);
			}
		}
		
		Debug.Assert(m_BindKeyField.IsInitValue(), "");
		
		for ( Field f : m_Fields )
		{
			RefField ref = f.getAnnotation(RefField.class);
			if ( ref != null )
			{
				Pair<String,String> pair = SystemFn.SplitRefString(ref.Bind(), f.getName());
				Object fo = DBStores.GetInstance().GetFieldData(m_BindKeyField, pair.first, pair.second);
				if ( fo == null )
				{
					throw new WrapRuntimeException("根据值[" + m_BindKeyField + "]" + "无法获取[" + pair + "]");
				}
				SystemFn.SetFieldValue(t, f, fo);
			}
		}
		return t;
	}
	
	private <T> T _CreateBindData(long p_lRoleID)
	{
		boolean gid = false;
		m_BindKeyField.ResetValue();
		T t = SystemFn.CreateObject(m_DataType);
		for ( Field f : m_Fields )
		{
			RefField ref = f.getAnnotation(RefField.class);
			if ( ref == null )
			{
				Object fo = null;
				if ( gid == false && f.getName().equals("GID") && f.getType() == DBUniqueLong.class)
				{
					gid = true;
					fo = SystemFn.CreateFieldValueUniqueLong(t, f, p_lRoleID);
				}
				else
				{
					fo = SystemFn.CreateFieldValue(t, f);
				}
				
				if ( f.getName() == m_sKeyName )
				{
					DBLong v = (DBLong) fo;
					v.Set(p_lRoleID);
				}
				
				m_BindKeyField.FetchValue(f.getName(), (DBValue) fo);
			}
		}
		
		Debug.Assert(m_BindKeyField.IsInitValue(), "");
		
		for ( Field f : m_Fields )
		{
			RefField ref = f.getAnnotation(RefField.class);
			if ( ref != null )
			{
				Pair<String,String> pair = SystemFn.SplitRefString(ref.Bind(), f.getName());
				Object fo = DBStores.GetInstance().GetFieldData(m_BindKeyField, pair.first, pair.second);
				if ( fo == null )
				{
					throw new WrapRuntimeException("根据值[" + m_BindKeyField + "]" + "无法获取[" + pair + "]");
				}
				SystemFn.SetFieldValue(t, f, fo);
			}
		}
		return t;
	}
	
	private <T> T _CreateSimpleData()
	{
		T t = SystemFn.CreateObject(m_DataType);
		for ( Field f : m_Fields )
		{
			SystemFn.CreateFieldValue(t, f);
		}
		return t;
	}

	private <T> T _CreateSimpleData(long p_lRoleID)
	{
		boolean gid = false;
		T t = SystemFn.CreateObject(m_DataType);
		for ( Field f : m_Fields )
		{
			Object fo = null;
			if ( gid == false && f.getName().equals("GID") && f.getType() == DBUniqueLong.class)
			{
				gid = true;
				fo = SystemFn.CreateFieldValueUniqueLong(t, f, p_lRoleID);
			}
			else
			{
				fo = SystemFn.CreateFieldValue(t, f);
			}
			
			if ( f.getName().equals(m_sKeyName) )
			{
				DBLong v = (DBLong) fo;
				v.Set(p_lRoleID);
			}
		}
		return t;
	}
}
