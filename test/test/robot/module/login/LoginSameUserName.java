/**
 * LoginSameUserName.java 2012-10-15上午10:20:43
 */
package test.robot.module.login;

import org.junit.*;

import test.robot.*;
import test.robot.step.*;
import test.robot.utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 用来测试2个机器人使用同一个账号登陆的情况
 */
public class LoginSameUserName
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		new RobotConfig();
	}

	@Test
	public void test()
	{
		FreeRun r = new FreeRun(60*60*1000, 2, NameRule.solidRule, NameRule.randomRule);
		OrderStep os = new OrderStep();
		os.Add(new LoginNormalStep());
		r.SetStep(os);
		r.run();
	}

}
