/**
 * ReceiveBuffer.java 2012-6-13下午11:09:32
 */
package core.detail.impl.socket;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ReceiveBuffer
{
	private static int INC_LENGTH = 1024;
	private ByteBuffer 	m_Buffer;
	private int			m_Start;	///<已经读取的buffer开始的位置
	private int			m_End;		///<已经读取的buffer结束的位置
	private int			m_PreEnd;	///<预先解析到的结尾处
	
	public ReceiveBuffer(int p_size)
	{
		m_Start = 0;
		m_End = 0;
		m_Buffer = ByteBuffer.allocate(p_size);
	}

	/**
	 * 把网络读取到的数据存入自己的buffer
	 * 
	 * @return 本次读取的长度
	 * @throws IOException 
	 */
	public int Read(SocketChannel sc) throws IOException
	{
		int count = sc.read(m_Buffer);
		if ( count > 0 )
		{
			m_End += count;
			
			if ( m_End == m_Buffer.capacity() )
			{
				ByteBuffer buffer = ByteBuffer.allocate(m_Buffer.capacity()+INC_LENGTH);
				byte[] bs = m_Buffer.array();
				buffer.put(bs);
				m_Buffer = buffer;
			}
		}
		return count;
	}

	/**
	 * 真正使用了那段buffer
	 */
	public void Use()
	{
		m_Start = m_PreEnd;
		//正好把buffer都读取完了
		if ( m_Start == m_End )
		{
			m_Start = 0;
			m_End = 0;
			m_Buffer.position(0);
		}
	}

	/**
	 * 设置解析到的位置
	 */
	public void SetPreEnd(int end)
	{
		m_PreEnd = end;
		Debug.Assert( m_PreEnd <= m_End, "" );
	}

	public int GetEndPos()
	{
		return m_End;
	}

	public int GetStartPos()
	{
		return m_Start;
	}

	public ByteBuffer GetBuffer()
	{
		return m_Buffer;
	}

	public int GetPreEnd()
	{
		return m_PreEnd;
	}
}
