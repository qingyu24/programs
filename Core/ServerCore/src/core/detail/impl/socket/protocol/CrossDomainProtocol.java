/**
 * CrossDomainProtocol.java 2012-7-9下午4:14:59
 */
package core.detail.impl.socket.protocol;

import java.nio.ByteBuffer;

import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 * 
 * @deprecated
 */
public class CrossDomainProtocol implements Protocol
{
	private boolean				m_Init			= false;
	private static byte[]		m_ProtocolHead	=
												{ '<', 'p', 'o', 'l', 'i', 'c',
			'y', '-', 'f', 'i', 'l', 'e', '-', 'r', 'e', 'q', 'u', 'e', 's',
			't', '/', '>', 0					};
	private static byte[]		m_Bytes			= new byte[22];
	private static String		m_SendBuffer	= "<?xml version='1.0'?>\r\n<cross-domain-policy>\r\n<allow-access-from domain=\"*\" to-ports=\"*\" />\r\n</cross-domain-policy>";
	private static ByteBuffer	m_SendByteBuffer	= ByteBuffer
														.wrap(m_SendBuffer.getBytes());

	/* (non-Javadoc)
	 * @see core.detail.interface_.Protocol#Create(core.detail.interface_.Link)
	 */
	@Override
	public void Create(Link p_Link)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see core.detail.interface_.Protocol#Init()
	 */
	@Override
	public boolean Init()
	{
		return m_Init;
	}

	/*
	 * (non-Javadoc)
	 * @see core.detail.interface_.Protocol#Pack(java.nio.ByteBuffer)
	 */
	@Override
	public ByteBuffer Pack(ByteBuffer buffer)
	{
		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * core.detail.interface_.Protocol#Resolve(core.detail.impl.socket.TransBuffer
	 * )
	 */
	@Override
	public boolean Resolve(TransBuffer buffer, Link p_Link) throws Exception
	{
		if (m_Init)
		{
			return true;
		}
		if (buffer.GetBufferSize() < m_ProtocolHead.length)
		{
			return false;
		}
		ByteBuffer b = buffer.GetBuffer();
		int startpos = buffer.GetStartPos();
		for (int i = 0; i < m_Bytes.length; ++i)
		{
			m_Bytes[i] = b.get(i + startpos);
		}
		for (int i = 0; i < m_Bytes.length; ++i)
		{
			if (m_Bytes[i] != m_ProtocolHead[i])
			{
				throw new Exception("# 跨域协议头错误,内容不匹配");
			}
		}
		m_Init = true;
		buffer.UseBufferSize(startpos, 23);

		p_Link.SendBuffer(m_SendByteBuffer);
		return true;
	}

}
