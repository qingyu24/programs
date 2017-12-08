/**
 * NoSecurityProtocal.java 2013-4-3下午2:35:06
 */
package core.detail.impl.socket.protocol;

import java.nio.ByteBuffer;
import java.util.Random;

import utility.Bit;
import utility.Debug;
import utility.Rand;

import core.Root;
import core.detail.impl.socket.SendMsgBuffer;
import core.detail.impl.socket.TransBuffer;
import core.detail.interface_.Link;
import core.detail.interface_.Protocol;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class NoSecurityProtocal implements Protocol
{
	private static byte[] m_TotalSecurityBuffer = null;
	private static SendMsgBuffer m_Buffer = new SendMsgBuffer(Short.MAX_VALUE - 1);
	private static Random m_Random = new Random();
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Protocol#Create(core.detail.interface_.Link)
	 */
	@Override
	public void Create(Link p_Link)
	{
		int funid = hashCode() % 4 + 2;
		byte[] buffer = _CreateNoSecurityBuffer(funid);
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
		return buffer;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Protocol#Resolve(core.detail.impl.socket.TransBuffer, core.detail.interface_.Link)
	 */
	@Override
	public boolean Resolve(TransBuffer buffer, Link p_Link) throws Exception
	{
		return true;
	}

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param funid
	 * @return
	 */
	private byte[] _CreateNoSecurityBuffer(int funid)
	{
		byte[] total = _GetTotalBuffer();
		
		int startpos = total.length + 2 + Rand.GetIn100();
		
		byte posbyte0 = Bit.GetIntByIndex(startpos, 0);
		byte posbyte1 = Bit.GetIntByIndex(startpos, 1);
		byte posbyte2 = Bit.GetIntByIndex(startpos, 2);
		byte posbyte3 = Bit.GetIntByIndex(startpos, 3);
		
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
		
		buffer[setloc0] = posbyte0;
		buffer[setloc1] = posbyte1;
		buffer[setloc2] = posbyte2;
		buffer[setloc3] = posbyte3;
		
		buffer[setloc0+1] = (byte) m_Random.nextInt();
		buffer[setloc1+1] = (byte) m_Random.nextInt();
		buffer[setloc2+1] = (byte) m_Random.nextInt();
		buffer[setloc3+1] = (byte) m_Random.nextInt();
		
		return buffer;
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

}
