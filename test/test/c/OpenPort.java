/**
 * OpenPort.java 2012-10-25下午3:55:22
 */
package test.c;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import core.detail.impl.socket.ReceiveBuffer;


/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class OpenPort
{
    private	Selector 		m_Selector;  
    private ReceiveBuffer 	m_ReceiveBuffer = new ReceiveBuffer(1024);
  
    /**
     * 初始化端口
     */
    public void Init(int port) throws IOException
    {  
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
    }  
  
    /**
     * 开始监听,这是个无限循环
     */
    public void Listen() throws IOException
    {  
    	while (true) 
        {  
            // 选择一组键，并且相应的通道已经打开  
            m_Selector.select();  
            // 返回此选择器的已选择键集。  
            Set<SelectionKey> selectionKeys = m_Selector.selectedKeys();  
            Iterator<SelectionKey> iterator = selectionKeys.iterator();  
            while (iterator.hasNext()) 
            {          
                SelectionKey selectionKey = iterator.next();  
                iterator.remove();  
                _HandleKey(selectionKey);  
            }
        }
    }
    
    /**
     * 出错后的对象清理,以保证下次重新初始化没有问题
     */
    public void Reset()
    {
    	if ( m_Selector != null )
    	{
    		try
			{
				m_Selector.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
    	}
    }
  
    // 处理请求  
    private void _HandleKey(SelectionKey selectionKey)
    { 
        // 测试此键的通道是否已准备好接受新的套接字连接。  
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
        		
        	}
        }
    }
    
	
	private void _ReadMsg(SelectionKey sk)
	{
		try
		{
			int count = m_ReceiveBuffer.Read((SocketChannel) sk.channel());
			System.out.println("收到消息:" + count);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		OpenPort o = new OpenPort();
		try
		{
			o.Init(9090);
			o.Listen();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
