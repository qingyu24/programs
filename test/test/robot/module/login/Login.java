/**
 * Login.java 2012-7-11下午1:58:37
 */
package test.robot.module.login;

import static org.junit.Assert.*;

import org.junit.Test;

import test.robot.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Login
{
	@Test
	public void TestEnter() throws Exception
	{
		Normal(Robot.GetARobot(), "abc", "123");
	}
	
	public static void Normal(Robot r, String p_sUserName,String p_sPassword) throws Exception
	{
		RFCFn.Login_Enter(r, p_sUserName, p_sPassword, 0, 3);
		assertEquals( r.m_LoginData.m_LoginRes.Result(), true);
	}
}