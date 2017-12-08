/**
 * TimerMgrImpl.java 2012-7-7下午1:51:18
 */
package core.detail.impl.mgr;

import java.text.ParseException;
import java.util.*;

import utility.*;

import core.*;
import core.detail.*;
import core.detail.impl.*;
import core.detail.impl.log.*;
import core.detail.impl.mgr.timer.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 * 
 *          这个目前还不支持在运行时刻添加删除定时器,真要的话以后加吧
 */
public class TimerMgrImpl implements TimerMgr
{
	private Set<TickEvent>			m_All	= new TreeSet<TickEvent>();
	private ArrayList<TickEvent>	m_ReAdd	= new ArrayList<TickEvent>();
	private ArrayList<Long>			m_RemoveList = new ArrayList<Long>();
	private ArrayList<TickEvent>	m_WaitAdd = new ArrayList<TickEvent>();
	private ArrayList<Integer>		m_DelMarks = new ArrayList<Integer>();
	private boolean					m_Update = false;

	/* (non-Javadoc)
	 * @see core.detail.interface_.TimerMgr#AddDefineDayTimer(int, core.Tick, java.lang.String, int)
	 */
	@Override
	public long AddDefineDayTimer(int p_nSecond, Tick tick, String p_sDayTime, int markID) throws ParseException
	{
		TickEvent e = new TickEvent(tick, p_nSecond, -1, markID);
		e.InitStartDayTime(p_sDayTime);
		return _AddTickEvent(e);
	}

