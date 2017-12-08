/**
 * SendMsgBuffer.java 2012-6-26下午4:05:07
 */
package core.detail.impl.socket;

import java.io.*;
import java.nio.*;
import java.util.*;

import utility.*;

import core.*;
import core.detail.impl.OnlineUserSelector;
import core.detail.impl.log.*;
import core.detail.interface_.*;
import core.ex.ShowMgr;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SendMsgBuffer
{
	private static String CHARSET = "UTF-8";
	private ByteBuffer 			m_Buffer;
	private int					m_CurrPos;
	private ArrayList<byte[]>	m_Stores = new ArrayList<byte[]>();
	private Queue<Long> 		m_BusyFlag = new LinkedList<Long>();
	
	private static long m_ProcessNum;
	private static long m_RecordTime = System.currentTimeMillis();
	private static long m_ProcessNumTotal;
	
	public SendMsgBuffer(int p_size)
	{
		Debug.Assert( p_size <= Short.MAX_VALUE, "" );
		m_Buffer = ByteBuffer.allocate(p_size);
		_Init();
	}

	public SendMsgBuffer Clear()
	{
		_Init();
		return this;
	}

	public SendMsgBuffer AddID(int p_ObjID, int p_MethodID)
	{
		Clear();
		Debug.Assert(p_ObjID <= Byte.MAX_VALUE && p_MethodID <= Byte.MAX_VALUE, "");
		Add((byte)p_ObjID);
		Add((byte)p_MethodID);
		return this;
	}

	public SendMsgBuffer Add(boolean p_data)
	{
		_CheckSize(1);
		m_Buffer.put( m_CurrPos, (byte) (p_data ? 0x01 : 0x0) );
		_AddSize(1);
		return this;
	}
	
	public SendMsgBuffer Add(byte p_data)
	{
		_CheckSize(1);
		m_Buffer.put( m_CurrPos, p_data);
		_AddSize(1);
		return this;
	}
	
	public SendMsgBuffer Add(short p_data)
	{
		_CheckSize(2);
		m_Buffer.putShort(m_CurrPos, p_data);
		_AddSize(2);
		return this;
	}
	
	public SendMsgBuffer Add(int p_data)
	{
		_CheckSize(4);
		m_Buffer.putInt(m_CurrPos, p_data);
		_AddSize(4);
		return this;
	}
	
	public SendMsgBuffer Add(long p_data)
	{
		_CheckSize(8);
		m_Buffer.putLong(m_CurrPos, p_data);
		_AddSize(8);
		return this;
	}
	
	public SendMsgBuffer Add(float p_data)
	{
		_CheckSize(4);
		m_Buffer.putFloat(m_CurrPos, p_data);
		_AddSize(4);
		return this;
	}
	
	public SendMsgBuffer Add(double p_data)
	{
		_CheckSize(8);
		m_Buffer.putDouble(m_CurrPos, p_data);
		_AddSize(8);
		return this;
	}
	
	public SendMsgBuffer Add(String p_data)
	{
		byte[] bs = null;
		try 
		{
			bs = p_data.getBytes(CHARSET);
		} 
		catch (UnsupportedEncodingException e) 
		{
			Log.out.LogException(e);
		}
		return Add(bs);
	}
	
	public SendMsgBuffer Add(byte[] p_data)
	{
		if ( p_data == null )
		{
			Add((short)0);
			return this;
		}
		Debug.Assert(p_data.length <= Short.MAX_VALUE, "");
		short sz = (short)p_data.length;
		Add(sz);
		
		_CheckSize(sz);
		for ( int i = 0,j = m_CurrPos; i < sz; ++i,++j )
		{
			m_Buffer.put(j, p_data[i]);
		}
		_AddSize(sz);
		return this;
	}
	
	public SendMsgBuffer Add(SerialData p_data)
	{
		p_data.OnSerialData(this);
		return this;
	}
	
	public SendMsgBuffer AddRawBytes(byte[] p_data)
	{
		if ( p_data == null )
			return this;
		Debug.Assert(p_data.length <= Short.MAX_VALUE, "");
		short sz = (short)p_data.length;
		_CheckSize(sz);
		for ( int i = 0,j = m_CurrPos; i < sz; ++i,++j )
		{
			m_Buffer.put(j, p_data[i]);
		}
		_AddSize(sz);
		return this;
	}
	
	public <T extends SerialData> SendMsgBuffer Add(ArrayList<T> p_data)
	{
		Debug.Assert(p_data.size() <= Short.MAX_VALUE, "");
		short sz = (short) p_data.size();
		Add(sz);
		
		for ( T t : p_data)
		{
			Add(t);
		}
		return this;
	}
	
	public SendMsgBuffer Add(boolean[] p_data)
	{
		Debug.Assert(p_data.length <= Short.MAX_VALUE, "");
		short sz = (short) p_data.length;
		Add(sz);
		
		for ( boolean t : p_data)
		{
			Add(t);
		}
		return this;
	}
	
	public SendMsgBuffer Add(double[] p_data)
	{
		Debug.Assert(p_data.length <= Short.MAX_VALUE, "");
		short sz = (short) p_data.length;
		Add(sz);
		
		for ( double t : p_data)
		{
			Add(t);
		}
		return this;
	}
	
	public SendMsgBuffer Add(float[] p_data)
	{
		Debug.Assert(p_data.length <= Short.MAX_VALUE, "");
		short sz = (short) p_data.length;
		Add(sz);
		
		for ( float t : p_data)
		{
			Add(t);
		}
		return this;
	}
	
	public SendMsgBuffer Add(int[] p_data)
	{
		Debug.Assert(p_data.length <= Short.MAX_VALUE, "");
		short sz = (short) p_data.length;
		Add(sz);
		
		for ( int t : p_data)
		{
			Add(t);
		}
		return this;
	}
	
	public SendMsgBuffer Add(long[] p_data)
	{
		Debug.Assert(p_data.length <= Short.MAX_VALUE, "");
		short sz = (short) p_data.length;
		Add(sz);
		
		for ( long t : p_data)
		{
			Add(t);
		}
		return this;
	}
	
	public SendMsgBuffer Add(String[] p_data)
	{
		Debug.Assert(p_data.length <= Short.MAX_VALUE, "");
		short sz = (short) p_data.length;
		Add(sz);
		
		for ( String t : p_data)
		{
			Add(t);
		}
		return this;
	}
	
	public SendMsgBuffer Add(short[] p_data)
	{
		Debug.Assert(p_data.length <= Short.MAX_VALUE, "");
		short sz = (short) p_data.length;
		Add(sz);
		
		for ( short t : p_data)
		{
			Add(t);
		}
		return this;
	}
	
	/**
	 * 对某些用户发送消息
	 * 
	 * @param s			用户选择器
	 * @param delay		是否延迟发送
	 */
	public void SendByUserSelector(UserSelector s, boolean delay)
	{
		m_Buffer.putShort(0, (short) (m_CurrPos - 2));
		m_Buffer.limit(m_CurrPos);
		
		if ( _IsBusy() || delay )
		{
			_AddStoreBuffer();
		}
		else
		{
			ArrayList<User> all = s.GetSelectUsers();
			for ( User u : all )
			{
				m_Buffer.position(0);
				_SendMsg(u.GetLink());
			}
		}
	}
	
	/**
	 * 给所有在线的用户发送消息
	 */
	public void SendAllOnlineUser()
	{
		m_Buffer.putShort(0, (short) (m_CurrPos - 2));
		m_Buffer.limit(m_CurrPos);
		
		SendByUserSelector(OnlineUserSelector.GetInstance(), true);
		
//		ArrayList<User> all = Root.GetInstance().GetAllOnlineUser();
//		for ( User u : all )
//		{
//			m_Buffer.position(0);
//			_SendMsg(u.GetLink());
//		}
	}

	public void Send(User p_User)
	{
		m_Buffer.putShort(0, (short) (m_CurrPos - 2));
		m_Buffer.limit(m_CurrPos);
		m_Buffer.position(0);
		Link link = p_User.GetLink();
		_SendMsg(link);
	}
	
	/**
	 * 机器人用的
	 */
	public void Send(Link p_Link)
	{
		m_Buffer.putShort(0, (short) (m_CurrPos - 2));
		m_Buffer.limit(m_CurrPos);
		m_Buffer.position(0);
		p_Link.Send(m_Buffer);
	}
	
	private void _CheckSize(int p_size)
	{
		if(m_CurrPos + p_size > m_Buffer.capacity()){
			int a = 0;
		}
		String msg = "Size:" + (m_CurrPos + p_size);
		Debug.Assert( m_CurrPos + p_size < m_Buffer.capacity(), "超出最大消息体长度:" + msg + "MAX:" + m_Buffer.capacity());
	}
	
	private void _AddSize(int p_size)
	{
		m_CurrPos += p_size;
	}
	
	private void _Init()
	{
		m_Buffer.clear();
		m_CurrPos = 2;
	}
	
	private void _SendMsg(Link link)
	{
		if ( link != null )
		{
			link.Send(m_Buffer);
			
			m_ProcessNum += m_CurrPos;
			m_ProcessNumTotal += m_CurrPos;
		}

		_RefreshRecord();
	}
	
	private boolean _IsBusy()
	{
		for ( long num : m_BusyFlag )
		{
			if ( num > RootConfig.GetInstance().LimitSendMsgBuffer * 1024 )
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean _IsRelax()
	{
		if ( m_BusyFlag.size() == 0 )
		{
			return true;
		}
		boolean relax = false;
//		long rnum = 0;
		for ( long num : m_BusyFlag )
		{
			relax = num < RootConfig.GetInstance().LimitSendMsgBuffer * 1024;
//			rnum = num;
		}
//		System.err.println("Relax:" + rnum);
		return relax;
	}
	
	private void _AddStoreBuffer()
	{
		if ( m_Stores.size() > RootConfig.GetInstance().LimitSendMsgNum )
		{
			return;
		}
		
		byte[] bs = new byte[m_CurrPos];
		for ( int i = 0; i < bs.length; ++i )
		{
			bs[i] = m_Buffer.get(i);
		}
		m_Stores.add(bs);
	}

	public void OnRelaxSendBuffer() throws Exception
	{
		_RefreshRecord();

		if ( !_IsRelax() )
		{
			return;
		}
		
		long start = m_ProcessNumTotal;
		long stm = System.currentTimeMillis();
		Iterator<byte[]> it = m_Stores.iterator();
		while ( it.hasNext() )
		{
			byte[] bs = it.next();
			it.remove();
			m_Buffer.clear();
			m_Buffer.put(bs);
			m_Buffer.limit(bs.length);
			
			ArrayList<User> all = OnlineUserSelector.GetInstance().GetSelectUsers();
			for ( User u : all )
			{
				m_Buffer.position(0);
				_SendMsg(u.GetLink());
//				System.err.println("OnRelaxSendBuffer:" + u);
			}
			
			if ( System.currentTimeMillis() - stm > 100 )
			{
				break;
			}
			
			if ( m_ProcessNumTotal - start > RootConfig.GetInstance().LimitSendMsgBuffer * 1024 / 4 )
			{
				break;
			}
		}
	}
	
	private void _RefreshRecord()
	{
		long tm = System.currentTimeMillis() - m_RecordTime;
		if ( tm > 1000 )
		{
			long buffer = m_ProcessNum * 1000 / tm;
			if ( ShowMgr.CanShow() )
			{
				ShowMgr.GetInfo().setOutputBuffer(buffer);
				ShowMgr.GetInfo().setOutputBufferTotal(m_ProcessNumTotal);
				ShowMgr.GetInfo().setOutputBufferSecond(buffer);
				ShowMgr.GetInfo().setStoremsg(m_Stores.size());
			}
			
			m_BusyFlag.add(buffer);
			if ( m_BusyFlag.size() > 5 )
			{
				m_BusyFlag.poll();
			}
			
			m_RecordTime = System.currentTimeMillis();
			m_ProcessNum = 0;
		}
	}
}
