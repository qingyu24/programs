/**
 * DBStores.java 2012-8-30上午11:58:08
 */
package core.db.sql;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;

import core.db.GlobalData;

import core.DBMgr;
import core.db.*;
import core.detail.*;
import core.detail.impl.log.*;

/**
 * @author ddoq
 * @version 1.0.0
 * 
 *          管理所有记录,对于user类型的数据,直接读直接写,所以不进行管理,只管理内存数据
 */
public class DBStores
{
	/**
	 * @author ddoq
	 * @version 1.0.0
	 * 
	 *          可以看成一个表的数据
	 */
	public static class DBStore
	{
		private ArrayList<Object>	m_DBs		= new ArrayList<Object>();
		private Map<String, Field>	m_FieldIndex= new HashMap<String, Field>();
		private Field[]				m_Fields;
		
		private Map<Long, GlobalData>	m_DBViews	= new HashMap<Long, GlobalData>();
		private String				m_TbName;

		public DBStore(Class<?> c, String tbname)
		{
			m_Fields = c.getFields();
			for (Field f : m_Fields )
			{
				m_FieldIndex.put(f.getName(), f);
			}
			m_TbName = tbname;
		}

		public <T> void Add(T t)
		{
			m_DBs.add(t);
			//强行写的优化，这个地方不怎么好
			if (m_TbName.equalsIgnoreCase("globaldata"))
			{
				GlobalData d = (GlobalData) t;
				m_DBViews.put(d.RoleID.Get(), d);
			}
		}

		public <T> void Add(T[] ts)
		{
			for (T t : ts)
			{
				Add(t);
			}
		}

		/**
		 * 找到一条记录
		 * 
		 * @return 没法找到就返回null
		 */
		public Object Get(KeyField keyfield)
		{
			Field f = _GetField(keyfield);
			if ( f == null )
			{
				return null;
			}
			if ( m_DBViews.isEmpty() )
			{
				for (Object db : m_DBs)
				{
					DBValue v = (DBValue) SystemFn.GetFieldValue(db, f);
					Object v1 = SystemFn.GetFieldValue(v);
					if (v1.equals(keyfield.GetValue()))
					{
						return db;
					}
				}
			}
			else
			{
				Long v = (Long)keyfield.GetValue();
				long key = v.longValue();
				if ( m_DBViews.containsKey(key))
				{
					return m_DBViews.get(key);
				}
			}
			return null;
		}

		/**
		 * 找到记录的某个字段的值
		 * 
		 * @return 没法找到就返回null
		 */
		public Object GetField(KeyField keyfield, String findfield)
		{
			Field f = _GetField(findfield);
			if (f == null)
			{
				return null;
			}
			Object v = Get(keyfield);
			if (v == null)
			{
				return null;
			}
			return SystemFn.GetFieldValue(v, f);
		}

		public void Remove(KeyField keyfield)
		{
			Object v = Get(keyfield);
			if (v != null)
			{
				m_DBs.remove(v);
			}
		}

		public void RemoveAll()
		{
			m_DBs.clear();
		}

		private Field _GetField(KeyField keyfield)
		{
			return m_FieldIndex.get(keyfield.key.GetName());
		}

		private Field _GetField(String field)
		{
			return m_FieldIndex.get(field);
		}

		public int Size()
		{
			return m_DBs.size();
		}
		
		public void SaveAllData()
		{
			for (Object d : m_DBs)
			{
				DBMgr.UpdateRoleData(d);
			}
			DBMgr.Commit();
		}
	}

	private static DBStores	m_Instance	= new DBStores();

	public static DBStores GetInstance()
	{
		return m_Instance;
	}

	private Map<String, DBStore>	m_All	= new HashMap<String, DBStore>();

	/**
	 * 添加记录
	 */
	public <T> void AddDBRow(String tbname, T t)
	{
		DBStore s = _GetDBStore(tbname, t.getClass());
		s.Add(t);
	}

	/**
	 * 添加记录
	 */
	public <T> void AddDBRow(String tbname, T[] ts)
	{
		if (ts.length == 0)
		{
			return;
		}
		DBStore s = _GetDBStore(tbname, ts[0].getClass());
		s.Add(ts);
	}

	/**
	 * 获取一条记录的值
	 * 
	 * @return 无法得到值则返回null
	 */
	public Object GetData(KeyField key, String tbname)
	{
		DBStore s = _GetDBStore(tbname);
		if (s == null)
		{
			return null;
		}
		return s.Get(key);
	}

	/**
	 * 获取一个字段的值
	 * 
	 * @return 无法得到值则返回null
	 */
	public Object GetFieldData(KeyField key, String tbname, String field)
	{
		DBStore s = _GetDBStore(tbname);
		if (s == null)
		{
			return null;
		}
		return s.GetField(key, field);
	}

	/**
	 * 清除所有记录
	 */
	public void RemoveAll()
	{
		m_All.clear();
	}

	/**
	 * 清除一个表中所有的记录
	 */
	public void RemoveAll(String tbname)
	{
		DBStore s = _GetDBStore(tbname);
		if (s == null)
		{
			return;
		}
		s.RemoveAll();
	}

	/**
	 * 清除一条记录
	 */
	public void RemoveDBRow(KeyField key, String tbname)
	{
		DBStore s = _GetDBStore(tbname);
		if (s == null)
		{
			return;
		}
		s.Remove(key);
	}

	private DBStore _GetDBStore(String tbname)
	{
		if (m_All.containsKey(tbname))
		{
			return m_All.get(tbname);
		}
		return null;
	}

	private DBStore _GetDBStore(String tbname, Class<?> c)
	{
		if (m_All.containsKey(tbname))
		{
			return m_All.get(tbname);
		}
		DBStore s = new DBStore(c, tbname);
		m_All.put(tbname, s);
		return s;
	}

	public void SaveAllData()
	{
		Iterator<Entry<String, DBStore>> it = m_All.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, DBStore> e = it.next();
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_GLOBAL_DATA, e.getKey(), e.getValue().Size());
			e.getValue().SaveAllData();
		}
	}
}
