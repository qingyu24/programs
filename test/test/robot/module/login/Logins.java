/**
 * Logins.java 2012-7-30上午10:29:02
 */
package test.robot.module.login;

import org.junit.Test;

import test.robot.*;
import test.robot.step.LoginNormalStep;
import test.robot.utility.NameRule;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 正常登陆
 */
public class Logins
{
	@Test
	public void Normal() throws Exception
	{
		FreeRun r = new FreeRun(10*60*1000, 20, NameRule.randomRule, NameRule.randomRule);
		r.SetStep(new LoginNormalStep());
		r.run();
	}
}
