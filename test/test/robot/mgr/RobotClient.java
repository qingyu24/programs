/**
 * RobotClient.java 2012-11-1下午2:36:39
 */
package test.robot.mgr;

import utility.ExcelData;
import utility.ExcelIniReader;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RobotClient
{
	@ExcelData( File = "RobotService.xls", Table = "Sheet1" )
	public static class Config
	{
		public String 	IP;
		public int		Port;
	}
	
	public static void main(String[] args)
	{
		Config cf = new Config();
		ExcelIniReader.Read(cf);
		JarTransClient c = new JarTransClient(cf.IP, cf.Port);
		new Thread(c).start();
		
		synchronized (c)
		{
			try
			{
				c.wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
