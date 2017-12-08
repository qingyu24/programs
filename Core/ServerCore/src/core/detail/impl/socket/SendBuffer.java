/**
 * SendBuffer.java 2013-2-18上午11:06:14
 */
package core.detail.impl.socket;

import java.io.IOException;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.Map.Entry;

import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SendBuffer
{
	public static class Data
	{
		public ByteBuffer		m_Data;
		public SocketChannel 	m_Sc;
	}
	
	private static SendBuffer m_Instance = new SendBuffer();
	private static SendBuffer m_GMInstance = new SendBuffer();
	
	public static SendBuffer GetNormal()
	{
		return m_Instance;
	}
	public static SendBuffer GetGM()
	{
		return m_GMInstance;
	}
	
	private List<Data> 			m_Datas = new LinkedList<Data>();
	private Map<SocketChannel, List<Data>> m_Sends = new HashMap<SocketChannel, List<Data>>();
	
	private Selector m_Selector;
	
	SendBuffer()
	{
	}
	
	public void AppendBuffer(ByteBuffer buffer, SocketChannel sc)
	{
		Data d = new Data();
		byte[] bs = new byte[buffer.limit()];
		buffer.get(bs);
		d.m_Data = ByteBuffer.wrap(bs);
		d.m_Sc = sc;
		
		long stm = System.currentTimeMillis();
		synchronized (m_Datas)
		{
			m_Datas.add(d);
//			System.out.println("==================add:" + d.m_Data.limit());
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "SendBuffer::_GetQueue用时[[[:" + (System.currentTimeMillis() - stm));
		}
//		System.out.println("ZZZZZZZZZZZZZZZZZZ wakeup");
		m_Selector.wakeup();
	}
	
	public void AppendBufferToAll(ByteBuffer buffer)
	{
		AppendBuffer(buffer, null);
	}
	
	public boolean HaveAnySendMsg(SocketChannel sc)
	{
		if (sc == null)
		{
			return false;
		}
		long stm = System.currentTimeMillis();
		synchronized (m_Datas)
		{
			Iterator<Data> it = m_Datas.iterator();
			while (it.hasNext())
			{
				if (it.next().m_Sc == sc)
				{
					return true;
				}
			}
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "SendBuffer::HaveAnySendMsg用时[[[:" + (System.currentTimeMillis() - stm));
		}
		
		return m_Sends.containsKey(sc) && !m_Sends.get(sc).isEmpty();
	}
	

	public void ProcessWriteBuffer(Selector s)
	{
		m_Selector = s;
		long stm = System.currentTimeMillis();
		synchronized (m_Datas)
		{
			for ( Data d : m_Datas )
			{
				if ( d.m_Sc != null )
				{
					_ProcessData(d, s);
				}
				else
				{
				}
			}
			m_Datas.clear();
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "SendBuffer::HaveAnySendMsg用时[[[:" + (System.currentTimeMillis() - stm));
		}
		
		stm = System.currentTimeMillis();
		Iterator<Entry<SocketChannel, List<Data>>> it = m_Sends.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<SocketChannel, List<Data>> e = it.next();
			if ( e.getValue().isEmpty() )
			{
				it.remove();
			}
			if ( System.currentTimeMillis() - stm > 50 )
			{
				break;
			}
		}
	}

	/**
	 * 实际发送网络数据
	 * 
	 * @return 返回true表示已经发送完数据,可以准备读取了
	 */
	public boolean Write(SocketChannel sc)
	{
		if ( sc.isOpen() == false )
		{
			m_Sends.remove(sc);
			return false;
		}
		
		try
		{
			List<Data> ds = m_Sends.get(sc);
			if ( ds == null )
			{
				return true;
			}
			while (!ds.isEmpty())
			{
				Data d = ds.get(0);
				sc.write(d.m_Data);
				if (d.m_Data.remaining() > 0)
				{
					break;
				}
				ds.remove(0);
			}
			
			if (ds.isEmpty())
			{
				m_Datas.remove(sc);
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (IOException e)
		{
			m_Sends.remove(sc);
			return false;
		}
	}

	private boolean _AddSend(Data d)
	{
		if (!m_Sends.containsKey(d.m_Sc))
		{
			m_Sends.put(d.m_Sc, new ArrayList<Data>());
		}
		List<Data> ds = m_Sends.get(d.m_Sc);
		ds.add(d);
		
//		System.out.println("==================add send:" + d.m_Data.limit());
		
		return ds.size() == 1;
	}

	private void _ProcessData(Data d, Selector s)
	{
		if ( d.m_Sc.isOpen() )
		{
			SelectionKey key = d.m_Sc.keyFor(s);
			if  (key != null)
			{
				_AddSend(d);
				key.interestOps(SelectionKey.OP_WRITE);
//				System.err.println("interestOps.Write:" + key);
			}
			else
			{
				System.err.println("xxxxxxxxxxxx can't find key:" + d.m_Sc + " sz:"+ d.m_Data.limit());
				Iterator<SelectionKey> it = s.keys().iterator();
				while (it.hasNext())
				{
					System.err.println(it.next());
				}
			}
		}
		else
		{
//			System.err.println("xxxxxxxxxxxx sc is closed:" + d.m_Data.limit());
		}
	}
}
