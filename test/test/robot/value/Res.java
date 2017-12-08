/**
 * ResBase.java 2012-8-2下午1:43:06
 */
package test.robot.value;

import test.robot.Robot;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Res<T>
{
	private Robot m_Robot;
	private boolean m_Set;
	private T m_Res;
	
	public Res(Robot r)
	{
		m_Robot = r;
	}
	
	public boolean IsSet()
	{
		return m_Set;
	}
	
	public T Result() throws InterruptedException
	{
		if ( !m_Set )
		{
			m_Robot.Wait();
		}
		return m_Res;
	}
	
	public void Set(T v)
	{
		m_Set = true;
		m_Res = v;
		m_Robot.Notify();
	}
	
	public void Init(T v)
	{
		m_Set = false;
		m_Res = v;
	}
}
