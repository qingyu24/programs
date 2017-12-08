/**
 * RobotTest.java 2012-10-11上午11:18:32
 */
package test.utility;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.jar.*;

import core.detail.impl.log.Log;

import test.robot.*;
import utility.Str;
import utility.dyjar.DynamicLoadJarFile;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RobotTest
{
	private static String TestModulePath = "test/robot/module";
	/**
	 * 从一个jar文件中获取所有的机器人测试模块
	 *
	 * @param jarFile 文件名
	 * @return 可用的测试模块类名,是全名
	 */
	public static ArrayList<Class<?>> GetAllTestClassName(String jarFile)
	{
		ArrayList<Class<?>> all = new ArrayList<Class<?>>();
		try
		{
			JarFile jar = new JarFile(jarFile);
			Enumeration<JarEntry> e = jar.entries();
			while (e.hasMoreElements())
			{
				JarEntry entry = e.nextElement();
				Class<?> c = _IsTestClassName(entry);
				if ( c != null )
				{
					all.add(c);
//					System.out.println(c);
				}
			}
		}
		catch ( Exception e)
		{
			Log.out.LogException(e);
		}
		return all;
	}
	
	public static ArrayList<Class<?>> GetAllTestClassName(JarInputStream jis)
	{
		ArrayList<Class<?>> all = new ArrayList<Class<?>>();
		JarEntry entry = null;
		try
		{
			while ( (entry = jis.getNextJarEntry()) != null )
			{
				Class<?> c = _IsTestClassName(entry);
				if ( c != null )
				{
					all.add(c);
//					System.out.println(c);
				}
			}
		}
		catch (IOException e)
		{
			Log.out.LogException(e);
		}
		return all;
	}
	
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
			}
		}
	}
	
	public static Class<?> GetClassByName(String jarFileName, String className)
	{
		try
		{
			JarFile jar = new JarFile(jarFileName);
			Enumeration<JarEntry> e = jar.entries();
			while (e.hasMoreElements())
			{
				JarEntry entry = e.nextElement();
				Class<?> c = _IsClassName(entry, className);
				if ( c != null )
				{
					return c;
				}
			}
		}
		catch ( Exception e)
		{
			Log.out.LogException(e);
		}
		return null;
	}
	
	private static Class<?> _IsClassName(JarEntry je, String className)
	{
		String s = je.getName();
		String s1 = Str.GetLastStr(s, '/');
		//必定是个类名
		if ( !s1.endsWith(".class") )
		{
			return null;
		}
		s = s.replace('/', '.');
		s = s.replaceAll(".class", "");
		try
		{
			if ( s.equals(className) )
			{
				Class<?> c = Class.forName(s);
				return c;
			}
		}
		catch( ClassNotFoundException e1 )
		{
			Log.out.LogException(e1, "无法加载类名:" + s);
		}
		return null;
	}
	
	private static Class<?> _IsTestClassName(JarEntry je)
	{
		String s = je.getName();
		String s1 = Str.GetLastStr(s, '/');
		//在"test.robot.module"包中
		if ( !s.contains(TestModulePath) )
		{
			return null;
		}
		//必定是个类名
		if ( !s1.endsWith(".class") )
		{
			return null;
		}
		//必定不是子类
		if ( s1.contains("$") )
		{
			return null;
		}
		//起码有一个方法是Test标识
		Class<?> c = null;
		try
		{
			String classname = s.replace('/', '.');
			classname = classname.replaceAll(".class", "");
			c = Class.forName(classname);
		}
		catch( ClassNotFoundException e1 )
		{
			Log.out.LogException(e1, "无法加载类名:" + s);
		}
		if ( c == null )
		{
			return null;
		}
		RC rc = c.getAnnotation(RC.class);
		if ( rc == null )
		{
			return null;
		}
		Method[] ms = c.getMethods();
		boolean find = false;
		for ( Method m : ms )
		{
			RF rf = m.getAnnotation(RF.class);
			if ( rf != null )
			{
				find = true;
				break;
			}
		}
		if ( !find )
		{
			return null;
		}
		return c;
	}
}
