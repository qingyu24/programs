/**
 * LogServer.java 2012-10-25下午4:27:01
 */
package test.c;

import org.apache.log4j.Level;
import org.apache.log4j.chainsaw.Main;

import core.detail.impl.log.SystemLevel;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LogServer
{
	
	public static void main(String[] args)
	{
		Level s = SystemLevel.SQL;
		System.out.println(s);
		Main.main(null);
	}
}
