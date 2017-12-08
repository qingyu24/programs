/**
 * Logins2.java 2012-8-6下午3:58:03
 */
package test.robot.module.login;

import org.junit.BeforeClass;
import org.junit.Test;

import test.robot.*;
import test.robot.step.*;
import test.robot.utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Logins2
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		new RobotConfig();
	}
	
	@Test
	public void Normal()
	{
//		TimeBatchRun r = new TimeBatchRun(60*60*1000, 500, 20*1000, NameRule.randomRule, NameRule.randomRule);
		FreeRun r = new FreeRun(10*60*1000, 1, NameRule.randomRule, NameRule.randomRule);
		OrderStep os = new OrderStep();
		os.Add(new LoginNormalStep());
		os.Add(new AutoCreateRoleStep());
		os.Add(new CheckReceiveEnterWorldStep());
		r.SetStep(os);
		r.run();
	}
}
