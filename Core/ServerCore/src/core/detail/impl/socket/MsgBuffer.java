/**
 * MsgBuffer.java 2012-6-13下午11:24:08
 */
package core.detail.impl.socket;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MsgBuffer
{
	private static String CHARSET = "UTF-8";
	private ByteBuffer 	m_Buffer;
	private int			m_Start;
	private int			m_Length;
	private int			m_Use;
	
	/**
	 * @param m_Buffer
	 * @param m_Start
	 * @param length
	 */
	public MsgBuffer(ByteBuffer buffer, int start, int length)
	{
		m_Buffer = buffer;
		m_Start = start;
		m_Length = length;
		m_Use = 0;
	}
	
	public int GetUseLength()
	{
		return m_Use;
	}
	
	public int GetLength()
	{
		return m_Length;
	}
	
	public byte GetByte()
	{
		byte b = m_Buffer.get(m_Start);
		_IncPos(1);
		return b;
	}
	
	public boolean GetBoolean()
	{
		byte b = GetByte();
		return b == 0x01;
	}
	
	public char GetChar()
	{
		char c = m_Buffer.getChar(m_Start);
		_IncPos(2);
		return c;
	}
	
	public short GetShort()
	{
		short s = m_Buffer.getShort(m_Start);
		_IncPos(2);
		return s;
	}
	
	public int GetInt()
	{
		int i = m_Buffer.getInt(m_Start);
		_IncPos(4);
		return i;
	}
	
	public long GetLong()
	{
		long l = m_Buffer.getLong(m_Start);
		_IncPos(8);
		return l;
	}
	
	public float GetFloat()
	{
		float f = m_Buffer.getFloat(m_Start);
		_IncPos(4);
		return f;
	}
	
	public double GetDouble()
	{
		double d = m_Buffer.getDouble(m_Start);
		_IncPos(8);
		return d;
	}
	
	public String GetString()
	{
		short s = GetShort();
		byte[] bs = new byte[s];
		for ( int i = 0; i < s; ++i )
		{
			bs[i] = m_Buffer.get(m_Start+i);
		}
		_IncPos(s);
		try
		{
			return new String(bs, CHARSET);
		}
		catch (UnsupportedEncodingException e)
		{
			return new String(bs);
		}
	}
	
	public boolean Check()
	{
		if ( m_Use != m_Length )
		{
			return false;
		}
		return true;
	}
	
	public void Use(int length)
	{
		_IncPos(length);
	}

	private void _IncPos(int length)
	{
		m_Start += length;
		m_Use += length;
	}
}
