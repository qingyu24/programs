/**
 * Str.java 2012-6-19下午12:19:47
 */
package utility;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 字符串的一些操作接口
 */
public class Str
{
	public static boolean GetBoolean(String s)
	{
		if ( s == null || s.isEmpty() )
		{
			return false;
		}
		return Boolean.parseBoolean(s);
	}
	
	public static byte GetByte(String s)
	{
		if ( s == null || s.isEmpty() )
		{
			return 0;
		}
		return Byte.parseByte(s);
	}
	
	public static short GetShort(String s)
	{
		if ( s == null || s.isEmpty() )
		{
			return 0;
		}
		return Short.parseShort(s);
	}
	
	public static int GetInt(String s)
	{
		if ( s == null || s.isEmpty() )
		{
			return 0;
		}
		return Integer.parseInt(s);
	}
	
	public static long GetLong(String s)
	{
		if ( s == null || s.isEmpty() )
		{
			return 0;
		}
		return Long.parseLong(s);
	}
	
	public static float GetFloat(String s)
	{
		if ( s == null || s.isEmpty() )
		{
			return 0;
		}
		return Float.parseFloat(s);
	}
	
	public static double GetDouble(String s)
	{
		if ( s == null || s.isEmpty() )
		{
			return 0;
		}
		return Double.parseDouble(s);
	}
	
	public static String GetString(String s)
	{
		if ( s == null )
		{
			return "";
		}
		return s;
	}
	
	public static String GetLastStr(String s, char c)
	{
		try
		{
			return s.substring(s.lastIndexOf(c) + 1);
		}
		catch( StringIndexOutOfBoundsException e)
		{
			return s;
		}
	}
	
	public static String GetFirstStr(String s, char c)
	{
		try
		{
			return s.substring(0, s.indexOf(c));
		}
		catch( StringIndexOutOfBoundsException e)
		{
			return s;
		}
	}
	
}
