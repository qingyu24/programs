/**
 * BoolRes.java 2012-7-31下午4:06:55
 */
package test.robot.value;

import test.robot.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class BoolRes
{
	private Robot m_Robot;
	private boolean m_Set;
	private boolean m_Res;
	
	public BoolRes(Robot r)
	{
		m_Robot = r;
	}
	
	public boolean IsSet()
	{
		return m_Set;
	}
	
	public boolean Result() throws InterruptedException
	{
		if ( !m_Set )
		{
			m_Robot.Wait();
		}
		return m_Res;
	}
	
	public void Set(boolean v)
	{
		m_Set = true;
		m_Res = v;
		m_Robot.Notify();
	}
	
	public void Init()
	{
		m_Set = false;
		m_Res = false;
	}
}
