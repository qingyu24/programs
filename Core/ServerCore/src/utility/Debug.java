/**
 * Show.java 2012-7-7下午3:49:20
 */
package utility;

import java.text.*;
import java.util.*;

import core.exception.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Debug
{
	public static void ShowCurrTime()
	{
		ShowTime(System.currentTimeMillis());
	}
	
	public static void ShowTime(long time)
	{
		if ( core.RootConfig.GetInstance().Debug )
		{
			System.out.println("# " + GetShowTime(time));
		}
	}
	
	public static String GetCurrTime()
	{
		return GetShowTime(System.currentTimeMillis());
	}
	
	public static String GetShowTime(long time)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		return f.format(c.getTime());
	}
	
	public static String GetShowTimeSE(long time,String ex)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "" + ex + "" + f.format(c.getTime()) + "";
	}
	
	private static void Assert(boolean res)
	{
		if ( !res )
		{
			assert(false);
		}
	}
	
	public static void Assert(boolean res, String str)
	{
		Assert(res);
		if ( !res )
		{
			throw new DebugAssertRuntimeException(str);
		}
	}
	
	public static void Assert(boolean res, Throwable e)
	{
		Assert(res);
		if ( !res )
		{
			throw new DebugAssertRuntimeException(e);
		}
	}

	public static String GetUseTime(long usetm)
	{
		long hour = usetm / (3600 * 1000);
		usetm -= hour * 3600 * 1000;
		long minute = usetm / (60 * 1000);
		usetm -= minute * 60 * 1000;
		long second = usetm / 1000;
		usetm -= second * 1000;
		return String.format("%d:%d:%d:%d", hour, minute, second, usetm);
	}
	
	public static String GetUseTime1(long usetm)
	{
		long hour = usetm / (3600 * 1000);
		usetm -= hour * 3600 * 1000;
		long minute = usetm / (60 * 1000);
		usetm -= minute * 60 * 1000;
		long second = usetm / 1000;
		return String.format("%d:%d:%d", hour, minute, second);
	}
}
