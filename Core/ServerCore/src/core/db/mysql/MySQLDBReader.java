/**
 * MySQLDBReader.java 2012-6-28上午11:27:02
 */
package core.db.mysql;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

import core.*;
import core.db.*;
import core.db.mysql.oper.*;
import core.db.sql.*;
import core.detail.*;
import core.detail.impl.log.*;
import core.exception.WrapRuntimeException;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MySQLDBReader implements DBReader
{
	private static MySQLDBReader m_Instance = new MySQLDBReader();
	
	public static MySQLDBReader GetInstance()
	{
		return m_Instance;
	}
	private Connection				m_Connection;
	@SuppressWarnings("unused")
	private Connection				m_LogConnection;
	private MySQLLogExecuteOper		m_LogOper;
	private Map<String,MySQLOper> 	m_Opers = new HashMap<String,MySQLOper>();
	
	private ArrayList<DBLoader>		m_DbLoaders = new ArrayList<DBLoader>();
	private String					m_DBName;
	
	public MySQLDBReader()
	{
		
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#AddDBLoader(core.DBLoader)
	 */
	@Override
	public void AddDBLoader(DBLoader l)
	{
		m_DbLoaders.add(l);
	}

	/* (non-Javadoc)
	 * @see core.db.DBReader#Commit()
	 */
	@Override
	public void Commit()
	{
		try
		{
			m_Connection.commit();
		}
		catch (SQLException e)
		{
			Log.out.LogException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#CreateRoleData(long, java.lang.Object)
	 * 
	 * @tip 这是内存数据
	 */
	@Override
	public <T> T CreateRoleData(long p_lGid, T t, boolean c)
	{
		MemoryCreateOper o = _GetCreateRoleIDOper(t);
		T v = o.Execute(p_lGid);

		if ( c )
		{
			String tbname = SystemFn.GetClassName(t.getClass());
			DBStores.GetInstance().AddDBRow(tbname, v);
		}
		return v;
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#CreateRoleDataByGID(java.lang.Object)
	 */
	@Override
	public <T> T CreateRoleDataByGID(T t, boolean c)
	{
		MemoryCreateOper o = _GetCreateGIDOper(t);
		T v = o.Execute();

		if ( c )
		{
			String tbname = SystemFn.GetClassName(t.getClass());
			DBStores.GetInstance().AddDBRow(tbname, v);
		}
		return v;
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#CreateUserData(int, java.lang.Object)
	 */
	@Override
	public <T> T CreateUserData(int p_userid, T t)
	{
		MySQLCreateOper o = _GetCreateUserIDOper(t);
		T v = o.Execute(p_userid);
		if ( v != null )
		{
			return v;
		}
		return ReadUserData(p_userid, t);
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#CreateUserData(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> T CreateUserData(String p_username, T t)
	{
		MySQLCreateOper o = _GetCreateUserNameOper(t);
		T v = o.Execute(p_username);
		if ( v != null )
		{
			return v;
		}
		return ReadUserData(p_username, t);
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#ExecuteSQL(java.lang.String)
	 */
	@Override
	public boolean ExecuteSQL(String sql)
	{
		MySQLExecuteOper o = _GetExecuteSQLOper(sql);
		return o.Execute();
	}
	
	public boolean ExecuteSQL(String sql, SQLResultParse parse)
	{
		MySQLExecuteOper o = _GetExecuteSQLOper(sql);
		return o.Execute(parse);
	}
	
	public String GetDBName()
	{
		return m_DBName;
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#Init()
	 */
	@Override
	public void Init()
	{
		RootConfig c = RootConfig.GetInstance();
		Init(c.DBIp, c.DBPort, c.DBName, c.DBUser, c.DBPassword);
	}
	
	public void Init(String ip, int port, String dbname, String user, String psd)
	{
		if ( m_Connection == null )
		{
			m_DBName = dbname;
			String url = String.format("jdbc:mysql://%s:%d/%s?rewriteBatchedStatements=true&characterEncoding=utf8", ip, port, dbname);
			
			String driver = "com.mysql.jdbc.Driver";
			try
			{
				Class.forName(driver);
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_OPEN_SQL, url);
				m_Connection = DriverManager.getConnection(url, user, psd);
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_OPEN_SQL_FINISH, url);
				
				m_Connection.setAutoCommit(false);
				//主动优化
				Statement s = m_Connection.createStatement();
				s.execute("set global innodb_flush_log_at_trx_commit=2");
				s.execute("set global wait_timeout=604800");
			}
			catch (ClassNotFoundException e)
			{
				Log.out.LogException(e);
				throw new WrapRuntimeException(e);
			}
			catch (SQLException e)
			{
				Log.out.LogException(e);
				throw new WrapRuntimeException(e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#LoadAll()
	 */
	@Override
	public void LoadAll()
	{
		for ( DBLoader l : m_DbLoaders )
		{
			try
			{
				l.OnLoad();
			}
			catch (Exception e)
			{
				Log.out.LogException(e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#ReadAllData(java.lang.Object)
	 */
	@Override
	public <T> T[] ReadAllData(T t, boolean c)
	{
		MySQLReadAllOper o = _GetReadAllOper(t);
		T[] vs = o.Execute();
		
		String tbname = SystemFn.GetClassName(t.getClass());
		if ( c )
		{
			DBStores.GetInstance().AddDBRow(tbname, vs);
		}
		return vs;
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#ReadRoleData(long, java.lang.Object)
	 */
	@Override
	public <T> T[] ReadRoleGIDData(long p_lGid, T t)
	{
//		MySQLReadOper o = _GetReadRoleGIDOper(t);
//		T[] vs = o.Execute(p_lGid);
		
		MySQLReadByBaseRoleIDOper o = _GetReadRoleBaseRoleOper(t);
		T[] vs = o.Execute(p_lGid);
//		String tbname = SystemFn.GetClassName(t.getClass());
//		DBStores.GetInstance().AddDBRow(tbname, vs);
		return vs;
	}
	
	@Override
	public <T> T[] ReadRoleIDData(long p_roleId, T t)
	{
		//todo niuhao;
		MySQLReadOper o = _GetReadRoleGIDOper(t);
		T[] vs = o.Execute(p_roleId);
//		String tbname = SystemFn.GetClassName(t.getClass());
//		DBStores.GetInstance().AddDBRow(tbname, vs);
		return vs;
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#ReadRoleNickData(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> T ReadRoleNickData(String p_sNick, T t)
	{
		MySQLReadOper o = _GetReadRoleNickOper(t);
		T v = o.Execute(p_sNick);
		
//		String tbname = SystemFn.GetClassName(t.getClass());
//		DBStores.GetInstance().AddDBRow(tbname, v);
		return v;
	}

	/* (non-Javadoc)
	 * @see core.db.DBReader#ReadSQL(java.lang.Object, java.lang.String)
	 */
	@Override
	public <T> T[] ReadSQL(T t, String sql)
	{
		MySQLReadSQLOper o = _GetReadSQLOper(t, sql);
		T[] v = o.Execute();
		return v;
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#ReadTransSQL(java.lang.Object, java.lang.Object, java.lang.String)
	 */
	@Override
	public <T, T1> T[] ReadTransSQL(T t, T1 t1, String sql)
	{
		MySQLReadTransSQLOper o = _GetReadTransSQLOper(t, t1, sql);
		return o.Execute();
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#ReadUserData(int, java.lang.Object)
	 */
	@Override
	public <T> T ReadUserData(int p_userid, T t)
	{
		MySQLReadOper o = _GetReadUserIDOper(t);
		T v = o.Execute(p_userid);
		return v;
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#ReadUserData(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> T ReadUserData(String p_username, T t)
	{
		MySQLReadOper o = _GetReadUserNameOper(t);
		T v = o.Execute(p_username);
		return v;
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#SaveAll()
	 */
	@Override
	public void SaveAll()
	{
		for ( DBLoader l : m_DbLoaders )
		{
			try
			{
				l.OnSave();
			}
			catch (Exception e)
			{
				Log.out.LogException(e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#SaveLog(java.lang.String)
	 */
	@Override
	public boolean SaveLog(String sql)
	{
		return m_LogOper.Execute(sql);
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#TransSQL(java.lang.Object, java.lang.Object)
	 */
	@Override
	public <T, T1> T TransSQL(T t, T1 t1)
	{
		return ParseResult.Trans(t, t1);
	}

	/* (non-Javadoc)
	 * @see core.db.DBReader#Update()
	 */
	@Override
	public void Update()
	{
		int num = 0;
		long stm = System.currentTimeMillis();
		Iterator<Entry<String, MySQLOper>> it = m_Opers.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, MySQLOper> e = it.next();
			if ( e.getValue().IsCanRemove() )
			{
				e.getValue().Release();
				it.remove();
				num++;
			}
			
			if ( System.currentTimeMillis() - stm > 200 )
			{
				break;
			}
		}
		
		if ( num > 0 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "#$%释放MySQLOper:" + num);
		}
	}
	

	/* (non-Javadoc)
	 * @see core.db.DBReader#UpdateAllData(T[])
	 */
	@Override
	public <T> void UpdateAllData(T[] t)
	{
		MySQLUpdateAllOper o = _GetUpdateAllOper(t);
		o.Execute(t);
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#UpdateRoleData(java.lang.Object)
	 */
	@Override
	public <T> boolean UpdateRoleData(T t)
	{
		if ( SystemFn.IsMemeoryData(t))
		{
			MySQLInserOper o = _GetInsertRoleIDOper(t);
			return o.Execute(t);
		}
		else
		{
			MySQLUpdateOper o = _GetUpdateRoleIDOper(t);
			return o.Execute(t, false);
		}
	}
	
	public <T> boolean UpdateRoleData(T t, boolean force)
	{
		if ( SystemFn.IsMemeoryData(t) || force )
		{
			MySQLInserOper o = _GetInsertRoleIDOper(t);
			return o.Execute(t);
		}
		else
		{
			MySQLUpdateOper o = _GetUpdateRoleIDOper(t);
			return o.Execute(t, force);
		}
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#UpdateUserIDData(java.lang.Object)
	 */
	@Override
	public <T> boolean UpdateUserIDData(T t)
	{
		MySQLUpdateOper o = _GetUpdateUserIDOper(t);
		return o.Execute(t, false);
	}
	
	/* (non-Javadoc)
	 * @see core.db.DBReader#UpdateUserNameData(java.lang.Object)
	 */
	@Override
	public <T> boolean UpdateUserNameData(T t)
	{
		MySQLUpdateOper o = _GetUpdateUserNameOper(t);
		return o.Execute(t, false);
	}
	
	public void ReleaseAll()
	{
		Iterator<Entry<String, MySQLOper>> it = m_Opers.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, MySQLOper> e = it.next();
			if ( e.getValue().IsCanRemove() )
			{
				e.getValue().Release();
				it.remove();
			}
		}
	}
	
	private <T> MemoryCreateOper _GetCreateGIDOper(T t)
	{
		return (MemoryCreateOper) _GetOper(t, eOperType.CREATE_ROLEGID);
	}
	
	private <T> MemoryCreateOper _GetCreateRoleIDOper(T t)
	{
		return (MemoryCreateOper) _GetOper(t, eOperType.CREATE_ROLEID);
	}
	
	private <T> MySQLCreateOper _GetCreateUserIDOper(T t)
	{
		return (MySQLCreateOper) _GetOper(t, eOperType.CREATE_USERID);
	}
	
	private <T> MySQLCreateOper _GetCreateUserNameOper(T t)
	{
		return (MySQLCreateOper) _GetOper(t, eOperType.CREATE_USERNAME);
	}
	
	private <T> MySQLExecuteOper _GetExecuteSQLOper(String sql)
	{
		String exkey = eOperType.EXECUTE_SQL.toString();
		String key = exkey + sql;
		if ( m_Opers.containsKey(key) )
		{
			return (MySQLExecuteOper) m_Opers.get(key);
		}
		MySQLExecuteOper o = new MySQLExecuteOper(m_Connection, sql);
		m_Opers.put(key, o);
		return o;
	}
	
	private <T> MySQLInserOper _GetInsertRoleIDOper(T t)
	{
		return (MySQLInserOper) _GetOper(t, eOperType.INSERT_ROLEID);
	}
	
	private <T> MySQLOper _GetOper(T t, eOperType type)
	{
		String exkey = type.toString();
		Class<?> c = t.getClass();
		String key = c.getName() + exkey;
		if ( m_Opers.containsKey(key) )
		{
			return m_Opers.get(key);
		}
		MySQLOper o = type.GetOper(m_Connection, c);
		m_Opers.put(key, o);
		return o;
	}
	
	private <T> MySQLReadAllOper _GetReadAllOper(T t)
	{
		return (MySQLReadAllOper) _GetOper(t, eOperType.READ_ALL);
	}
	
	private <T> MySQLReadByBaseRoleIDOper _GetReadRoleBaseRoleOper(T t)
	{
		return (MySQLReadByBaseRoleIDOper) _GetOper(t, eOperType.READ_ROLEGIDBYBASE);
	}
	
	@SuppressWarnings("unused")
	private <T> MySQLReadOper _GetReadRoleGIDOper(T t)
	{
		return (MySQLReadOper) _GetOper(t, eOperType.READ_ROLEGID);
	}
	
	private <T> MySQLReadOper _GetReadRoleNickOper(T t)
	{
		return (MySQLReadOper) _GetOper(t, eOperType.READ_ROLENICK);
	}
	
	private <T> MySQLReadSQLOper _GetReadSQLOper(T t, String sql)
	{
		String exkey = eOperType.READ_SQL.toString();
		Class<?> c = t.getClass();
		String key = c.getName() + exkey + sql;
		if ( m_Opers.containsKey(key) )
		{
			return (MySQLReadSQLOper) m_Opers.get(key);
		}
		MySQLReadSQLOper o = new MySQLReadSQLOper(m_Connection, c, "*", sql);
		m_Opers.put(key, o);
		return o;
	}
	
	private <T,T1> MySQLReadTransSQLOper _GetReadTransSQLOper(T t, T1 t1, String sql)
	{
		String exkey = eOperType.READ_TRANS_SQL.toString();
		Class<?> c = t.getClass();
		Class<?> c1 = t1.getClass();
		String key = c.getName() + c1.getName() + exkey + sql;
		if ( m_Opers.containsKey(key) )
		{
			return (MySQLReadTransSQLOper) m_Opers.get(key);
		}
		MySQLReadTransSQLOper o = new MySQLReadTransSQLOper(m_Connection, c, c1, "*", sql);
		m_Opers.put(key, o);
		return o;
	}
	
	private <T> MySQLReadOper _GetReadUserIDOper(T t)
	{
		return (MySQLReadOper) _GetOper(t, eOperType.READ_USERID);
	}
	
	private <T> MySQLReadOper _GetReadUserNameOper(T t)
	{
		return (MySQLReadOper) _GetOper(t, eOperType.READ_USERNAME);
	}

	private <T> MySQLUpdateAllOper _GetUpdateAllOper(T[] t)
	{
		return (MySQLUpdateAllOper) _GetOper(t[0], eOperType.UPDATA_ALL);
	}

	private <T> MySQLUpdateOper _GetUpdateRoleIDOper(T t)
	{
		String exkey = eOperType.UPDATE_ROLEID.toString();
		Class<?> c = t.getClass();
		String key = c.getName() + exkey;
		if ( m_Opers.containsKey(key) )
		{
			return (MySQLUpdateOper) m_Opers.get(key);
		}
		eFieldKey k = eFieldKey.RoleID;
		for ( Field f : c.getFields() )
		{
			if ( f.getName().equals(eFieldKey.GID.GetName()) )
			{
				k = eFieldKey.GID;
				break;
			}
		}
		MySQLUpdateOper o = new MySQLUpdateOper(m_Connection, t.getClass(), k.GetName());
		m_Opers.put(key, o);
		return o;
	}

	private <T> MySQLUpdateOper _GetUpdateUserIDOper(T t)
	{
		return (MySQLUpdateOper) _GetOper(t, eOperType.UPDATE_USERID);
	}

	private <T> MySQLUpdateOper _GetUpdateUserNameOper(T t)
	{
		return (MySQLUpdateOper) _GetOper(t, eOperType.UPDATE_USERNAME);
	}
}
