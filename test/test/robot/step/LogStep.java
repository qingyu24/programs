/**
 * LogStep.java 2013-3-11下午2:22:21
 */
package test.robot.step;

import test.robot.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LogStep extends Step
{
	private int m_Max = 0;
	private int m_Num = 0;
	private long m_RunTime = 0;
	
	public LogStep(int num)
	{
		m_Max = num;
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		return new LogStep(m_Max);
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Execute(test.robot.Robot)
	 */
	@Override
	public boolean Execute(Robot r) throws InterruptedException
	{
		if ( m_Num >= m_Max )
		{
			return true;
		}
		if ( System.currentTimeMillis() - m_RunTime > 1000 )
		{
			RFCFnTest.Login_Log(r, "阿斯顿福建大房间冻死了咖啡吉萨剪短发了空间的烧录卡 我是机器人Robot,运行次数" + m_Num);
			m_RunTime = System.currentTimeMillis();
			m_Num++;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Executed(test.robot.Robot)
	 */
	@Override
	public boolean Executed(Robot r) throws Exception
	{
		return m_Num >= m_Max;
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Result(test.robot.Robot)
	 */
	@Override
	public boolean Result(Robot r) throws InterruptedException
	{
		return true;
	}

}
