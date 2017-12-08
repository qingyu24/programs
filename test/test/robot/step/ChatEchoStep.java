/**
 * ChatEchoStep.java 2012-8-9下午6:43:19
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
public class ChatEchoStep extends Step
{
	private static final byte[] m_SendBytes = new byte[10240];
	private static final int m_Space = 0;
	private static final int m_MaxSendNum = 2000; ///<同时发送的最大数量
	private long m_LastSend = -1;
	private Map<Integer,String> m_AllSend = new HashMap<Integer,String>();
	private int m_SendHashcode = 0;
	private int m_SendLength = 1;
	private static long m_SendNum = 0;
	
	public ChatEchoStep(int sendbytelength)
	{
		m_SendLength = sendbytelength;
	}
	
	/* (non-Javadoc)
	 * @see test.robot.step.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		return new ChatEchoStep(m_SendLength);
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
			String s = new String(m_SendBytes, 0, m_SendLength);
			RFCFnTest.Login_Echo(r, m_SendHashcode++, s);
			m_AllSend.put(m_SendHashcode, s);
			m_SendNum++;
			
			if ( m_SendNum % 10000 == 0 )
			{
				System.out.println("发送消息[" + m_SendNum + "]" + Debug.GetCurrTime() + " r.m_LoginData.m_Echos:" + r.m_LoginData.m_Echos.size() + " m_AllSend:" + m_AllSend.size());
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
		if ( !r.m_LoginData.m_Echos.isEmpty() )
		{
			for ( ResEcho res : r.m_LoginData.m_Echos )
			{
				int v = res.GetHashcode();
				if ( m_AllSend.containsKey(v) )
				{
					String s = res.GetInfo();
					String s1 = res.GetInfo();
					Debug.Assert(s.length() == s1.length(), "?");
					m_AllSend.remove(v);
				}
			}
			r.m_LoginData.m_Echos.clear();
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
