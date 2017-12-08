/**
 * NoticeAllStep.java 2013-2-17下午5:47:19
 */
package test.robot.step;

import test.robot.RFCFn;
import test.robot.Robot;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 给所有人发通知
 */
public class NoticeAllStep extends Step
{
	private int m_CurrNum = 0;
	private int m_MaxNum = 0;
	private long m_LastSendTime = 0;
	
	public NoticeAllStep(int max)
	{
		m_CurrNum = 0;
		m_MaxNum = max;
	}
	/* (non-Javadoc)
	 * @see test.robot.step.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		return new NoticeAllStep(m_MaxNum);
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Execute(test.robot.Robot)
	 */
	@Override
	public boolean Execute(Robot r) throws InterruptedException
	{
		if ( System.currentTimeMillis() - m_LastSendTime > 300 )
		{
			m_LastSendTime = System.currentTimeMillis();
			RFCFn.ChatImple_ChatWithMulti(r, (short)1, "ChatExecuteNum:" + m_CurrNum + "," + r.hashCode());
			m_CurrNum++;
			
			if (m_CurrNum >= m_MaxNum)
			{
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Executed(test.robot.Robot)
	 */
	@Override
	public boolean Executed(Robot r) throws Exception
	{
		return m_CurrNum >= m_MaxNum;
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
