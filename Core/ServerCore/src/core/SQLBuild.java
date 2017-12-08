/**
 * SQLBuild.java 2012-10-25上午10:28:34
 */
package core;

import java.lang.reflect.*;
import java.util.*;

import utility.*;

import core.db.*;
import core.detail.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SQLBuild
{
	private static SQLBuild m_Build = new SQLBuild();
	private Map<Class<?>, String> m_All = new HashMap<Class<?>, String>();
	
	public static SQLBuild GetInstance()
	{
		return m_Build;
	}
	
	public String GetSQL(Object o)
	{
		Class<?> c = o.getClass();
		if ( m_All.containsKey(c) )
		{
			return m_All.get(c);
		}
		String sql = _GetInsertAllSQL(c, SystemFn.GetClassName(c));
		m_All.put(c, sql);
		return sql;
	}
	
	/**
	 * 获取"INSERT INTO TBNAME (FIELD1,FIELD2...) VALUES (%s,%s...)"格式的语句
	 */
	private static String _GetInsertAllSQL(Class<?> c, String tbname)
	{
		Field[] fs = c.getFields();
		Debug.Assert(fs.length >= 2, "");
		StringBuilder querySQL = new StringBuilder();
		querySQL.append("INSERT INTO ").append(tbname).append(" (");
		
		StringBuilder valueSQL = new StringBuilder();
		valueSQL.append("VALUES (");
		boolean add = false;
		for (Field f : fs)
		{
			String fname = f.getName();
			if (add)
			{
				querySQL.append(",");
				valueSQL.append(",");
			}
			querySQL.append(fname);
			if ( f.getType() == DBDateTime.class ||
				 f.getType() == DBString.class )
			{
				valueSQL.append("'%s'");
			}
			else
			{
				valueSQL.append("%s");
			}
			add = true;
		}
		querySQL.append(") ");
		valueSQL.append(")");
		querySQL.append(valueSQL.toString());
		return querySQL.toString();
	}
	
	public static class DBTestBase
	{
		public DBInt ID;
	}
	
	public static class DBTest extends DBTestBase
	{
		public DBString Name;
		public DBDateTime Tm;
		public DBLong RID;
	}

	public static void main(String[] args)
	{
		String s = SQLBuild.GetInstance().GetSQL(new DBTest());
		System.out.println(s);
	}
}
