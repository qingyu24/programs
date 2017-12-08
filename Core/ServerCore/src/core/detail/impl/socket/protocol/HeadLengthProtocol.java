/**
 * HeadLengthProtocol.java 2012-7-9下午3:07:11
 */
package core.detail.impl.socket.protocol;

import java.nio.*;

import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 * 
 */
public class HeadLengthProtocol implements Protocol
{
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
		return true;
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
		int end = buffer.GetEndPos();
		if (end == 0)
		{
			return false;
		}
		int start = buffer.GetStartPos();
		if (start + 2 > end)
		{
			return false;
		}

		if (buffer.GetBufferSize() < 2)
		{
			return false;
		}
		ByteBuffer b = buffer.GetBuffer();
		short length = b.getShort(start);
		if (length <= 0)
		{
			throw new Exception("# 读取的长度有误[" + length + "]");
		}
		if (start + 2 + length > end)
		{
			return false;
		}
		buffer.UseBufferSize(start + 2,length);
		return true;
	}
}
