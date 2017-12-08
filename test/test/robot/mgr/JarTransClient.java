/**
 * JarTransClient.java 2012-11-1下午2:37:16
 */
package test.robot.mgr;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;
import java.util.jar.*;

import test.robot.*;
import test.robot.step.*;
import test.utility.RobotTest;
import utility.*;

import core.exception.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class JarTransClient implements Runnable
{
	private Socket 	m_Socket = null;
	private String 	m_IP;
	private int 	m_Port;
	private DataInputStream m_Stream;
	
	public JarTransClient(String ip, int port)
	{
		m_IP = ip;
		m_Port = port;
		
		new RobotConfig();
	}
	
	public void Connect()
	{
		try
		{
			m_Socket = new Socket(m_IP, m_Port);
			m_Stream = new DataInputStream(new BufferedInputStream(m_Socket.getInputStream()));
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			throw new WrapRuntimeException(e);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new WrapRuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		if ( m_Socket == null )
		{
			Connect();
		}
		
		try
		{
			byte[] md5 = new byte[16];
			m_Stream.read(md5);
			
			long len = m_Stream.readLong();
			System.out.println("获取长度:" + len);
			
			byte[] buffer = new byte[(int) len];
			byte[] buf = new byte[1024*8];
			int offset = 0;
			while ( true )
			{
				int read = m_Stream.read(buf);
				if ( read < 0 )
				{
					break;
				}
				
				for ( int i = 0; i < read; ++i )
				{
					buffer[offset + i] = buf[i];
				}
				offset += read;
			}
			System.out.println("共接受文件长度:" + offset);
			
			byte[] calcmd5 = Verify.GetMD5(buffer);
			System.out.println("文件md5对比:" + Arrays.equals(md5, calcmd5));
			
			JarInputStream jis = new JarInputStream(new ByteArrayInputStream(buffer));
			ArrayList<Class<?>> all =  RobotTest.GetAllTestClassName(jis);
			
			Map<String, Method> runs = new HashMap<String, Method>();
			for ( Class<?> c : all )
			{
				Method[] ms = c.getMethods();
				for ( Method m : ms )
				{
					RF rf = m.getAnnotation(RF.class);
					if ( rf == null )
					{
						continue;
					}
					System.out.println(m);
					runs.put(rf.Name(), m);
				}
			}
			
			Iterator<Entry<String, Method>> it = runs.entrySet().iterator();
			while ( it.hasNext() )
			{
				Entry<String, Method> e = it.next();
				System.out.println("准备运行:" + e.getKey());
				try
				{
					Step s = (Step) e.getValue().invoke(null);
					RunStep.RunBatch(s, 1 * 60 * 1000, 1, 1 * 60 * 1000);
					System.out.println("执行完成:" + e.getKey());
					break;
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
			
			m_Socket.close();
			m_Socket = null;
			
			synchronized (this)
			{
				notify();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new WrapRuntimeException(e);
		}
	}
}