	/*
	 * (non-Javadoc)
	 * @see core.detail.interface_.TimerMgr#AddDefineTimer(int, core.Tick,
	 * java.lang.String)
	 */
	@Override
	public long AddDefineTimer(int p_nSecond, Tick tick, String p_sTime ,int markID) throws ParseException
	{
		TickEvent e = new TickEvent(tick, p_nSecond, -1, markID);
		e.InitStartTime(p_sTime);
		return _AddTickEvent(e);
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.TimerMgr#AddMilliTimer(long, core.Tick, int)
	 */
	@Override
	public long AddMilliTimer(long p_nMilliSecond, Tick tick, int num ,int markID)
	{
		return _AddTickEvent(new TickEvent(tick, p_nMilliSecond, num, markID));
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.TimerMgr#AddSolidDefineDayTimer(int, core.Tick, java.lang.String, int)
	 */
	@Override
	public long AddSolidDefineDayTimer(int p_nSecond, Tick tick, String p_sDayTime, int markID) throws ParseException
	{
		TickEvent e = new TickEvent(tick, p_nSecond, -1, markID);
		e.InitSolidStartDayTime(p_sDayTime);
		return _AddTickEvent(e);
	}

	/*
	 * (non-Javadoc)
	 * @see core.detail.interface_.TimerMgr#AddSolidDefineTimer(int, core.Tick,
	 * java.lang.String)
	 */
	@Override
	public long AddSolidDefineTimer(int p_nSecond, Tick tick, String p_sTime ,int markID) throws ParseException
	{
		TickEvent e = new TickEvent(tick, p_nSecond, -1, markID);
		e.InitSolidStartTime(p_sTime);
		return _AddTickEvent(e);
	}
	
	/*
	 * (non-Javadoc)
	 * @see core.detail.interface_.TimerMgr#AddTimer(int, core.Tick, int)
	 */
	@Override
	public long AddTimer(int p_nSecond, Tick tick, int num ,int markID)
	{
		return _AddTickEvent(new TickEvent(tick, p_nSecond, num, markID));
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.TimerMgr#RemoveBatchByMarkID(int)
	 */
	@Override
	public void RemoveBatchByMarkID(int markID)
	{
		if (m_Update)
		{
			m_DelMarks.add(markID);
		}
		else
		{
			_RemoveTimerByMarkID(markID);
		}
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.TimerMgr#RemoveTimer(long)
	 */
	@Override
	public void RemoveTimer(long p_lTimerID)
	{
		Debug.Assert( Mgr.GetThreadMgr().GetCurrThreadType() == eThreadType.LOGIC, "" );
		//这种写法表示可以在OnTick的回调中删除某个定时器,当然是在下个Update的时候
		m_RemoveList.add(p_lTimerID);
	}
	
	/*
	 * (non-Javadoc)
	 * @see core.detail.interface_.TimerMgr#Update()
	 */
	@Override
	public void Update()
	{
		_RemoveAllWaitRemoveTimer();
		_RemoveAllWaitRemoveTimerByMarkID();
		_AddWaitAddTimer();
		
		if (m_All.isEmpty() && m_ReAdd.isEmpty())
		{
			return;
		}
		
		m_Update = true;

		_ExecuteTicks();
		_ReAddTicks();
		
		m_Update = false;
	}

	private long _AddTickEvent(TickEvent tickEvent)
	{
		//目前是还不允许在OnTick中创建定时器,好像也没这需求,先这样
		if ( m_Update )
		{
			m_WaitAdd.add(tickEvent);
		}
		else
		{
			Debug.Assert( Mgr.GetThreadMgr().GetCurrThreadType() == eThreadType.LOGIC ||
					Mgr.GetThreadMgr().GetCurrThreadType() == eThreadType.MAIN, "");
			m_ReAdd.add(tickEvent);
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_ADD_TIMER, tickEvent.GetID(), tickEvent);
			
		}
		return tickEvent.GetID();
	}

	private void _AddWaitAddTimer()
	{
		for (TickEvent e : m_WaitAdd)
		{
			_AddTickEvent(e);
		}
		m_WaitAdd.clear();
	}

	/**
	 * 执行所有可以执行的定时器
	 */
	private void _ExecuteTicks()
	{
		Iterator<TickEvent> it = m_All.iterator();
		while (it.hasNext())
		{
			TickEvent e = it.next();
			if (!e.CanRun())
			{
				//因为队列是按照执行的顺序来排序,那么当一个不能执行,后面的也肯定不能执行
				break;
			}
			else
			{
				_Run(e);
				it.remove();
			}
		}
	}

	/**
	 * 对于要执行多次的Tick,重新插入到执行队列
	 */
	private void _ReAddTicks()
	{
		for (TickEvent e : m_ReAdd)
		{
			m_All.add(e);
		}
		m_ReAdd.clear();
	}
	
	private void _RemoveAllWaitRemoveTimer()
	{
		if ( !m_RemoveList.isEmpty() )
		{
			for ( long tid : m_RemoveList )
			{
				boolean c = _RemoveTimer(tid);
				if ( !c )
				{
					Log.out.Log(eSystemWarningLogType.SYSTEM_WARNING_REMOVE_UNFIND_TIMER_EVENT, tid);
				}
			}
			m_RemoveList.clear();
		}
	}
	
	private void _RemoveAllWaitRemoveTimerByMarkID()
	{
		for (int markid : m_DelMarks)
		{
			_RemoveTimerByMarkID(markid);
		}
		m_DelMarks.clear();
	}

	private boolean _RemoveTimer(long p_lTimerID)
	{
		Iterator<TickEvent> it = m_All.iterator();
		while (it.hasNext())
		{
			TickEvent e = it.next();
			if (e.GetID() == p_lTimerID)
			{
				m_All.remove(e);
				
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_REMOVE_TIMER_MANUAL, e.GetID());
				return true;
			}
		}
		
		for (TickEvent e : m_ReAdd)
		{
			if ( e.GetID() == p_lTimerID )
			{
				m_ReAdd.remove(e);
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_REMOVE_TIMER_MANUAL, e.GetID());
				return true;
			}
		}
		return false;
	}

	private void _RemoveTimerByMarkID(int markID)
	{
		Iterator<TickEvent> it = m_All.iterator();
		while (it.hasNext())
		{
			TickEvent e = it.next();
			if (e.GetMarkID() == markID)
			{
				it.remove();
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_REMOVE_TIMER_DISCONNECT, e.GetID());
			}
		}
	}

	private void _Run(TickEvent e)
	{
		boolean remove = true;
		try
		{
			remove = e.Run();
		}
		catch (Exception e1)
		{
			//所有执行出问题的定时器都将移除
			Log.out.LogException(e1);
		}
		finally
		{
			if (!remove)
			{
				m_ReAdd.add(e);
			}
			else
			{
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_REMOVE_TIMER, e.GetID());
			}
		}
	}
}
