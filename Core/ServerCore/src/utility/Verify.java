/**
 * MD5.java 2012-11-1上午11:17:51
 */
package utility;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import core.detail.impl.log.Log;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Verify
{
	/**
	 * 获取一个文件的MD5码
	 * 
	 * @return 无法获取会返回null
	 */
	public static byte[] GetMD5(String filename)
	{
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(filename);
			return GetMD5(fis);
		}
		catch(IOException e)
		{
			Log.out.LogException(e);
		}
		finally
		{
			if ( fis != null )
			{
				try
				{
					fis.close();
				}
				catch (IOException e)
				{
					Log.out.LogException(e);
				}
			}
		}
		return null;
	}
	
	public static byte[] GetMD5(InputStream is) throws IOException
	{
		try
		{
			int length = -1;
			byte[] buffer= new byte[1024*8];
			MessageDigest md = MessageDigest.getInstance("MD5");
			int offset = 0;
			while ( (length = is.read(buffer)) != -1 )
			{
				md.update(buffer, 0, length);
				offset += length;
			}
			System.out.println("Stream Length:" + offset);
			return md.digest();
		}
		catch (NoSuchAlgorithmException e)
		{
			Log.out.LogException(e);
		}
		return null;
	}
	
	public static byte[] GetMD5(byte[] bs)
	{
		MessageDigest md;
		try
		{
			md = MessageDigest.getInstance("MD5");
			md.update(bs, 0, bs.length);
			System.out.println("Stream Length:" + bs.length);
			return md.digest();
		}
		catch (NoSuchAlgorithmException e)
		{
			Log.out.LogException(e);
		}
		return null;
	}
}
