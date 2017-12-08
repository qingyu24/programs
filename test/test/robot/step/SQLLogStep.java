/**
 * SQLLogStep.java 2013-3-11下午3:33:00
 */
package test.robot.step;

import test.robot.RFCFnTest;
import test.robot.Robot;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SQLLogStep extends Step
{
	private int m_Max = 0;
	private int m_Num = 0;
	private long m_RunTime = 0;
	
	public SQLLogStep(int num)
	{
		m_Max = num;
	}
	
	/* (non-Javadoc)
	 * @see test.robot.step.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		return new SQLLogStep(m_Max);
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
			RFCFnTest.Login_SQLLog(r);
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
