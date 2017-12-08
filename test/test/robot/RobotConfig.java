/**
 * RootConfig.java 2012-7-12上午10:41:49
 */
package test.robot;

import logic.config.Config;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RobotConfig
{
//	public static String Host = "localhost";
	public static String Host = "192.168.3.200";
	public static int Port = 9527;
	
	public static String RobotName = "t"; 
	
	public static int MsgWaitTime = 2000;
	
	private static boolean m_Init = false;
	
	public static void Init()
	{
		if ( !m_Init )
		{
			Config.GetInstance().InitAll();
			m_Init = true;
		}
	}
	
	public RobotConfig()
	{
		Init();
	}
}
