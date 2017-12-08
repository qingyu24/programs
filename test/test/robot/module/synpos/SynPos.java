/**
 * SynPos.java 2012-12-24上午11:17:16
 */
package test.robot.module.synpos;

import org.junit.*;

import test.robot.*;
import test.robot.step.*;
import test.robot.utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SynPos
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		new RobotConfig();
	}

	@Test
	public void AutoMove()
	{
		FreeRun r = new FreeRun(2*60*60*1000, 50, NameRule.randomRule, NameRule.randomRule);
		Step s = RFAutoMoveStep();
		r.SetStep(s);
		r.run();
	}

	@RF ( Name = "随机自动移动测试" )
	public static Step RFAutoMoveStep()
	{
		OrderStep os = new OrderStep();
		os.Add(new LoginNormalStep());
		os.Add(new AutoCreateRoleStep());
		os.Add(new CheckReceiveEnterWorldStep());
//		os.Add(new AutoMoveStep(5000));
		os.Add(new NoticeAllStep(1000));
		return os;
	}
}
