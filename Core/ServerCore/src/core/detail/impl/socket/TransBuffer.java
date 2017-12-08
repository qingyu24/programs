/**
 * TransBuffer.java 2012-7-9下午3:18:39
 */
package core.detail.impl.socket;

import java.nio.ByteBuffer;

/**
 * @author ddoq
 * @version 1.0.0
 * 
 */
public class TransBuffer
{
	private ByteBuffer	m_Buffer;
	private int			m_Start;
	private int			m_End;
	private ReceiveBuffer m_ReceiveBuffer;
	
	public ByteBuffer GetBuffer()
	{
		return m_Buffer;
	}

	public int GetBufferSize()
	{
		return m_End - m_Start;
	}

	public int GetEndPos()
	{
		return m_End;
	}

	public int GetStartPos()
	{
		return m_Start;
	}

	public void Init(ReceiveBuffer p_ReceiveBuffer)
	{
		m_Buffer = p_ReceiveBuffer.GetBuffer();
		m_Start = p_ReceiveBuffer.GetStartPos();
		m_End = p_ReceiveBuffer.GetEndPos();
		m_ReceiveBuffer = p_ReceiveBuffer;
	}

	public MsgBuffer ToMsgBuffer()
	{
		return new MsgBuffer(m_Buffer, m_Start, m_ReceiveBuffer.GetPreEnd() - m_Start);
	}
	
	public void UseBufferSize(int startpos, int sz)
	{
		m_Start = startpos;
		m_ReceiveBuffer.SetPreEnd(m_Start+sz);
	}
	
	public int GetPreEndPos()
	{
		return m_ReceiveBuffer.GetPreEnd();
	}

}
