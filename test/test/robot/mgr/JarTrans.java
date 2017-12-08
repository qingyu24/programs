/**
 * JarTrans.java 2012-11-1上午10:57:51
 */
package test.robot.mgr;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import utility.*;

import core.exception.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 用来传输Jar文件的
 */
public class JarTrans implements Runnable
{
	private ServerSocket m_Socket;
	private boolean m_Run = true;
	
	public JarTrans(int port)
	{
		try
		{
			m_Socket = new ServerSocket(port);
			System.out.println("打开端口:" + port);
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
		while ( m_Run )
		{
			Socket s = null;
			DataInputStream fis = null;
			try
			{
				s = m_Socket.accept();
				System.out.println("建立了一个连接");
				
				String filename = "Server.jar";
				File file = new File(filename);
				
				DataOutputStream ps = new DataOutputStream(s.getOutputStream());
				
				byte[] bs = Verify.GetMD5(filename);
				ps.write(bs, 0, bs.length);
				ps.flush();
				
				ps.writeLong(file.length());
				ps.flush();
				
				System.out.println("文件长度:" + file.length());
				
				System.out.println(Arrays.toString(bs));
				
				FileInputStream fi = new FileInputStream(filename);
				fis = new DataInputStream(new BufferedInputStream(fi));
				byte[] buffer = new byte[1024*8];
				while (true)
				{
					int read = fis.read(buffer);
					if ( read == -1 )
					{
						break;
					}
					ps.write(buffer, 0, read);
				}
				ps.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				throw new WrapRuntimeException(e);
			}
			finally
			{
				try
				{
					fis.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				
				try
				{
					s.close();
					System.out.println("关闭socket");
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void Stop()
	{
		m_Run = false;
	}
}
