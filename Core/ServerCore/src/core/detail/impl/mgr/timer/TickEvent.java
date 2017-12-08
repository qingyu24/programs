/**
 * TickEvent.java 2012-7-7下午2:00:07
 */
package core.detail.impl.mgr.timer;

import java.text.*;
import java.util.Calendar;

import utility.Debug;
import utility.TimeMethod;

import core.*;
import core.detail.impl.log.Log;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */

public class TickEvent implements Comparable<TickEvent>
{
	private static int m_BaseID = 0;
	
	private long m_NextRunTime;
	private long m_ID;
	private Tick m_Run;
	private long m_SpaceTime;
	private int	 m_Num;
	private int  m_MarkID;
	
	public TickEvent(Tick tick, int p_nSecond,int p_nNum, int markID)
	{
		m_Run = tick;
		m_SpaceTime = p_nSecond * 1000;
		m_NextRunTime = TimeMethod.currentTimeMillis() + m_SpaceTime;
		m_Num = p_nNum;
		m_MarkID = markID;
		m_ID = m_BaseID++;
	}
	
	public TickEvent(Tick tick, long p_nMilliSecond,int p_nNum, int markID)
	{
		m_Run = tick;
		m_SpaceTime = p_nMilliSecond;
		m_NextRunTime = TimeMethod.currentTimeMillis() + m_SpaceTime;
		m_Num = p_nNum;
		m_ID = m_BaseID++;
	}
	
	/**
	 * 是否到了执行的时间
	 */
	public boolean CanRun()
	{
		return TimeMethod.currentTimeMillis() >= m_NextRunTime;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TickEvent t)
	{
		if ( m_NextRunTime < t.m_NextRunTime )
		{
			return -1;
		}
		else if ( m_NextRunTime > t.m_NextRunTime )
		{
			return 1;
		}
		if ( m_ID < t.m_ID )
		{
			return -1;
		}
		if ( m_ID > t.m_ID )
		{
			return 1;
		}
		return 0;
	}
	
	public long GetID()
	{
		return m_ID;
	}
	
	public int GetMarkID()
	{
		return m_MarkID;
	}

	public void InitSolidStartDayTime(String p_sDayTime) throws ParseException
	{
		try
		{
			Calendar c = TimeMethod.ParseDayTime(p_sDayTime);
			_InitSolidTm(c);
		}
		catch (ParseException e)
		{
			Log.out.LogException(e, "InitSolidStartTime#开始时间解析错误[" + p_sDayTime + "]");
			throw e;
		}
	}

	public void InitSolidStartTime(String p_sTime) throws ParseException
	{
		try
		{
			Calendar c = TimeMethod.ParseTime(p_sTime);
			_InitSolidTm(c);
		}
		catch (ParseException e)
		{
			Log.out.LogException(e, "InitSolidStartTime#开始时间解析错误[" + p_sTime + "]");
			throw e;
		}
	}
	
	public void InitStartDayTime(String p_sDayTime) throws ParseException
	{
		Calendar c = TimeMethod.ParseDayTime(p_sDayTime);
		_InitStartTime(c);
	}

	public void InitStartTime(String p_sTime) throws ParseException
	{
		Calendar c = TimeMethod.ParseTime(p_sTime);
		_InitStartTime(c);
	}
	
	/**
	 * 执行
	 *
	 * @return 如果下次不需要执行,返回true
	 */
	public boolean Run() throws Exception
	{
		m_Run.OnTick(m_ID);
		if ( m_Num > 0 )
		{
			m_Num--;
			if ( m_Num == 0 )
			{
				return true;
			}
		}
		m_NextRunTime += m_SpaceTime;
		return false;
	}
	
	public String toString()
	{
		return super.toString() + " [ID:" + m_ID + "] NextRunTime[" + m_NextRunTime + "] Run[" + m_Run + "] SpaceTime[" + m_SpaceTime + "]";
	}

	private void _InitSolidTm(Calendar c) throws ParseException
	{
		long stm = c.getTimeInMillis();
		if ( stm > TimeMethod.currentTimeMillis() )
		{
//			Debug.ShowTime(TimeMethod.currentTimeMillis());
//			Debug.ShowTime(stm);
			m_NextRunTime = stm;
		}
		else
		{
			while ( stm < TimeMethod.currentTimeMillis() )
			{
				stm += m_SpaceTime;
				m_NextRunTime = stm;
			}
		}
		System.out.println("下次运行时间:" + Debug.GetShowTime(m_NextRunTime));
	}

	private void _InitStartTime(Calendar c)
	{
		long stm = c.getTimeInMillis();
		if ( stm > TimeMethod.currentTimeMillis() )
		{
			while ( stm > TimeMethod.currentTimeMillis() )
			{
				m_NextRunTime = stm;
				stm -= m_SpaceTime;
			}
		}
		else
		{
			while ( stm < TimeMethod.currentTimeMillis() )
			{
				stm += m_SpaceTime;
				m_NextRunTime = stm;
			}
		}
		System.out.println("下次运行时间:" + Debug.GetShowTime(m_NextRunTime));
	}
}
