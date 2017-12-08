/**
 * Logins1.java 2012-7-31下午6:23:51
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
 * 正常登陆,如果没有创建角色则创建角色
 */
@RC ( Name = "登陆" )
public class Logins1
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		new RobotConfig();
	}
	
	@Test
	public void Normal()
	{
		TimeBatchRun r = new TimeBatchRun(3*60*60*1000, 100, 120*1000, IncreaseRule.randomRule, IncreaseRule.randomRule);
		r.SetMaxRunNum(100);
		Step os = RFNormalStep();
		r.SetStep(os);
		r.run();
	}
	
	@RF ( Name = "正常登陆测试" )
	public static Step RFNormalStep()
	{
		OrderStep os = new OrderStep();
		os.Add(new LoginNormalStep());
//		os.Add(new AutoCreateRoleStep());
//		os.Add(new CheckReceiveEnterWorldStep());
//		os.Add(new AutoMoveStep(20));
		return os;
	}
}
