/**
 * ResEcho.java 2012-8-9下午2:58:06
 */
package test.robot.value;

import test.robot.Robot;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ResEcho
{
	private Robot m_Robot;
	private boolean m_Set;
	private int m_Hashcode;
	private String m_Info;
	
	public ResEcho(Robot r)
	{
		m_Robot = r;
	}
	
	public boolean IsSet()
	{
		return m_Set;
	}
	
	public int GetHashcode() throws InterruptedException
	{
		if ( !m_Set )
		{
			m_Robot.Wait();
		}
		return m_Hashcode;
	}
	
	public String GetInfo() throws InterruptedException
	{
		if ( !m_Set )
		{
			m_Robot.Wait();
		}
		return m_Info;
	}
	
	public void Set(int hashcode, String info)
	{
		m_Set = true;
		m_Hashcode = hashcode;
		m_Info = info;
		m_Robot.Notify();
	}
	
	public void Init()
	{
		m_Set = false;
		m_Hashcode = 0;
		m_Info = "";
	}
}
