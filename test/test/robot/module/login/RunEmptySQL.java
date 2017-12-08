/**
 * RunEmptySQL.java 2012-8-10下午3:03:31
 */
package test.robot.module.login;

import org.junit.BeforeClass;
import org.junit.Test;

import test.robot.*;
import test.robot.step.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RunEmptySQL
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		new RobotConfig();
	}

	@Test
	public void test()
	{
		TimeBatchRun r = new TimeBatchRun(10*60*1000, 1000, 10*60*1000);
		r.SetStep(new EmptySQLRunStep(1));
		r.run();
	}

}
