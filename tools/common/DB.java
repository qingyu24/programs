/**
 * DB.java 2013-7-4下午2:18:46
 */
package common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import core.db.mysql.MySQLDBReader;
import core.db.mysql.SQLResultParse;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DB
{
	private class TbNameParse implements SQLResultParse
	{

		/* (non-Javadoc)
		 * @see core.db.mysql.SQLResultParse#Parse(java.sql.ResultSet)
		 */
		@Override
		public void Parse(ResultSet r) throws SQLException
		{
			while (r.next())
			{
				String tbname = r.getString("table_name");
				m_AllTbNames.add(tbname);
			}
		}
	}
	private final MySQLDBReader m_DB;
	private final TbNameParse m_TbNameParse = new TbNameParse();
	
	private ArrayList<String> m_AllTbNames = new ArrayList<String>();
	
	public DB(MySQLDBReader db)
	{
		m_DB = db;
	}
	
	public void Add(Object[] datas)
	{
		if (datas == null)
		{
			return;
		}
		int num = 0;
		for (Object data : datas)
		{
			m_DB.UpdateRoleData(data, true);
			num++;
			if (num % 1000 == 0)
			{
				Commit();
			}
		}
	}

	public void Add(Object data)
	{
		m_DB.UpdateRoleData(data, true);
	}

	public void Commit()
	{
		m_DB.Commit();
	}

	public ArrayList<String> GetAllTbName()
	{
		if ( m_AllTbNames.isEmpty() )
		{
			m_DB.ExecuteSQL(String.format("SELECT * FROM information_schema.tables WHERE table_schema='%s'", m_DB.GetDBName()), m_TbNameParse);
		}
		return m_AllTbNames;
	}

	public String GetDBName()
	{
		return m_DB.GetDBName();
	}
	
	public Object[] ReadAll(Class<?> c)
	{
		Object o;
		try
		{
			o = c.newInstance();
			Object[] obs = m_DB.ReadAllData(o, false);
			return obs;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public <T> T[] ReadAllByType(T t)
	{
		try
		{
			T[] obs = m_DB.ReadAllData(t, false);
			return obs;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void ReleaseAll()
	{
		m_DB.ReleaseAll();
	}
}
