/**
 * DynamicLoadJarFile.java 2012-11-27下午3:39:44
 */
package utility.dyjar;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import core.detail.impl.log.Log;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DynamicLoadJarFile extends URLClassLoader
{
	private static DynamicLoadJarFile m_Instance = new DynamicLoadJarFile();
	
	public DynamicLoadJarFile()
	{
		super(new URL[0], ClassLoader.getSystemClassLoader());
	}
	
	public static DynamicLoadJarFile GetInstance()
	{
		return m_Instance;
	}
	
	public void LoadJarFile(String jarFilePathName)
	{
		try
		{
			URL[] us = getURLs();
			String u = "file:" + jarFilePathName;
			for ( URL url : us )
			{
				if (url.getFile().equals(jarFilePathName))
				{
					return;
				}
			}
			URL url = new URL(u);
			addURL(url);
		}
		catch (MalformedURLException e)
		{
		}
	}
	
	public Class<?> ReadClass(String className)
	{
		try
		{
			Class<?> c = findLoadedClass(className);
			if ( c != null )
			{
				return c;
			}
			else
			{
				c = findClass(className);
				return c;
			}
		}
		catch (ClassNotFoundException e)
		{
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		DynamicLoadJarFile loader = DynamicLoadJarFile.GetInstance();
		loader.LoadJarFile("SecurityPolicyServer.jar");
		Class<?> c = loader.ReadClass("sec.SecurityPolicyServer");
		if ( c != null )
		{
			System.out.println(c);
		}
		else
		{
			System.out.println("无法加载类");
		}
	}

	/**
	 * 使用一个线程来加载jar文件,然后调用指定类的main函数
	 *
	 * @param jarFileName 调用的jar文件名
	 * @param className 指定的类
	 */
	public static void ThreadRunJarClassMain(String jarFileName, String className)
	{
		DynamicLoadJarFile.GetInstance().LoadJarFile(jarFileName);
		Class<?> c = DynamicLoadJarFile.GetInstance().ReadClass(className);
		if ( c == null )
		{
			return;
		}
		Method[] ms = c.getMethods();
		for ( final Method m : ms )
		{
			if ( m.getName().equals("main") )
			{
				new Thread(
							new Runnable()
							{
								public void run()
								{
									String s = null;
									try
									{
										m.invoke(null, s);
									}
									catch (Exception e)
									{
										e.printStackTrace();
									}
								}
							}
							).start();
				return;
			}
		}
	}

	/**
	 * 获取一个Jar文件中的类对象
	 */
	public static Object GetClassObj(String jarFileName, String className, String... exAddJars)
	{
		for ( String jar : exAddJars )
		{
			DynamicLoadJarFile.GetInstance().LoadJarFile(jar);
		}
		DynamicLoadJarFile.GetInstance().LoadJarFile(jarFileName);
		Class<?> c = DynamicLoadJarFile.GetInstance().ReadClass(className);
		if ( c == null )
		{
			return null;
		}
		try
		{
			return c.newInstance();
		}
		catch (InstantiationException e)
		{
			Log.out.LogException(e);
		}
		catch (IllegalAccessException e)
		{
			Log.out.LogException(e);
		}
		return null;
	}
}
