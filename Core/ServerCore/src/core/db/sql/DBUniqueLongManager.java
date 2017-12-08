/**
 * DBUniqueLongManager.java 2013-5-2下午11:36:29
 */
package core.db.sql;

import java.util.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DBUniqueLongManager
{
	private static DBUniqueLongManager m_Instance = new DBUniqueLongManager();
	
	public static DBUniqueLongManager GetInstance()
	{
		return m_Instance;
	}
	
	private Map<String, Long> m_Store = new HashMap<String, Long>();
	
	public long GetNextValue(String tbname, long p_lMarkID)
	{
		String key = _GetKey(tbname, p_lMarkID);
		if ( !m_Store.containsKey(key) )
		{
			m_Store.put(key, p_lMarkID);
		}
		long v = m_Store.get(key);
		m_Store.put(key, v + 1);
		return v + 1;
	}
	
	public long GetValue(String tbname, long p_lMarkID)
	{
		String key = _GetKey(tbname, p_lMarkID);
		if ( m_Store.containsKey(key) )
		{
			return m_Store.get(key);
		}
		else
		{
			return 0;
		}
	}
	
	public void SetBaseValue(String tbname, long p_lMarkID, long v)
	{
		String key = _GetKey(tbname, p_lMarkID);
		if ( m_Store.containsKey(key) )
		{
			long sv = m_Store.get(key);
			if (v > sv)
			{
				m_Store.put(key, v);
			}
		}
		else
		{
			m_Store.put(key, v);
		}
	}
	
	private String _GetKey(String tbname, long p_lMarkID)
	{
		return tbname + p_lMarkID;
	}
}
