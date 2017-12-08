package test.robot.value;

import test.robot.Robot;

public class ChatRes 
{
	private Robot m_Robot;
	private boolean m_Set;
	private short m_ChannelID;
	private String m_Info;
	
	public ChatRes(Robot r)
	{
		m_Robot = r;
	}
	
	public boolean IsSet()
	{
		return m_Set;
	}
	
	public short GetChannelID() throws InterruptedException
	{
		if ( !m_Set )
		{
			m_Robot.Wait();
		}
		return m_ChannelID;
	}
	
	public String GetInfo() throws InterruptedException
	{
		if ( !m_Set )
		{
			m_Robot.Wait();
		}
		return m_Info;
	}
	
	public void Set(short channelID, String info)
	{
		m_Set = true;
		m_ChannelID = channelID;
		m_Info = info;
		m_Robot.Notify();
	}
	
	public void Init()
	{
		m_Set = false;
		m_ChannelID = -1;
		m_Info = "";
	}
}
