/**
 * RobotLink.java 2012-7-11下午4:33:50
 */
package test.robot;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.SocketChannel;

import utility.*;

import core.detail.*;
import core.detail.impl.mgr.methodmgr.MethodType;
import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RobotLink implements Link
{
	private Robot m_Robot;
	private boolean m_Close = false;
	private Socket m_Socket;
	private ByteBuffer m_Buffer = ByteBuffer.allocate(1024);
	private int m_Start = 0;
	private int m_End = 0;
	private byte[] m_ReadBuffer = new byte[10240];
	private int m_ReadLength = 0;
	
	public RobotLink(Robot r)
	{
		m_Robot = r;
	}
	
	public void LinkToServer()
	{
		try
		{
			m_Socket = new Socket(RobotConfig.Host, RobotConfig.Port);
			m_Socket.setReuseAddress(true);
			m_Close = false;
			m_Buffer.clear();
			m_Start = 0;
			m_End = 0;
			m_ReadLength = 0;
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			m_Close = true;
		}
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#OnRead()
	 */
	@Override
	public synchronized boolean OnRead()
	{
		if ( IsDisConnected() )
		{
			return false;
		}
		try
		{
			InputStream ius = m_Socket.getInputStream();
			DataInputStream dis = new DataInputStream(ius);
			
			int sz = dis.available();
			if ( sz != 0 )
			{
//				System.out.println("# Read:" + sz + " m_ReadLength:" + m_ReadLength + " [" + m_Robot.GetID() + "]" + " " +  this);
				try
				{
					dis.readFully(m_ReadBuffer, m_ReadLength, sz);
				}
				catch(Exception e)
				{
					Debug.Assert(false, "Read sz:" + sz + " m_ReadLength:" + m_ReadLength);
				}
				m_ReadLength += sz;
				_ParseReadBuffer();
			}
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			Close(0, 0);
		}
		return false;
	}
	
	private void _ParseReadBuffer() throws Exception
	{
		ByteBuffer b = ByteBuffer.wrap(m_ReadBuffer);
		//2个字节的长度 1个字节为objid 1个字节为methodid
		int use = 0;
		while ( m_ReadLength - use >= 4 )
		{
			b.position(use);
//			System.out.println("# Parse position:" + use);
			short length = b.getShort();
			if ( use + length + 2 > m_ReadLength )
			{
//				byte classid = 0;
//				byte methodid = 0;
//				if ( length >=2 )
//				{
//					classid = b.get(use+3);
//					methodid = b.get(use+4);
//				}
//				System.out.println("# Parse[" + m_ReadLength + "] msg:[" + length + "][" + classid + "][" + methodid + "] use[" + use + "] " + this);
//				System.out.println( Arrays.toString(m_ReadBuffer) );
				break;
			}
			use += 2;
			MsgBuffer buffer = new MsgBuffer(b, use, length);
			byte classid = buffer.GetByte();
			byte methodid = buffer.GetByte();
//			System.out.println("# Parse[" + m_ReadLength + "] msg:[" + length + "][" + classid + "][" + methodid + "] use[" + use + "]" + this);
			
			MethodEx mex = GetMethod(classid, methodid);
			if ( mex != null )
			{
				mex.ParseMsg(buffer, m_Robot);
				if ( !buffer.Check() )
				{
					Debug.Assert(false, "# 解析buffer的长度出错 使用的长度["+buffer.GetUseLength()+"] 应该使用的长度[" + buffer.GetLength() + "][" + classid + "," + methodid + "]" + this);
				}
				mex.RunDirect();
			}
			use += length;
		}
		
		if ( use != 0 )
		{
			if ( use == m_ReadLength )
			{
				m_ReadLength = 0;
			}
			else
			{
				for ( int i = use, j = 0; i < m_ReadLength; ++i,++j )
				{
					m_ReadBuffer[j] = m_ReadBuffer[i];
				}
//				System.out.println( Arrays.toString(m_ReadBuffer) );
//				System.out.println("Move ReadBuffer m_ReadLength[" + m_ReadLength + "] [use:" + use + "] " +  this);
				m_ReadLength -= use;
//				System.out.println("Move ReadBuffer Finish m_ReadLength[" + m_ReadLength + "] " +  this);
				Debug.Assert(m_ReadLength >= 0, "");
			}
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#Close()
	 */
	@Override
	public void Close(int reason, int ex)
	{
		if ( !m_Close )
		{
			m_Close = true;
//			System.out.println("#Close[" + this + "]");
			if ( m_Socket != null )
			{
				try
				{
					m_Socket.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					m_Socket = null;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#Send(java.nio.ByteBuffer)
	 */
	@Override
	public synchronized void Send(ByteBuffer p_Buffer)
	{
//		System.out.println("SendBuffer:");
//		for ( int i = 0; i < p_Buffer.limit(); ++i )
//		{
//			System.out.printf("%ox", p_Buffer.get(i));
//		}
//		System.out.println();
		m_End += p_Buffer.limit();
		m_Buffer.limit(m_End);
		m_Buffer.put(p_Buffer);
		m_Start += p_Buffer.limit();
		m_Buffer.position(m_Start);
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#SendBuffer(java.nio.ByteBuffer)
	 */
	@Override
	public void SendBuffer(ByteBuffer p_Buffer)
	{
		assert(false);
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#IsDisConnected()
	 */
	@Override
	public boolean IsDisConnected()
	{
		return m_Close;
	}


	public synchronized void OnSend()
	{
		if ( IsDisConnected() )
		{
			return;
		}
		if ( m_End != 0 )
		{
			try
			{
				OutputStream ous = m_Socket.getOutputStream();
				byte[] bs = m_Buffer.array();
//				System.out.println("OnSend:");
//				for ( int i = 0; i < m_End; ++i )
//				{
//					System.out.printf("%ox", bs[i]);
//				}
//				System.out.println();
				ous.write(bs, 0, m_End );
//				System.out.println("# Send:" + m_End);
				m_Buffer.clear();
				m_Start = 0;
				m_End = 0;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Close(0, 0);
			}
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#CheckLink(int)
	 */
	@Override
	public boolean CheckLink(int maxtm)
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#GetIP()
	 */
	@Override
	public String GetIP()
	{
		if ( m_Socket != null )
		{
			return m_Socket.getInetAddress().getHostAddress();
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#Clone(core.detail.interface_.Listen, java.nio.channels.SocketChannel)
	 */
	@Override
	public Link Clone(Listen listen, SocketChannel sc)
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#GetUserHashCode()
	 */
	@Override
	public int GetUserHashCode()
	{
		return 0;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#GetMethod(byte, byte)
	 */
	@Override
	public MethodEx GetMethod(byte classid, byte methodid)
	{
		return Mgr.GetRunMethodMgr(MethodType.NORMAL).GetMethod(classid, methodid);
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#_CanCloseImmediately()
	 */
	@Override
	public boolean _CanCloseImmediately()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#_CloseImmediately()
	 */
	@Override
	public void _CloseImmediately()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#_CheckLink()
	 */
	@Override
	public void _CheckLink()
	{
		// TODO Auto-generated method stub
		
	}

}
