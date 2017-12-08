/**
 * DBMgr.java 2012-6-24����10:12:56
 */
package core;

import utility.*;
import core.db.*;
import core.db.mysql.*;

/**
 * @author ddoq
 * @version 1.0.0
 * 
 */
public class DBMgr
{
	/**
	 * ��ʼ���������õȵ�
	 */
	public static void Init()
	{
		GetReader().Init();
	}
	
	/**
	 * �������е�DBLoader
	 */
	public static void LoadAll()
	{
		GetReader().LoadAll();
	}
	
	/**
	 * ����
	 */
	public static void Update()
	{
		GetReader().Update();
	}
	
	/**
	 * ���һ��DBLoader
	 */
	public static void AddDBLoader(DBLoader l)
	{
		Debug.Assert(l != null, "����l������Ϊ��");
		GetReader().AddDBLoader(l);
	}
	
	public static void Commit()
	{
		GetReader().Commit();
	}
	
	/////////////////////////////////////////////////////////////////////////////
	/**
	 * �õ�����������ɫid������
	 */
	public static RoleIDUniqueID GetCreateRoleUniqueID()
	{
		return new RoleIDUniqueID();
	}
	
	/////////////////////////////////////////////////////////////////////////////
	
	public static <T> T CreateGlobalRoleData(long p_lGid, T t)
	{
		Debug.Assert(t != null, "����t������Ϊ��");
		return GetReader().CreateRoleData(p_lGid, t, true);
	}
	
	public static <T> T CreateGlobalRoleDataByGID(T t)
	{
		Debug.Assert(t != null, "����t������Ϊ��");
		return GetReader().CreateRoleDataByGID(t, true);
	}
	
	/////////////////////////////////////////////////////////////////////////////
	
	/**
	 * ����һ���Խ�ɫidΪ������"�ڴ�����"
	 * 
	 * <br>���Դ���:{@link TestDBMgr#TestRoleData}
	 */
	public static <T> T CreateRoleData(long p_lGid, T t)
	{
		return GetReader().CreateRoleData(p_lGid, t, false);
	}
	
	/////////////////////////////////////////////////////////////////////////////
	
	/**
	 * ִ��һ��sql���
	 * 
	 * <br>���Դ���:{@link TestDBMgr#setUp}
	 * <br>���Դ���:{@link TestDBMgr_GlobalData#TestGloabalRef}
	 * 
	 * @return ����true��ʾ�ɹ�
	 */
	public static <T> boolean ExecuteSQL(String sql)
	{
		return GetReader().ExecuteSQL(sql);
	}
	
	/**
	 * ��ȡ���е�����
	 * 
	 * <br>���Դ���:{@link TestDBMgr_GlobalData#TestGloabalRef}
	 * 
	 * @param t
	 *            Ҫ��ȡ����������
	 * @return ��ȡ���ļ�¼,���᷵��nullָ��,û������Ҳ�Ƿ���һ������Ϊ0�����ݼ�
	 */
	public static <T> T[] ReadAllData(T t)
	{
		Debug.Assert(t != null, "����t������Ϊ��");
		return GetReader().ReadAllData(t, false);
	}
	
	public static <T> T[] ReadAllDataAddStore(T t)
	{
		Debug.Assert(t != null, "����t������Ϊ��");
		return GetReader().ReadAllData(t, true);
	}
	
	/**
	 * ���ݽ�ɫid����ȡ��ɫ����
	 *
	 * <br>���Դ���:{@link TestDBMgr#TestRoleData}
	 */
	public static <T> T[] ReadRoleData(long p_lGid, T t)
	{
		return GetReader().ReadRoleGIDData(p_lGid, t);
	}
	
	public static <T> T[] ReadRoleIDData(long p_roleId, T t)
	{
		return GetReader().ReadRoleIDData(p_roleId, t);
	}
	
	public static <T> T ReadRoleData(String p_Nick, T t)
	{
		Debug.Assert(t != null, "����t������Ϊ��");
		return GetReader().ReadRoleNickData(p_Nick, t);
	}
	
	/**
	 * ����sql�������ȡ����
	 * 
	 * <br>���Դ���:{@link TestDBMgr_GlobalData#TestGloabalRef}
	 * 
	 * @param t
	 *            Ҫ��ȡ�Ľṹ
	 * @param sql
	 *            ʹ�õ�sql���
	 * @return ��ȡ�������ݼ�,���᷵��nullָ��,û������Ҳ�Ƿ���һ������Ϊ0�����ݼ�
	 */
	public static <T> T[] ReadSQL(T t, String sql)
	{
		Debug.Assert(t != null, "����t������Ϊ��");
		return GetReader().ReadSQL(t, sql);
	}
	
	/**
	 * ��tת����t1�ṹ, t1��t���ֻ�����������ϵ�����
	 */
	public static <T,T1> T TransSQL(T t, T1 t1)
	{
		Debug.Assert(t != null && t1 != null, "����t,t1������Ϊ��");
		return GetReader().TransSQL(t, t1);
	}

