/**
 * DBReader.java 2012-6-28上午11:26:42
 */
package core.db;

import core.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface DBReader
{
	public void Init();

	public <T> T ReadUserData(String p_username, T t);
	
	public <T> T ReadUserData(int p_userid, T t);
	
	public <T> T[] ReadRoleGIDData(long p_lGid, T t);
	
	public <T> T[] ReadRoleIDData(long p_roleId, T t);
	
	public <T> T ReadRoleNickData(String p_sNick, T t);
	
	public <T> T CreateUserData(String p_username, T t);
	
	public <T> T CreateUserData(int p_userid, T t);
	
	public <T> T CreateRoleData(long p_lGid, T t, boolean c);
	
	public <T> T CreateRoleDataByGID(T t, boolean c);
	
	public <T> boolean UpdateUserNameData(T t);
	
	public <T> boolean UpdateUserIDData(T t);
	
	public <T> boolean UpdateRoleData(T t);
	
	public <T> T[] ReadAllData(T t, boolean c);
	
	public <T> void UpdateAllData(T[] t);
	
	public <T> T[] ReadSQL(T t, String sql);
	
	public <T,T1> T[] ReadTransSQL(T t, T1 t1, String sql);
	
	public <T,T1> T TransSQL(T t, T1 t1);

	public void AddDBLoader(DBLoader l);

	public void LoadAll();

	public void SaveAll();

	public boolean ExecuteSQL(String sql);

	public boolean SaveLog(String sql);

	public void Commit();

	public void Update();
}
