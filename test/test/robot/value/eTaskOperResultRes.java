/**
 * eTaskOperResultRes.java 2012-8-2上午11:40:08
 */
package test.robot.value;

import logic.userdata.task.eTaskOperResult;
import test.robot.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class eTaskOperResultRes
{
	private Robot m_Robot;
	private boolean m_Set;
	private short m_TaskID;
	private eTaskOperResult m_Res;
	
	public eTaskOperResultRes(Robot r)
	{
		m_Robot = r;
	}
	
	public boolean IsSet()
	{
		return m_Set;
	}
	
	public eTaskOperResult GetOper() throws InterruptedException
	{
		if ( !m_Set )
		{
			m_Robot.Wait();
		}
		return m_Res;
	}
	
	public short GetTaskID() throws InterruptedException
	{
		if ( !m_Set )
		{
			m_Robot.Wait();
		}
		return m_TaskID;
	}
	
	public void Set(eTaskOperResult oper, short taskid)
	{
		m_Set = true;
		m_Res = oper;
		m_TaskID = taskid;
		m_Robot.Notify();
	}
	
	public void Init()
	{
		m_Set = false;
		m_Res = eTaskOperResult.EMPTY;
	}
}
