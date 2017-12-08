/**
 * SecurityProtocal.java 2013-3-19上午10:37:21
 */
package core.detail.impl.socket.protocol;

import java.nio.ByteBuffer;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import utility.*;

import core.*;
import core.detail.impl.socket.SendMsgBuffer;
import core.detail.impl.socket.TransBuffer;
import core.detail.interface_.Link;
import core.detail.interface_.Protocol;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SecurityProtocal implements Protocol
{
	private static byte[] m_TotalSecurityBuffer = null;
	private static SendMsgBuffer m_Buffer = new SendMsgBuffer(Short.MAX_VALUE - 1);
	private static Random m_Random = new Random();
	
	private byte[] m_SecurityBuffer = new byte[8];
	//下个加密buffer的启用
	private byte[] m_NextSBuffer = new byte[8];
	@SuppressWarnings("unused")
	private long m_NextSBufferMaxTime = -1;
	
	private byte[] m_TempBuffer = new byte[256];
	private byte m_LastIndex = 0;
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Protocol#Create(core.detail.interface_.Link)
	 */
	@Override
	public void Create(Link p_Link)
	{
		int funid = hashCode() % 4 + 2;
		byte[] buffer = _CreateSecurityBuffer(funid);
		m_Buffer.AddID(37, funid).Add(buffer).Send(p_Link);
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Protocol#Init()
	 */
	@Override
	public boolean Init()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Protocol#Pack(java.nio.ByteBuffer)
	 */
	@Override
	public ByteBuffer Pack(ByteBuffer buffer)
	{
		//返回的消息没加密
		return buffer;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Protocol#Resolve(core.detail.impl.socket.TransBuffer, core.detail.interface_.Link)
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
		if (start + length > end)
		{
			return false;
		}
		
		int bufferlength = length - 2;
		int newTempBufferLength = m_TempBuffer.length;
		while(bufferlength >= newTempBufferLength)
			newTempBufferLength *= 2;
		m_TempBuffer = new byte[newTempBufferLength];
//		if ( bufferlength >= m_TempBuffer.length )
//		{
//			m_TempBuffer = new byte[m_TempBuffer.length * 2];
//		}
		
		for ( int i = 0; i < bufferlength; ++i )
		{
			m_TempBuffer[i] = b.get(start + 2 + i);
		}
		
		//解密
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(m_SecurityBuffer);
		SecretKey sKey = SecretKeyFactory.getInstance("DES").generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(m_SecurityBuffer);
		cipher.init(Cipher.DECRYPT_MODE, sKey, iv);
		byte[] normalMsg = cipher.doFinal(m_TempBuffer, 0, bufferlength);
		
		//把解密的内容重新塞回网络的buffer,用倒序是为了保证不止一条消息时,不破坏后面的消息,塞完后重设buffer起始位置
		for ( int i = 0; i < normalMsg.length; i++ )
		{
			b.put(start + length -1 - i, normalMsg[normalMsg.length-1-i]);
		}
		
		if ( normalMsg[0] == m_LastIndex )
		{
			throw new Exception("# 使用重复发包工具,踢掉");
		}
		m_LastIndex = normalMsg[0];
//		System.err.println("m_LastIndex:" + m_LastIndex);
		buffer.UseBufferSize(start + length - normalMsg.length + 1, normalMsg.length - 1);
		
		return true;
	}

	private byte[] _GetTotalBuffer()
	{
		if ( m_TotalSecurityBuffer == null )
		{
			m_TotalSecurityBuffer = Root.GetInstance().GetFactory().GetEncryptBuffer();
			Debug.Assert(m_TotalSecurityBuffer.length > 1024, "加密的缓存要大于1024,现在是:" + m_TotalSecurityBuffer.length);
		}
		return m_TotalSecurityBuffer;
	}
	
	private byte[] _CreateSecurityBuffer(int funid)
	{
		byte[] total = _GetTotalBuffer();
		int startpos = hashCode() % total.length - 8;
		if ( startpos < 2 )
		{
			startpos = Rand.Get(234) + 2;
		}
		Debug.Assert(startpos < total.length - 8, "加密buffer的起始位置[" + startpos + "]必须小于总buffer[" + (total.length - 8) + "]");
		
		//把起始位置打散放置在4个地方
		byte posbyte0 = Bit.GetIntByIndex(startpos, 0);
		byte posbyte1 = Bit.GetIntByIndex(startpos, 1);
		byte posbyte2 = Bit.GetIntByIndex(startpos, 2);
		byte posbyte3 = Bit.GetIntByIndex(startpos, 3);
		
//		System.err.println("startpos:" + startpos + "[" + posbyte0 + "," + posbyte1 + "," + posbyte2 + "," + posbyte3 + " maxbuffer:" + total.length);
		int setloc0 = funid + 1;
		int setloc1 = setloc0 + funid;
		int setloc2 = setloc1 + funid;
		int setloc3 = setloc2 + funid;
		int size = 128 + Rand.GetIn100();
		if ( setloc3 >= size )
		{
			size = setloc3 + 1;
		}
		
		byte[] buffer = new byte[size];
		m_Random.nextBytes(buffer);
		//设置起始位置数据
		buffer[setloc0] = posbyte0;
		buffer[setloc1] = posbyte1;
		buffer[setloc2] = posbyte2;
		buffer[setloc3] = posbyte3;
		
		buffer[setloc0+1] = total[startpos];
		buffer[setloc1+1] = total[startpos+1];
		buffer[setloc2+1] = total[startpos+2];
		buffer[setloc3+1] = total[startpos+3];
		
		if ( funid <= 5 )
		{
			for ( int i = 0; i < m_SecurityBuffer.length; ++i )
			{
				m_SecurityBuffer[i] = total[startpos+i];
			}
//			System.err.println("funid:" + funid + "," + Arrays.toString(m_SecurityBuffer));
		}
		else
		{
			for ( int i = 0; i < m_NextSBuffer.length; ++i )
			{
				m_NextSBuffer[i] = total[startpos+i];
			}
			m_NextSBufferMaxTime = System.currentTimeMillis() + 5 * 60 * 1000;
		}
//		System.err.println(Arrays.toString(buffer));
		return buffer;
	}
}
