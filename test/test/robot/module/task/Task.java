/**
 * Task.java 2012-7-12上午11:34:39
 */
package test.robot.module.task;

import static org.junit.Assert.*;

import org.junit.Test;

import test.robot.*;
import test.robot.module.login.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Task
{

	@Test
	public void Refresh() throws Exception
	{
		Robot r = Robot.GetARobot();
		Login.Normal(r, "abc", "123");
		
		RFCFn.Task_Refresh(r, 0);
		assertEquals( r.m_TaskData.GetRefreshRes(), true);
	}
}
