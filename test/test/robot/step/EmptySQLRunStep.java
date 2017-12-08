/**
 * EmptySQLRunStep.java 2012-8-10下午3:06:38
 */
package test.robot.step;

import test.robot.*;
import test.robot.Robot;
import test.robot.value.Res;
import utility.Debug;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class EmptySQLRunStep extends Step
{
	private int m_RunNum = 1;
	private boolean m_Execute = false;
	private int m_Hashcode = 0;
	
	public EmptySQLRunStep(int runnum)
	{
		m_RunNum = runnum;
		m_Hashcode = hashCode();
	}
	
	/* (non-Javadoc)
	 * @see test.robot.step.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		return new EmptySQLRunStep(m_RunNum);
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Execute(test.robot.Robot)
	 */
	@Override
	public boolean Execute(Robot r) throws InterruptedException
	{
		if ( !m_Execute )
		{
			m_Execute = true;
			RFCFnTest.Login_RunEmptySQL(r, m_Hashcode++, m_RunNum);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Executed(test.robot.Robot)
	 */
	@Override
	public boolean Executed(Robot r) throws Exception
	{
		if ( !m_Execute )
		{
			return false;
		}
		Res<Integer> res = r.m_LoginData.m_RunEmptySQLRes;
		if ( res.IsSet() )
		{
			Debug.Assert(res.Result() == (m_Hashcode - 1), "hash值对比必定相同");
			res.Init(0);
			m_Execute = false;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Result(test.robot.Robot)
	 */
	@Override
	public boolean Result(Robot r) throws InterruptedException
	{
		return false;
	}

}
