package test.robot.module.chat;

import org.junit.BeforeClass;
import org.junit.Test;

import test.robot.*;
import test.robot.step.*;

public class ChatWithAnother 
{
	private int m_nTestRobotNum = 2;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		new RobotConfig();
	}

	@Test
	public void test()
	{
		TimeBatchRun r = new TimeBatchRun(60*60*1000, m_nTestRobotNum, 20*1000);
		OrderStep os = new OrderStep();
		os.Add(new LoginNormalStep());
		os.Add(new AutoCreateRoleStep());
		os.Add(new CheckReceiveEnterWorldStep());
		os.Add(new ChatAnotherStep(m_nTestRobotNum));
		r.SetStep(os);
		r.run();
	}

}
