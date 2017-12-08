/**
 * ListenNioBase.java 2012-11-6上午10:36:37
 */
package core.detail.impl.socket;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;
import java.util.Map.*;

import utility.*;


import core.*;
import core.detail.*;
import core.detail.impl.log.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public abstract class ListenNioBase implements Listen
{
	private Map<Integer,Link> 	m_AllLink = new HashMap<Integer,Link>();
    private	Selector 			m_Selector;  
    private List<Link>	 		m_RemoveLink = new LinkedList<Link>();
    private Link				m_Seed;	///<用来创建新Link的种子
    private boolean				m_Run = false;
    private int					m_Port = 0;
    
    public ListenNioBase(Link seed)
    {
    	Debug.Assert(seed != null, "");
    	m_Seed = seed;
    }
    
    /* (non-Javadoc)
	 * @see core.detail.interface_.Listen#AddRemoveLinkList(core.detail.interface_.Link)
	 */
	@Override
	public void AddRemoveLinkList(Link l)
	{
		long stm = System.currentTimeMillis();
		synchronized (m_RemoveLink)
		{
			m_RemoveLink.add(l);
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "ListenNioBase::_GetQueue用时[[[:" + (System.currentTimeMillis() - stm));
		}
	}
   
    /* (non-Javadoc)
	 * @see core.detail.interface_.Listen#GetLinkNum()
	 */
	@Override
	public int GetLinkNum()
	{
		return m_AllLink.size();
	}
    /* (non-Javadoc)
	 * @see core.detail.interface_.Listen#Init(int)
	 */
	@Override
	public void Init(int port) throws IOException
	{
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_OPEN_PORT_FINISH, port);
    	
        // 打开服务器套接字通道  
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		// 服务器配置为非阻塞  
        serverSocketChannel.configureBlocking(false);  
        // 检索与此通道关联的服务器套接字  
        ServerSocket serverSocket = serverSocketChannel.socket();
        //重用端口,解决8.1.30错误
        serverSocket.setReuseAddress(true);
        // 进行服务的绑定  
        serverSocket.bind(new InetSocketAddress(port));  
        // 通过open()方法找到Selector  
        m_Selector = Selector.open(); 
        // 注册到m_Selector，等待连接  
        serverSocketChannel.register(m_Selector, SelectionKey.OP_ACCEPT);  
        
        m_Run = true;
        m_Port = port;
        
        Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_OPEN_PORT, port);
	}
   
    
	/* (non-Javadoc)
	 * @see core.detail.interface_.Listen#Reset()
	 */
	@Override
	public void Reset()
	{
		if ( m_Selector != null )
    	{
    		try
			{
				m_Selector.close();
				m_Selector = null;
			}
			catch (IOException e)
			{
				Log.out.LogException(e);
			}
    	}
    	
    	Iterator<Entry<Integer, Link>> it = m_AllLink.entrySet().iterator();
    	while ( it.hasNext() )
    	{
    		Entry<Integer, Link> e = it.next();
    		e.getValue().Close(eCloseReasonType.LISTEN_CLOSE.ID(), 0);
    	}
    	m_AllLink.clear();
    	m_RemoveLink.clear();
	}
    
