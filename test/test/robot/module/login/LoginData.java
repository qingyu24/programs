/**
 * LoginData.java 2012-7-11下午4:41:00
 */
package test.robot.module.login;

import java.util.concurrent.*;

import test.robot.*;
import test.robot.value.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LoginData implements ResData
{
	private Robot m_Robot;
	public Res<Integer> m_LoginRes = null;
	public RoleRes m_NoRleRes = null;
	public RoleRes m_UseRoleRes = null;
	public Res<Boolean> m_CreateRoleRes = null;
	public ConcurrentLinkedQueue<Res<Integer>> m_EchoReceives = new ConcurrentLinkedQueue<Res<Integer>>();
	public ConcurrentLinkedQueue<ResEcho> m_EchoSends = new ConcurrentLinkedQueue<ResEcho>();
	public ConcurrentLinkedQueue<ResEcho> m_Echos = new ConcurrentLinkedQueue<ResEcho>();
	public Res<Integer> m_RunEmptySQLRes = null;
	public Res<Integer> m_ReadSQLRes = null;
	public Res<Integer> m_SaveSQLRes = null;
	
	public LoginData(Robot r)
	{
		m_Robot = r;
		m_LoginRes = new Res<Integer>(r);
		m_NoRleRes = new RoleRes(r);
		m_UseRoleRes = new RoleRes(r);
		m_CreateRoleRes = new Res<Boolean>(r);
		m_RunEmptySQLRes = new Res<Integer>(r);
		m_ReadSQLRes = new Res<Integer>(r);
		m_SaveSQLRes = new Res<Integer>(r);
		Reset();
	}
	
	public void Reset()
	{
		m_LoginRes.Init(0);
		m_NoRleRes.Init();
		m_UseRoleRes.Init();
		m_CreateRoleRes.Init(false);
		m_EchoReceives.clear();
		m_EchoSends.clear();
		m_Echos.clear();
		m_RunEmptySQLRes.Init(0);
		m_ReadSQLRes.Init(0);
		m_SaveSQLRes.Init(0);
	}

	public void SetLoginRes(int res)
	{
		m_LoginRes.Set(res);
	}
	
	public void SetNoRole(long roleid, int roletemplateid)
	{
		m_NoRleRes.Set(roleid, roletemplateid);
	}
	
	public void SetCreateRole(int res)
	{
		m_CreateRoleRes.Set(res == 1 ? true : false);
	}
	
	public void SetEnterWorld(long roleid, int roletemplateid)
	{
		m_UseRoleRes.Set(roleid, roletemplateid);
	}
	
	public void SetEchoRecieveRes(int hashcode)
	{
		Res<Integer> r = new Res<Integer>(m_Robot);
		r.Set(hashcode);
		m_EchoReceives.add(r);
	}
	
	public void SetEchoSendRes(int hashcode, String p_info)
	{
		ResEcho e = new ResEcho(m_Robot);
		e.Set(hashcode, p_info);
		m_EchoSends.add(e);
	}

	public void SetEchoRes(int hashcode, String p_info)
	{
		ResEcho e = new ResEcho(m_Robot);
		e.Set(hashcode, p_info);
		m_Echos.add(e);
	}

	public void SetRunEmptySQLRes(int hashcode)
	{
		m_RunEmptySQLRes.Set(hashcode);
	}

	public void SetReadSQLRes(int hashcode)
	{
		m_ReadSQLRes.Set(hashcode);
	}

	public void SetSaveSQLRes(int hashcode)
	{
		m_SaveSQLRes.Set(hashcode);
	}
}
