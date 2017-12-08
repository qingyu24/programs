/**
 * RoleRes.java 2012-7-31下午5:59:32
 */
package test.robot.value;

import test.robot.Robot;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RoleRes
{
	private Robot m_Robot;
	private boolean m_Set;
	private long m_RoleID;
	private int m_RoleTemplateID;
	
	public RoleRes(Robot r)
	{
		m_Robot = r;
	}
	
	public boolean IsSet()
	{
		return m_Set;
	}
	
	public long RoleID() throws InterruptedException
	{
		if ( !m_Set )
		{
			m_Robot.Wait();
		}
		return m_RoleID;
	}
	
	public int RoleTemplateID() throws InterruptedException
	{
		if ( !m_Set )
		{
			m_Robot.Wait();
		}
		return m_RoleTemplateID;
	}
	
	public void Set(long roleID, int roleTemplateID)
	{
		m_Set = true;
		m_RoleID = roleID;
		m_RoleTemplateID = roleTemplateID;
		m_Robot.Notify();
	}
	
	public void Init()
	{
		m_Set = false;
		m_RoleID = 0;
		m_RoleTemplateID = 0;
	}
}