//	private int m_Index = 0;
    /* (non-Javadoc)
	 * @see core.detail.interface_.Listen#StartListen()
	 */
	@Override
	public void StartListen() throws IOException
	{
		while (m_Run) 
        {  
//			m_Index++;
			
			_GetSendBuffer().ProcessWriteBuffer(m_Selector);
			
            // 返回此选择器的已选择键集。  
            Set<SelectionKey> selectionKeys = null;
            try
            {
            	 // 选择一组键，并且相应的通道已经打开  
                m_Selector.select(); 
            	selectionKeys = m_Selector.selectedKeys();
            }
            catch (Exception e)
            {
            	m_Run = false;
            }
            
//            System.err.println("selectionKeys:" + selectionKeys + "," + m_Index + " tid:" + Thread.currentThread());
            if ( selectionKeys != null )
            {
	            Iterator<SelectionKey> iterator = selectionKeys.iterator();  
	            while (iterator.hasNext()) 
	            {          
	                SelectionKey selectionKey = iterator.next();  
	                iterator.remove();  
	                _HandleKey(selectionKey);  
	            }
            }
            
            _CheckLink();
            _RemoveLink();
            
//            System.err.println("selectionKeys Finish:" + m_Index + " tid:" + Thread.currentThread());
        }
		
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_STOP_USER_ENTRY, "ListenNioBase::Finish:" + m_Port);
	}
    
    /* (non-Javadoc)
	 * @see core.detail.interface_.Listen#Stop()
	 */
	@Override
	public void Stop()
	{
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_STOP_USER_ENTRY, "ListenNioBase::Start:" + m_Port);
		m_Run = false;
		Reset();
	}
    
    private void _AcceptConnect(SelectionKey selectionKey)
	{
		SocketChannel client = null;
		try
		{
			// 返回为之创建此键的通道。  
			ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();  
	        // 接受到此通道套接字的连接。  
	        // 此方法返回的套接字通道（如果有）将处于阻塞模式。  
			client = server.accept();  
	        // 配置为非阻塞  
	        client.configureBlocking(false);  
	        // 注册到m_Selector，等待连接  
	        client.register(m_Selector, SelectionKey.OP_READ);  
	        
	        boolean c = _CheckIPLimit(client);
	        if (!c)
	        {
	        	client.close();
	        	return;
	        }
	        
	        Root.GetInstance().GetFactory().OnLinkNumChange(m_AllLink.size(), RootConfig.GetInstance().LinkLimit);
	        
	        Link l = m_Seed.Clone(this, client);
	        m_AllLink.put(l.hashCode(), l);
	        
	        Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_LINK_OK, l.hashCode(), l.GetUserHashCode());
		}
		catch( IOException e )
		{
			Log.out.LogException(e);
			SystemFn.CloseSocketChannel(client);
		}
	}
    protected abstract boolean _CheckIPLimit(SocketChannel c);
    
    private Link _GetLink(SelectionKey selectionKey)
    {
    	Link l = null;
    	if ( selectionKey != null )
    	{
	    	SocketChannel client = (SocketChannel) selectionKey.channel();
	    	if ( m_AllLink.containsKey(client.hashCode()) )
	    	{
	    		l = m_AllLink.get(client.hashCode());
	    	}
    	}
        return l;
    }
    
    // 处理请求  
    private void _HandleKey(SelectionKey selectionKey)
    { 
//    	System.err.println("_HandleKey:" + selectionKey + "," + m_Index + " channel:" + selectionKey.channel() + " accept:" + selectionKey.isAcceptable() + " read:" + selectionKey.isReadable() + " write:" + selectionKey.isWritable());
        // 测试此键的通道是否已准备好接受新的套接字连接
    	try
    	{
	        if ( selectionKey.isValid() ) 
	        {  
	        	if ( selectionKey.isAcceptable() )
	        	{
	        		_AcceptConnect(selectionKey);
	        	}
	        	else if ( selectionKey.isReadable() )
	        	{
	        		_ReadMsg(selectionKey);
	        	}
	        	else if ( selectionKey.isWritable() )
	        	{
	        		_WriteMsg(selectionKey);
	        	}
	        }
    	}
    	catch ( CancelledKeyException e)
    	{
    		try
    		{
	    		Link l = _GetLink(selectionKey);
	    		if ( l != null )
	    		{
	    			long stm = System.currentTimeMillis();
	    			l.Close(eCloseReasonType.CANCELLEDKEY_EXCEPTION.ID(), 0);
	    			if ( System.currentTimeMillis() - stm > 50 )
	    			{
	    				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "ListenNioBase::_HandleKey[[[:" + (System.currentTimeMillis() - stm));
	    			}
	    		}
    		}
    		catch(Exception e1)
    		{
    			
    		}
    	}
        
        _OnProcessOperCallBack();
    }
    
	private void _ReadMsg(SelectionKey selectionKey)
	{
		// 返回为之创建此键的通道。  
		Link l = _GetLink(selectionKey);
		if ( l == null )
		{
			return;
		}
        boolean b = l.OnRead();
        if ( !b )
        {
        	m_AllLink.remove(l.hashCode());
        }
	}
	
	private void _WriteMsg(SelectionKey selectionKey)
	{
		SocketChannel sc = (SocketChannel)selectionKey.channel();
		boolean b = _GetSendBuffer().Write(sc);
		if (b)
		{
			selectionKey.interestOps(SelectionKey.OP_READ);
//			System.err.println("interestOps.Read:" + selectionKey);
		}
	}
	
	private void _CheckLink()
	{
		Iterator<Entry<Integer, Link>> it = m_AllLink.entrySet().iterator();
		while (it.hasNext())
		{
			it.next().getValue()._CheckLink();
		}
	}

	private void _RemoveLink()
	{
		long stm = System.currentTimeMillis();
		synchronized (m_RemoveLink)
		{
			Iterator<Link> it = m_RemoveLink.iterator();
			while (it.hasNext())
			{
				Link l = it.next();
				if ( l._CanCloseImmediately() )
				{
					l._CloseImmediately();
					it.remove();
					m_AllLink.remove(l.hashCode());
				}
				else
				{
					System.out.println("连接:" + l + "还有消息没有发送完毕,不能删除");
				}
			}
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "ListenNioBase::_RemoveLink用时[[[:" + (System.currentTimeMillis() - stm));
		}
	}
	
	protected SendBuffer _GetSendBuffer()
	{
		return SendBuffer.GetNormal();
	}

	/**
     * 当每次连接有操作后的回调(创建,读取,发送)
     */
    protected abstract void _OnProcessOperCallBack();
}