	/**
	 * �����ɫ����
	 *
	 * <br>���Դ���:{@link TestDBMgr#TestRoleData}
	 */
	public static <T> boolean UpdateRoleData(T t)
	{
		if ( t != null )
		{
			return GetReader().UpdateRoleData(t);
		}
		return false;
	}

	/**
	 * �����û�id�������û�����
	 * 
	 * @deprecated
	 *
	 * <br>���Դ���:{@link TestDBMgr#TestUserIDData}
	 */
	public static <T> boolean UpdateUserIDData(T t)
	{
		if ( t != null )
		{
			return GetReader().UpdateUserIDData(t);
		}
		return false;
	}
	
	/**
	 * �����û����������û�����
	 *
	 * <br>���Դ���:{@link TestDBMgr#TestUserNameData}
	 */
	public static <T> boolean UpdateUserNameData(T t)
	{
		if ( t != null )
		{
			return GetReader().UpdateUserNameData(t);
		}
		return false;
	}
	
	/**
	 * ����һ���Խ�ɫidΪ������"�ڴ�����"
	 * 
	 * @deprecated
	 * 
	 * <br>���Դ���:{@link TestDBMgr#TestGID}
	 */
	public static <T> T CreateRoleDataByGID(T t)
	{
		Debug.Assert(t != null, "����t������Ϊ��");
		return GetReader().CreateRoleDataByGID(t, false);
	}
	
	/**
	 * ����sql�������ȡ���ݣ���t1�Ľṹ����ȡ������t�����ݽṹ��t1��t���ֻ�����������ϵ�����
	 * 
	 * <br>���Դ���:{@link TestDBMgr_TransRead#Test}
	 * 
	 * @deprecated
	 * 
	 * @param t
	 *            �����Ľṹ����
	 * @param t1
	 *            ��ȡ�Ľṹ����
	 * @param sql
	 *            ʹ�õ�sql���
	 * @return ��ȡ�������ݼ�,���᷵��nullָ��,û������Ҳ�Ƿ���һ������Ϊ0�����ݼ�
	 */
	public static <T,T1> T[] ReadTransSQL(T t,T1 t1, String sql)
	{
		Debug.Assert(t != null && t1 != null, "����t,t1������Ϊ��");
		return GetReader().ReadTransSQL(t, t1, sql);
	}
	
	/**
	 * �����û�id����ȡ����
	 * 
	 * @deprecated
	 *
	 * <br>���Դ���:{@link TestDBMgr#TestUserIDData}
	 */
	public static <T> T ReadUserData(int p_userid, T t)
	{
		Debug.Assert(t != null, "����t������Ϊ��");
		return GetReader().ReadUserData(p_userid, t);
	}
	
	/**
	 * ��ȡ����
	 * 
	 * @deprecated
	 * 
	 * <br>���Դ���:{@link TestDBMgr#TestUserNameData}
	 * 
	 * @param p_username
	 *            �û���
	 * @param t
	 *            Ҫ��ȡ����������
	 * @return ��ȡ���ļ�¼,���᷵��nullָ��,û������Ҳ�Ƿ���һ������Ϊ0�����ݼ�
	 */
	public static <T> T ReadUserData(String p_username, T t)
	{
		Debug.Assert(t != null, "����t������Ϊ��");
		return GetReader().ReadUserData(p_username, t);
	}
	
	/**
	 * �������û�idΪ�������û�����,ֱ��д�����ݿ�
	 * 
	 * @deprecated
	 * 
	 * <br>���Դ���:{@link TestDBMgr#TestUserIDData}
	 */
	public static <T> T CreateUserData(int p_userid, T t)
	{
		Debug.Assert(t != null, "����t������Ϊ��");
		return GetReader().CreateUserData(p_userid, t);
	}
	
	/**
	 * �������û���Ϊ�������û�����,ֱ��д�����ݿ�
	 * 
	 * @deprecated
	 * 
	 * <br>���Դ���:{@link TestDBMgr#TestUserNameData}
	 */
	public static <T> T CreateUserData(String p_sUserName, T t)
	{
		Debug.Assert(t != null, "����t������Ϊ��");
		return GetReader().CreateUserData(p_sUserName, t);
	}
	
	/**
	 * @deprecated
	 */
	public static boolean Fail()
	{
		return false;
	}
	
	/**
	 * @deprecated
	 */
	public static boolean Success()
	{
		return true;
	}
	

	/**
	 * �����������е�����
	 * 
	 * <br>���Դ���:{@link TestDBMgr_GlobalData#TestGloabalRef}������SaveAll���ڲ������������
	 * 
	 * @param t
	 *            Ҫ���������
	 *            
	 *  @deprecated
	 */
	public static <T> void UpdateAllData(T[] t)
	{
		if ( t != null && t.length != 0 )
		{
			GetReader().UpdateAllData(t);
		}
	}
	
	private static DBReader GetReader()
	{
		return MySQLDBReader.GetInstance();
	}
}
