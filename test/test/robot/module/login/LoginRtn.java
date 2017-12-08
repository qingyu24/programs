/**
 * LoginRtn.java 2012-7-12上午10:13:56
 */
package test.robot.module.login;

import test.robot.Robot;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LoginRtn implements LoginRtnInterface
{
	/* (non-Javadoc)
	 * @see test.robot.module.login.LoginRtnInterface#LoginRes(test.robot.Robot, boolean)
	 */
	@Override
	public void LoginRes(Robot r, int res)
	{
//		System.out.println(" LoginRes:" + res);
		r.m_LoginData.SetLoginRes(res);
	}
	
	/* (non-Javadoc)
	 * @see test.robot.module.login.LoginRtnInterface#NoRoleRes(test.robot.Robot, long, int)
	 */
	@Override
	public void NoRoleRes(Robot r, long roleid, int roletemplateid)
	{
//		System.out.println(" NoRoleRes:" + roleid + "," + roletemplateid);
		r.m_LoginData.SetNoRole(roleid, roletemplateid);
	}
	
	/* (non-Javadoc)
	 * @see test.robot.module.login.LoginRtnInterface#CreateRoleRes(test.robot.Robot, long, int)
	 */
	@Override
	public void CreateRoleRes(Robot r, int res)
	{
//		System.out.println(" CreateRoleRes:" + roleid + "," + roletemplateid);
		r.m_LoginData.SetCreateRole(res);
	}

	/* (non-Javadoc)
	 * @see test.robot.module.login.LoginRtnInterface#EnterWorld(test.robot.Robot, long, int)
	 */
	@Override
	public void EnterWorld(Robot r, long roleid, int roletemplateid, String rolename)
	{
//		System.out.println(" EnterWorld:" + roleid + "," + roletemplateid);
		r.m_LoginData.SetEnterWorld(roleid, roletemplateid);
	}

	/* (non-Javadoc)
	 * @see test.robot.module.login.LoginRtnInterface#EchoRecieveRes(test.robot.Robot, int)
	 */
	@Override
	public void EchoRecieveRes(Robot r, int hashcode)
	{
		r.m_LoginData.SetEchoRecieveRes(hashcode);
	}

	/* (non-Javadoc)
	 * @see test.robot.module.login.LoginRtnInterface#EchoSendRes(test.robot.Robot, int, java.lang.String)
	 */
	@Override
	public void EchoSendRes(Robot r, int hashcode, String p_info)
	{
		r.m_LoginData.SetEchoSendRes(hashcode, p_info);
	}

	/* (non-Javadoc)
	 * @see test.robot.module.login.LoginRtnInterface#EchoRes(test.robot.Robot, int, java.lang.String)
	 */
	@Override
	public void EchoRes(Robot r, int hashcode, String p_info)
	{
		r.m_LoginData.SetEchoRes(hashcode, p_info);
	}

	/* (non-Javadoc)
	 * @see test.robot.module.login.LoginRtnInterface#RunEmptySQLRes(test.robot.Robot, int)
	 */
	@Override
	public void RunEmptySQLRes(Robot r, int hashcode)
	{
		r.m_LoginData.SetRunEmptySQLRes(hashcode);
	}

	/* (non-Javadoc)
	 * @see test.robot.module.login.LoginRtnInterface#ReadSQLRes(test.robot.Robot, int)
	 */
	@Override
	public void ReadSQLRes(Robot r, int hashcode)
	{
		r.m_LoginData.SetReadSQLRes(hashcode);
	}

	/* (non-Javadoc)
	 * @see test.robot.module.login.LoginRtnInterface#SaveSQLRes(test.robot.Robot, int)
	 */
	@Override
	public void SaveSQLRes(Robot r, int hashcode)
	{
		r.m_LoginData.SetSaveSQLRes(hashcode);
	}

}
