/**
 * Tasks.java 2012-8-2下午6:47:22
 */
package test.robot.module.task;


//import java.io.PrintStream;

import org.junit.BeforeClass;
import org.junit.Test;

import test.robot.*;
import test.robot.step.*;
import test.robot.utility.NameRule;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Tasks
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		new RobotConfig();
//		System.setOut(new PrintStream("robot.txt"));
	}

	@Test
	public void AutoFinishAllTask()
	{
//		FreeRun r = new FreeRun(40*60*60*1000, 1, NameRule.randomRule, NameRule.randomRule);
		TimeBatchRun r = new TimeBatchRun(40*60*60*1000, 1, 300 * 1000, NameRule.normalRule, NameRule.normalRule); 
		r.SetMaxRunNum(10);
		OrderStep os = new OrderStep();
		os.Add(new LoginNormalStep());
		os.Add(new AutoCreateRoleStep());
		os.Add(new CheckReceiveEnterWorldStep());
		os.Add(new AutoFinishAllTaskStep());
		r.SetStep(os);
		r.run();
	}
}
