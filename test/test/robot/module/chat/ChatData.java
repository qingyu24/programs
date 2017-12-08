package test.robot.module.chat;

import java.util.concurrent.ConcurrentLinkedQueue;

import test.robot.ResData;
import test.robot.Robot;
import test.robot.value.ChatRes;

public class ChatData implements ResData
{
	public ConcurrentLinkedQueue<ChatRes> m_resList = new ConcurrentLinkedQueue<ChatRes>();
	private Robot m_robot ;
	
	
	public ChatData(Robot robot)
	{
		m_robot = robot;
	}
	
	public void SetInfo(short nchannle ,String p_info)
	{
		ChatRes r = new ChatRes(m_robot);
		r.Set(nchannle, p_info);
		m_resList.add(r);		
	}

	public void Reset() 
	{
		m_resList.clear();
	}

}
