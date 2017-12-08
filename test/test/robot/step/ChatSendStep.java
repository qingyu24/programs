/**
 * ChatSendStep.java 2012-8-9下午6:33:00
 */
package test.robot.step;

import java.util.*;

import test.robot.*;
import test.robot.value.*;
import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ChatSendStep extends Step
{
	private static final int m_Space = 0;
	private static final int m_MaxSendNum = 2000; ///<同时发送的最大数量
	private long m_LastSend = -1;
	private Set<Integer> m_AllSend = new HashSet<Integer>();
	private int m_SendHashcode = 0;
	private int m_SendLength = 1;
	private static long m_SendNum = 0;
	
	public ChatSendStep(int asklength)
	{
		m_SendLength = asklength;
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		return new ChatSendStep(m_SendLength);
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Execute(test.robot.Robot)
	 */
	@Override
	public boolean Execute(Robot r) throws InterruptedException
	{
		long currtm = System.currentTimeMillis();
		if ( currtm - m_LastSend > m_Space && m_AllSend.size() < m_MaxSendNum )
		{
			m_LastSend = currtm;
			RFCFnTest.Login_EchoSend(r, ++m_SendHashcode, m_SendLength);
			m_AllSend.add(m_SendHashcode-1);
			
			m_SendNum++;
			
			if ( m_SendNum % 10000 == 0 )
			{
				System.out.println("发送消息[" + m_SendNum + "]" + Debug.GetCurrTime());
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
		if ( !r.m_LoginData.m_EchoSends.isEmpty() )
		{
			for ( ResEcho res : r.m_LoginData.m_EchoSends )
			{
				int v = res.GetHashcode();
				Debug.Assert(res.GetInfo().length() == m_SendLength, "接收到聊天的信息长度等于发送的长度");
				m_AllSend.remove(v);
			}
			r.m_LoginData.m_EchoSends.clear();
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
