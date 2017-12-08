package test.robot.step;

import test.robot.RFCFn;
import test.robot.Robot;
import test.robot.value.ChatRes;

public class ChatAnotherStep extends Step 
{
	private int m_nPlayerNum = 2;
	private boolean m_Execute;
	private String m_SendInfo = "我发送了消息";
	
	public ChatAnotherStep(int p_nNUM)
	{
		m_nPlayerNum =  p_nNUM;
	}
	@Override
	public Step Clone()
	{		
		return new ChatAnotherStep(m_nPlayerNum);
	}

	@Override
	public boolean Execute(Robot r) throws InterruptedException
	{
		if ( !m_Execute )
		{
			m_Execute = true;
			
			String sendInfo = "机器人"+r.GetID()+"说: "+ m_SendInfo;
			RFCFn.ChatImple_ChatWithMulti(r, (short)1, sendInfo);
		}
		return m_Execute;
	}

	@Override
	public boolean Executed(Robot r) throws Exception 
	{
		if ( m_nPlayerNum == r.m_ChatData.m_resList.size())
		{
			for ( ChatRes c : r.m_ChatData.m_resList )
			{
				String s = c.GetInfo();
				if ( c.IsSet() && c.GetChannelID() == 1 && s.contains(m_SendInfo) )
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean Result(Robot r) throws InterruptedException
	{		
		return true;
	}

}
