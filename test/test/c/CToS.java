/**
 * CToS.java 2012-6-16下午3:14:27
 */
package test.c;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

import java.io.OutputStream;  
import java.net.Socket;  

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class CToS implements Runnable
{
	static SocketChannel sc;   
	String host="localhost";   
	int port=9527; 
	ByteBuffer writebuf=ByteBuffer.allocate(1024);   
	InetSocketAddress ad=new InetSocketAddress(host,port);
	
	public CToS()
	{
//		try 
//		{
//			sc=SocketChannel.open();   
//			Selector sl=Selector.open();   
//			sc.configureBlocking(false);   
//			//连接到server;   
//			sc.connect(ad);   
//			    
//			if(!sc.finishConnect())
//			{
//				System.out.print("客户端连接失败");   
//			}
//			else
//			{
//				System.out.println("客服端连接成功"); 
//			}
//		} 
//		catch (Exception e) 
//		{      
//			e.printStackTrace();   
//		}   
	}
	
	void SendMsg()
	{
		try 
		{
			short length = 10;
			writebuf.putShort(0,length);
			writebuf.put(2,(byte)0);
			writebuf.put(3,(byte)0);
			
			writebuf.putShort(4,(short)3);
			writebuf.put(6,(byte) 'a');
			writebuf.put(7,(byte) 'b');
			writebuf.put(8,(byte) 'c');
			
			writebuf.putShort(9,(short) 1);  
			writebuf.put(11,(byte) 'x');
			
			writebuf.limit(12);
			writebuf.flip();
			sc.write(writebuf);
		}   
		catch (Exception e) 
		{  
			e.printStackTrace();   
		}
	}
	
	public void SendMsg1()
	{
		try
		{
			Socket socket = new Socket(host, port);
			
			OutputStream ous = socket.getOutputStream();
			
			short length = 10;
			writebuf.putShort(0,length);
			writebuf.put(2,(byte)0);
			writebuf.put(3,(byte)0);
			
			writebuf.putShort(4,(short)3);
			writebuf.put(6,(byte) 'a');
			writebuf.put(7,(byte) 'b');
			writebuf.put(8,(byte) 'd');
			
			writebuf.putShort(9,(short) 1);  
			writebuf.put(11,(byte) 'x');
			
			byte[] bs = writebuf.array();
			
			ous.write(bs, 0, 12);
			
			
			byte[] read = new byte[1024];
			InputStream ius = socket.getInputStream();
			DataInputStream dis = new DataInputStream(ius);
			short sz = dis.readShort();
			dis.readFully(read, 0 , sz-2);
			
			System.out.println("sz:" + sz);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}  
	}
	
	public void SendMsg2()
	{
		try
		{
			Socket socket = new Socket(host, port);
			
			OutputStream ous = socket.getOutputStream();
			
			short length = 14;
			writebuf.putShort(0,length);
			writebuf.put(2,(byte)0);
			writebuf.put(3,(byte)63);
			
			writebuf.putShort(4,(short)1);
			writebuf.put(6,(byte) 'a');
			
			writebuf.putShort(7,(short) 3);  
			writebuf.put(9,(byte) '1');
			writebuf.put(10,(byte) '2');
			writebuf.put(11,(byte) '3');
			
			writebuf.putInt(12, 3);
			
			byte[] bs = writebuf.array();
			
			ous.write(bs, 0, length + 2);
			
			
			byte[] read = new byte[1024];
			InputStream ius = socket.getInputStream();
			DataInputStream dis = new DataInputStream(ius);
			short sz = dis.readShort();
			dis.readFully(read, 0 , sz-2);
			
			System.out.println("sz:" + sz);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}  
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		new Thread(new CToS()).start();
		Thread.sleep(3);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		SendMsg2();
	}

}
