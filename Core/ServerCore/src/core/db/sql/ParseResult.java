/**
 * ParseResult.java 2012-9-3下午12:03:16
 */
package core.db.sql;

import java.lang.reflect.*;
import java.lang.reflect.Array;
import java.sql.*;
import java.util.*;

import utility.*;

import core.db.*;
import core.db.mysql.eFieldKey;
import core.detail.SystemFn;
import core.exception.WrapRuntimeException;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ParseResult
{
	private Class<?>			m_DataType;		///<产生的类型
	private Field[]				m_Fields;		///<产生的类型字段
	private Field[]				m_ReadFields;	///<读取的类型字段
	private boolean				m_BindExData;	///<是否绑定了其他的数据结构
	private KeyField			m_BindKeyField;	///<当绑定了其他的数据结构，利用这个对象来获取绑定的对象
	@SuppressWarnings("rawtypes")
	private ArrayList			m_Datas = new ArrayList();
	
	public ParseResult(Class<?> c)
	{
		m_DataType = c;
		m_Fields = c.getFields();
		m_ReadFields = null;
		
		m_BindExData = SystemFn.IsNeedBindExData(c);
		if ( m_BindExData )
		{
			m_BindKeyField = BindKeyFieldFactory.GetAnyBindKeyField(c, eFieldKey.GID, eFieldKey.RoleID);
		}
	}
	
	/**
	 * @param c 产生的类型
	 * @param c1 读取的类型
	 */
	public ParseResult(Class<?> c, Class<?> c1)
	{
		m_DataType = c;
		m_Fields = c.getFields();
		m_ReadFields = c1.getFields();
		
		m_BindExData = SystemFn.IsNeedBindExData(c1);
		if ( m_BindExData )
		{
			m_BindKeyField = BindKeyFieldFactory.GetAnyBindKeyField(c1, eFieldKey.GID, eFieldKey.RoleID);
		}
	}
	
	/**
	 * 获取一条数据
	 */
	public <T> T Read(ResultSet resultSet) throws SQLException
	{
		return _Get(resultSet);
	}

	/**
	 * 获取所有数据
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] ReadAll(ResultSet resultSet) throws SQLException
	{
		m_Datas.clear();
		T t = null;
		while ( (t = _Get(resultSet)) != null )
		{
			m_Datas.add(t);
		}
		T[] v = (T[]) Array.newInstance(m_DataType, m_Datas.size());
		for ( int i = 0; i < m_Datas.size(); ++i )
		{
			v[i] = (T) m_Datas.get(i);
		}
		return v;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T[] ReadAll(ResultSet resultSet, long p_lMarkID) throws SQLException
	{
		m_Datas.clear();
		T t = null;
		while ( (t = _Get(resultSet, p_lMarkID)) != null )
		{
			m_Datas.add(t);
		}
		T[] v = (T[]) Array.newInstance(m_DataType, m_Datas.size());
		for ( int i = 0; i < m_Datas.size(); ++i )
		{
			v[i] = (T) m_Datas.get(i);
		}
		return v;
	}
	
	private <T> T _Get(ResultSet resultSet) throws SQLException
	{
		if ( !m_BindExData )
		{
			return _GetSimple(resultSet);
		}
		else
		{
			return _GetEx(resultSet);
		}
	}
	
	private <T> T _Get(ResultSet resultSet, long p_lMarkID) throws SQLException
	{
		if ( !m_BindExData )
		{
			return _GetSimple(resultSet, p_lMarkID);
		}
		else
		{
			return _GetEx(resultSet, p_lMarkID);
		}
	}
	
	private <T> T _GetSimple(ResultSet resultSet, long p_lMarkID) throws SQLException
	{
		if ( resultSet != null && resultSet.next() )
		{
			T t = SystemFn.CreateObject(m_DataType);
			boolean gid = false;
			for ( Field f : m_Fields )
			{
				Object v = SystemFn.CreateFieldValue(t, f, resultSet);
				if ( gid == false && f.getType() == DBUniqueLong.class )
				{
					gid = true;
					DBUniqueLong v1 = (DBUniqueLong) v;
					String tbname = SystemFn.GetClassName(t.getClass());
					DBUniqueLongManager.GetInstance().SetBaseValue(tbname, p_lMarkID, v1.Get());
				}
				
			}
			return t;
		}
		return null;
	}
	
	private <T> T _GetSimple(ResultSet resultSet) throws SQLException
	{
		if ( resultSet != null && resultSet.next() )
		{
			T t = SystemFn.CreateObject(m_DataType);
			for ( Field f : m_Fields )
			{
				SystemFn.CreateFieldValue(t, f, resultSet);
			}
			return t;
		}
		return null;
	}
	
	private <T> T _GetEx(ResultSet resultSet, long p_lMarkID) throws SQLException
	{
		if ( resultSet != null && resultSet.next() )
		{
			m_BindKeyField.ResetValue();
			boolean gid = false;
			T t = SystemFn.CreateObject(m_DataType);
			for ( int i = 0; i < m_Fields.length; ++i )
			{
				Field f = m_ReadFields == null ? m_Fields[i] : m_ReadFields[i];
				RefField ref = f.getAnnotation(RefField.class);
				if ( ref == null )
				{
					Object fo = SystemFn.CreateFieldValue(t, m_Fields[i], resultSet);
					m_BindKeyField.FetchValue(f.getName(), (DBValue) fo);
					
					if ( gid == false && f.getType() == DBUniqueLong.class )
					{
						gid = true;
						DBUniqueLong v1 = (DBUniqueLong) fo;
						String tbname = SystemFn.GetClassName(t.getClass());
						DBUniqueLongManager.GetInstance().SetBaseValue(tbname, p_lMarkID, v1.Get());
					}
				}
			}
			
			for ( int i = 0; i < m_Fields.length; ++i )
			{
				Field f = m_ReadFields == null ? m_Fields[i] : m_ReadFields[i];
				RefField ref = f.getAnnotation(RefField.class);
				if ( ref != null )
				{
					Pair<String,String> pair = SystemFn.SplitRefString(ref.Bind(), f.getName());
					Object fo = DBStores.GetInstance().GetFieldData(m_BindKeyField, pair.first, pair.second);
					if ( fo == null )
					{
						throw new WrapRuntimeException("根据值[" + m_BindKeyField + "]" + "无法获取[" + pair + "]");
					}
					SystemFn.SetFieldValue(t, m_Fields[i], fo);
				}
			}
			return t;
		}
		return null;
	}

	private <T> T _GetEx(ResultSet resultSet) throws SQLException
	{
		if ( resultSet != null && resultSet.next() )
		{
			m_BindKeyField.ResetValue();
			T t = SystemFn.CreateObject(m_DataType);
			for ( int i = 0; i < m_Fields.length; ++i )
			{
				Field f = m_ReadFields == null ? m_Fields[i] : m_ReadFields[i];
				RefField ref = f.getAnnotation(RefField.class);
				if ( ref == null )
				{
					Object fo = SystemFn.CreateFieldValue(t, m_Fields[i], resultSet);
					m_BindKeyField.FetchValue(f.getName(), (DBValue) fo);
				}
			}
			
			for ( int i = 0; i < m_Fields.length; ++i )
			{
				Field f = m_ReadFields == null ? m_Fields[i] : m_ReadFields[i];
				RefField ref = f.getAnnotation(RefField.class);
				if ( ref != null )
				{
					Pair<String,String> pair = SystemFn.SplitRefString(ref.Bind(), f.getName());
					Object fo = DBStores.GetInstance().GetFieldData(m_BindKeyField, pair.first, pair.second);
					if ( fo == null )
					{
						throw new WrapRuntimeException("根据值[" + m_BindKeyField + "]" + "无法获取[" + pair + "]");
					}
					SystemFn.SetFieldValue(t, m_Fields[i], fo);
				}
			}
			return t;
		}
		return null;
	}
	
	public static <T,T1> T Trans(T t, T1 t1)
	{
		Field[] fs = t1.getClass().getFields();
		for ( int i = 0; i < fs.length; ++i )
		{
			Field f = fs[i];
			RefField ref = f.getAnnotation(RefField.class);
			if ( ref != null )
			{
				try
				{
					Field writeField = t.getClass().getField(f.getName());
					
					KeyField k = BindKeyFieldFactory.GetAnyBindKeyField(t1.getClass(), eFieldKey.RoleID);
					Object fo = SystemFn.GetFieldValue(t, eFieldKey.RoleID.GetName());
					k.FetchValue(eFieldKey.RoleID.GetName(), (DBValue) fo);
					
					Pair<String,String> pair = SystemFn.SplitRefString(ref.Bind(), f.getName());
					Object fo1 = DBStores.GetInstance().GetFieldData(k, pair.first, pair.second);
					
					if ( fo1 == null )
					{
						throw new WrapRuntimeException("根据值[" + k + "]" + "无法获取[" + pair + "]");
					}
					
					Object fo2 = SystemFn.CreateRefObject(fo1);
					SystemFn.SetFieldValue(t, writeField, fo2);
				}
				catch (Exception e)
				{
					throw new WrapRuntimeException(e);
				}
			}
		}
		return t;
	}

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 */
	public void Release()
	{
		// TODO Auto-generated method stub
		
	}
}
