package utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import core.RootConfig;
import core.detail.impl.log.Log;

public class TimeMethod
{
	private static SimpleDateFormat m_Format = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat m_DayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static float m_Speed = 1.0f;
	private static double m_CurrentTime = System.currentTimeMillis();
	private static long m_RealTime = System.currentTimeMillis();
	private static boolean m_Change = false;
	private static long m_StartTime = System.currentTimeMillis();
	public static final long OneDayMillis = 24 * 60 * 60 * 1000;
	
	public static final int FIXED_DATE = 0;
	public static final int EVERY_YEAR = 1;
	public static final int EVERY_MONTH = 2;
	public static final int EVERY_WEEK = 3;
	public static final int EVERY_DAY = 4;
	
	static public boolean IsInOneDay(Calendar c1, Calendar c2)
	{
		if(c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
		{
			return false;
		}
		
		if (c1.get(Calendar.DAY_OF_YEAR) != c2.get(Calendar.DAY_OF_YEAR))
		{
			return false;
		}
		return true;
	}
	
	static public boolean IsInOneMonth(Calendar c1, Calendar c2)
	{
		if(c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
		{
			return false;
		}
		
		if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH))
		{
			return false;
		}
		
		return true;
	}
	
	static public boolean IsInOneDay(long t1, long t2)
	{
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(t1);
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(t2);
		return IsInOneDay(c1, c2);
	}
	
	static public boolean IsInOneDay(long t, Calendar c)
	{
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(t);
		return IsInOneDay(c1, c);
	}
	
	static public boolean IsInOneDay(Calendar c, long t)
	{
		return IsInOneDay(t, c);
	}
	
	static public boolean IsToday(long t)
	{
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(currentTimeMillis());
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(t);
		return IsInOneDay(c1, c2);
	}
	
	static public boolean IsInOneMonth(long t)
	{
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(currentTimeMillis());
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(t);
		return IsInOneMonth(c1,c2);
	}
	
	static public boolean IsToday(Calendar c)
	{
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(currentTimeMillis());
		return IsInOneDay(c1, c);
	}
	
	// 时间前后判断(每天更新)
	// DefineTime(定义更新时间/HH:MM:SS)
	// UpdateTime(上次更新时间/毫秒)
	// 判断当前时间与上次更新时间是否跨越了定义更新时间
	static public boolean IsOutOfDate(String p_DefineTime, long p_UpdateTime)
	{
		long defineTime = GetDefineTime(p_DefineTime);
		
		// 打印输出
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(defineTime);
		System.out.println("定义时间："  + m_DayFormat.format(c.getTime()));
		
		return (defineTime > p_UpdateTime);
	}
	
	// 根据当前时间判断，获取上次定义更新时间
	@SuppressWarnings("deprecation")
	public static long GetDefineTime(String p_DefineTime)
	{
		long result = 0;
		try
		{
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			Date curdate = c.getTime();
			
			Date date = m_Format.parse(p_DefineTime);
			c.set(Calendar.HOUR_OF_DAY, date.getHours());
			c.set(Calendar.MINUTE, date.getMinutes());
			c.set(Calendar.SECOND, date.getSeconds());

			result = c.getTimeInMillis();
			
			if (curdate.before(c.getTime()))
			{
				// 昨天的定义时间
				result -= OneDayMillis;
			}
		}
		catch (ParseException e)
		{
			Log.out.LogException(e, "时间解析错误[" + p_DefineTime + "]");
		}
		
		return result;
	}
	
	public static long GetDefineTimeByStr(String p_DefineTime)
	{
		long result = 0;
		try
		{
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			Date curdate = c.getTime();
			
			Date date = m_Format.parse(p_DefineTime);
			c.set(Calendar.HOUR_OF_DAY, date.getHours());
			c.set(Calendar.MINUTE, date.getMinutes());
			c.set(Calendar.SECOND, date.getSeconds());

			result = c.getTimeInMillis();
		}
		catch (ParseException e)
		{
			Log.out.LogException(e, "时间解析错误[" + p_DefineTime + "]");
		}
		
		return result;
	}
	
	public static void Init()
	{
		m_CurrentTime = System.currentTimeMillis();
		m_RealTime = System.currentTimeMillis();
		m_StartTime = System.currentTimeMillis();
		m_Change = RootConfig.GetInstance().Show;
	}
	
	public static long currentTimeMillis()
	{
		if ( !m_Change )
		{
			return System.currentTimeMillis();
		}
		else
		{
			if ( m_RealTime != System.currentTimeMillis() )
			{
				m_CurrentTime += (System.currentTimeMillis() - m_RealTime) * m_Speed;
				m_RealTime = System.currentTimeMillis();
			}
			return (long)m_CurrentTime;
		}
	}

	public static void SetSpeed(float speed)
	{
		m_Speed = speed;
	}

	public static long GetRelativeElapseTime()
	{
		return currentTimeMillis() - m_StartTime;
	}
	
	public static long GetRealElapseTime()
	{
		return System.currentTimeMillis() - m_StartTime;
	}

	/**
	 * 让时间向前走多少毫秒
	 */
	public static void AddTime(int add)
	{
		m_CurrentTime += add;
		m_RealTime = System.currentTimeMillis();
	}
	
	public static String GetCurTimeStr()
	{
		return GetDayTimeStr(currentTimeMillis());
	}
	
	public static String GetDayTimeStr(long tm)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tm);
		return m_DayFormat.format(c.getTime());
	}
	
	public static long GetInDateTime(String p_strdata, int p_inDateHours)
	{
		Calendar cNow = Calendar.getInstance();
		cNow.setTimeInMillis(System.currentTimeMillis());		
		String strData =""+cNow.get(Calendar.YEAR)+"-"+p_strdata+" 00:00:00";
		try
		{
			Date time = m_DayFormat.parse(strData);
			Calendar cStartTime = Calendar.getInstance();
			cStartTime.setTime(time);
			
			Calendar cAimTimeGap = Calendar.getInstance();
			cAimTimeGap.setTime(time);			
			cAimTimeGap.add(Calendar.HOUR,p_inDateHours);
			//Date aimdata = cAimTimeGap.getTime();
			//String a = f.format(aimdata);
			
			long indate =cAimTimeGap.getTimeInMillis() - cNow.getTimeInMillis() ;
			if(indate < 0)
				indate = 0;
			
			return indate;		
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 检测当前时间是否在某段日期内,
	 * @param p_strdata			:起始的日期(?月?日), 
	 * @param p_inDateHours		:从指定日期开始多少个小时内
	 * @return
	 */
	public static boolean CheckInDate(String p_strdata, int p_inDateHours)
	{
		Calendar cNow = Calendar.getInstance();
		cNow.setTimeInMillis(System.currentTimeMillis());		
		String strData =""+cNow.get(Calendar.YEAR)+"-"+p_strdata+" 00:00:00";
		try
		{
			Date time = m_DayFormat.parse(strData);
			Calendar cStartTime = Calendar.getInstance();
			cStartTime.setTime(time);
			
			Calendar cAimTimeGap = Calendar.getInstance();
			cAimTimeGap.setTime(time);			
			cAimTimeGap.add(Calendar.HOUR,p_inDateHours);
			//Date aimdata = cAimTimeGap.getTime();
			//String a = f.format(aimdata);
			
			if(cNow.getTimeInMillis() < cAimTimeGap.getTimeInMillis())
			{
				return true;
			}
			else
			{
				return false;
			}		
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 检测当前时间是否在某段日期内,
	 * @param p_strdata			:起始的日期(?月?日), 
	 * @param p_inDateHours		:从指定日期开始多少个小时内
	 * @return
	 */
	public static boolean CheckYearDate(String p_stryeardata, int p_inDateHours)
	{
		Calendar cNow = Calendar.getInstance();
		cNow.setTimeInMillis(System.currentTimeMillis());		
//		String strData =""+cNow.get(Calendar.YEAR)+"-"+p_strdata+" 00:00:00";
		try
		{
			Date time = m_DayFormat.parse(p_stryeardata);
			Calendar cStartTime = Calendar.getInstance();
			cStartTime.setTime(time);
			
			Calendar cAimTimeGap = Calendar.getInstance();
			cAimTimeGap.setTime(time);			
			cAimTimeGap.add(Calendar.HOUR,p_inDateHours);
			//Date aimdata = cAimTimeGap.getTime();
			//String a = f.format(aimdata);
			
			if(cNow.getTimeInMillis() < cAimTimeGap.getTimeInMillis())
			{
				return true;
			}
			else
			{
				return false;
			}		
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	// p_sTime格式必须满足"HH:mm:ss"
	public static Calendar ParseTime(String p_sTime)
	{
		Calendar c = Calendar.getInstance();
		c.setLenient(true);
		try
		{
			Date date = m_Format.parse(p_sTime);
			c.setTime(date);
		}
		catch (ParseException e)
		{
			Log.out.LogException(e, "开始时间解析错误[" + p_sTime + "]");
			e.printStackTrace();
		}
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		return c;
	}
	
	// p_sDayTime格式必须满足"yyyy-MM-dd HH:mm:ss"
	public static Calendar ParseDayTime(String p_sDayTime)
	{
		Calendar c = Calendar.getInstance();
		c.setLenient(true);
		try
		{
			Date date = m_DayFormat.parse(p_sDayTime);
			c.setTime(date);
		}
		catch (ParseException e)
		{
			Log.out.LogException(e, "开始时间解析错误[" + p_sDayTime + "]");
			e.printStackTrace();
		}
		return c;
	}
	
	// p_sMonthTime格式必须满足"dd HH:mm:ss"
	public static Calendar ParseMonthTime(String p_sMonthTime)
	{
		String[] tmp = p_sMonthTime.split(" ");
		int dayOfMonth = Integer.parseInt(tmp[0]);
		Calendar c = ParseTime(tmp[1]);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		c.set(year, month, dayOfMonth);
		return c;
	}
	
	// p_sWeekTime格式必须满足"u HH:mm:ss"，其中u表示一周的第几天（1-周一……7周日）（默认周一为一周的第一天）
	public static Calendar ParseWeekTime(String p_sWeekTime)
	{
		String[] tmp = p_sWeekTime.split(" ");
		int weekDay = Integer.parseInt(tmp[0]) % 7 + 1;		// Calendar里面周一是2，周二是3……周六是7，而周日是1，所以这里要转换一下
		Calendar c = ParseTime(tmp[1]);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		int weekYear = c.getWeekYear();
		int weekOfYear = c.get(Calendar.WEEK_OF_YEAR);
		c.setWeekDate(weekYear, weekOfYear, weekDay);
		return c;
	}
	
	static final String[] weeks = new String[]{"星期天","星期一","星期二","星期三","星期四","星期五","星期六",};
	
	public static void main(String[] args)
	{
		long tm = System.currentTimeMillis();
		String s = GetDayTimeStr(tm);
		try
		{
			Date date = m_Format.parse("12:13:14");
			Calendar cc1 = Calendar.getInstance();
			cc1.setLenient(true);
			cc1.setTimeInMillis(TimeMethod.currentTimeMillis());
			cc1.set(Calendar.HOUR_OF_DAY, date.getHours());
			cc1.set(Calendar.MINUTE, date.getMinutes());
			cc1.set(Calendar.SECOND, date.getSeconds());
			Calendar cc2 = Calendar.getInstance();
			cc2.setLenient(true);
			cc2.setTime(date);
			int hour1 = cc2.get(Calendar.HOUR_OF_DAY);
			int minute1 = cc2.get(Calendar.MINUTE);
			int second1 = cc2.get(Calendar.SECOND);
			cc2 = Calendar.getInstance();
			cc2.set(Calendar.HOUR_OF_DAY, hour1);
			cc2.set(Calendar.MINUTE, minute1);
			cc2.set(Calendar.SECOND, second1);
			int res1 = cc2.compareTo(cc1);

			boolean res = CheckTmPeriodPerWeek("6 00:00:00", "7 23:59:59");
			System.out.print(res);
			Calendar c = ParseDayTime(s);
//			long tm1 = c.getTimeInMillis();
//			String s1 = GetDayTimeStr(tm1);
			int dayofmonth = c.get(Calendar.DAY_OF_MONTH);
			int dayofweek = c.get(Calendar.DAY_OF_WEEK);
			int dayofweekinmonth = c.get(Calendar.DAY_OF_WEEK_IN_MONTH);
			int dayofyear = c.get(Calendar.DAY_OF_YEAR);
			int hourofday = c.get(Calendar.HOUR_OF_DAY);
			int hour = c.get(Calendar.HOUR);
			int minute = c.get(Calendar.MINUTE);
			int second = c.get(Calendar.SECOND);
			int month = c.get(Calendar.MONTH);
			int weekofmonth = c.get(Calendar.WEEK_OF_MONTH);
			int weekofyear = c.get(Calendar.WEEK_OF_YEAR);
			int year = c.get(Calendar.YEAR);
			
			String s2 = s.substring(s.indexOf(' ') + 1);
			Calendar c1 = ParseTime(s2);
			hourofday = c1.get(Calendar.HOUR_OF_DAY);
			hour = c1.get(Calendar.HOUR);
			minute = c1.get(Calendar.MINUTE);
			second = c1.get(Calendar.SECOND);
			
			System.out.print("" + dayofmonth + dayofweek + dayofweekinmonth + dayofyear + hourofday + hour + minute + second + month + weekofmonth + weekofyear + year);
//			Debug.Assert(tm1 == tm && s1.equals(s1), "时间错误");
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
	}
	
	//检测当前时间是否位于定义时间范围之内，时间格式"yyyy-MM-dd HH:mm:ss"
	public static boolean CheckTmPeriod(String p_StartTm,String p_EndTm)
	{
		long start_tm=ParseDayTime(p_StartTm).getTimeInMillis();
		long end_tm=ParseDayTime(p_EndTm).getTimeInMillis();
		long tm_now=currentTimeMillis();
		if (start_tm < tm_now && tm_now < end_tm)
			return true;
		return false;
	}
	
	// 格式必须是“MM-dd HH:mm:ss”
	public static boolean CheckTmPeriodPerYear(String p_StartTm, String p_EndTm)
	{
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		return CheckTmPeriod(String.format("%1$04d-%2$s", year, p_StartTm), String.format("%1$04d-%2$s", year, p_EndTm));
	}
	
	// 格式必须是“dd HH:mm:ss”
	public static boolean CheckTmPeriodPerMonth(String p_StartTm, String p_EndTm)
	{
		long nowMilli = currentTimeMillis();
		long startMilli = ParseMonthTime(p_StartTm).getTimeInMillis();
		long endMilli = ParseMonthTime(p_EndTm).getTimeInMillis();
		if (startMilli < nowMilli && endMilli > nowMilli)
			return true;
		return false;
	}
	
	// 格式必须是“u HH:mm:ss”
	public static boolean CheckTmPeriodPerWeek(String p_StartTm, String p_EndTm)
	{
		long nowMilli = currentTimeMillis();
		long startMilli = ParseWeekTime(p_StartTm).getTimeInMillis();
		long endMilli = ParseWeekTime(p_EndTm).getTimeInMillis();
		if (startMilli < nowMilli && endMilli > nowMilli)
			return true;
		return false;
	}
	
	// 格式必须是“HH:mm:ss”
	public static boolean CheckTmPeriodPerDay(String p_StartTm, String p_EndTm)
	{
		long nowMilli = currentTimeMillis();
		long startMilli = ParseTime(p_StartTm).getTimeInMillis();
		long endMilli = ParseTime(p_EndTm).getTimeInMillis();
		if (startMilli < nowMilli && endMilli > nowMilli)
			return true;
		return false;
	}
	
	public static boolean CheckTmPeriodByType(int type, String p_StartTm, String p_EndTm) 
	{
		switch (type) 
		{
		case 0:
			return CheckTmPeriod(p_StartTm, p_EndTm);
		case 1:
			return CheckTmPeriodPerYear(p_StartTm, p_EndTm);
		case 2:
			return CheckTmPeriodPerMonth(p_StartTm, p_EndTm);
		case 3:
			return CheckTmPeriodPerWeek(p_StartTm, p_EndTm);
		case 4:
			return CheckTmPeriodPerDay(p_StartTm, p_EndTm);
		default:
			return false;
		}
	}
	
	public static long GetStringToTime(String str)
	{
		if (str == null) {
			return -1;
		}
		Calendar c1 = Calendar.getInstance();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			c1.setTime(sd.parse(str));
		} catch (ParseException e) {
			System.out.println("输入的日期格式有误！");
			return -1;
		}
		return c1.getTimeInMillis();
	}
	
	//时间间隔  "yyyy-MM-dd HH:mm:ss"  "yyyy-MM-dd HH:mm:ss"
	public static long TimeInterval(String str1,String str2)
	{
		long time1 = GetStringToTime(str1);
		long time2 = GetStringToTime(str2);
		
		return Math.abs(time1 - time2);
	}
	
	//时间间隔  "yyyy-MM-dd HH:mm:ss"  "yyyy-MM-dd HH:mm:ss"
	public static long TimeInterval(long timeMillis,String str)
	{
		long timeMills_str = GetStringToTime(str);
		
		return Math.abs(timeMillis - timeMills_str);
	}
}
