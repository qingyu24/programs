/**
 * SqlMgrImpl.java 2012-6-13下午11:49:45
 */
package core.detail.impl.mgr;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.*;
import core.detail.*;
import core.detail.impl.*;
import core.detail.impl.log.*;
import core.detail.interface_.*;
import core.ex.ShowMgr;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SqlMgrImpl implements SqlMgr, Runnable
{
	private Queue<MethodEx> m_All = new LinkedList<MethodEx>();
	private Queue<MethodEx> m_Run = new LinkedList<MethodEx>();
	
	private int m_RunNum = 5;	///<一次小循环执行的次数
	private long m_ProcessNum;
	private long m_RecordTime = System.currentTimeMillis();
	private long m_ProcessNumTotal;
	private boolean m_RunLoop = true;
	private long m_MaxUpdateTime = 0;
	private long m_LastExecuteSQLUpdate = 0;
	private StringBuilder m_RecordDetail = new StringBuilder();
	
	private Queue<Long> m_BusyFlag = new ConcurrentLinkedQueue<Long>();
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.SqlMgr#AddTask(core.detail.interface_.MethodEx)
	 */
	@Override
	public void AddTask(MethodEx mex)
	{
		long stm = System.currentTimeMillis();
		synchronized (m_All)
		{
			m_All.add(mex);
			_SQLMethodDetailLog.Log(mex, eSQLMethodType.INSERT, System.currentTimeMillis() - stm, m_All.size(), 0);
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "SqlMgrImpl::AddTask用时[[[:" + (System.currentTimeMillis() - stm));
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.SqlMgr#BusyRatio()
	 */
	@Override
	public int BusyRatio()
	{
		long total = 0;
		for ( long tm : m_BusyFlag )
		{
			total += tm;
		}
		return (int) (total / 300);
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.SqlMgr#IsBusy()
	 */
	@Override
	public boolean IsBusy()
	{
		for ( long tm : m_BusyFlag )
		{
			if ( tm > 300 )
			{
				return true;
			}
		}
		if ( m_All.size() > 50 )
		{
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MgrBase#IsRun()
	 */
	@Override
	public boolean IsRun()
	{
		return m_RunLoop;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.SqlMgr#Run()
	 */
	@Override
	public void run()
	{
		while(m_RunLoop)
		{
			long stm = System.currentTimeMillis();
			
			m_RecordDetail.setLength(0);
			
			while (true)
			{
				Queue<MethodEx> all = _GetRunMethodEx();
				
				if ( all.isEmpty() )
				{
					break;
				}
				
				_ExecuteSQL(all);
				all.clear();
				
				if ( System.currentTimeMillis() - stm > 500 )
				{
					break;
				}
			}
			
			_EndLoop(stm);
			
			_SetBusyTime(stm);
			
			_Record(stm);
			
			SystemFn.Sleep(1);
		}
		
		_ExecuteLastTask();
		
		Mgr.GetThreadMgr().Remove(eThreadType.SQL);
		
		synchronized (this)
		{
			notifyAll();
		}
	}
	
	private void _EndLoop(long startlooptime)
	{
		if ( System.currentTimeMillis() - m_LastExecuteSQLUpdate > 5000 &&
			 System.currentTimeMillis() - startlooptime < 300 )
		{
			DBMgr.Update();
			m_LastExecuteSQLUpdate = System.currentTimeMillis();
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MgrBase#Stop()
	 */
	@Override
	public void Stop()
	{
		m_RunLoop = false;
	}
	
	private void _AddToLogic(MethodEx mex)
	{
		Mgr.GetLogicMgr().AddSqlTask(mex);
	}
	
	/**
	 * 如何还有任何任务没有执行,那么执行
	 */
	private void _ExecuteLastTask()
	{
		m_RunNum = 100000000;
		Queue<MethodEx> all = _GetRunMethodEx();
		_ExecuteSQL(all);
		all.clear();
	}

	private void _ExecuteSQL(Queue<MethodEx> all)
	{
		for ( MethodEx ex : all )
		{
			User p_User = ex.GetUser();
			try
			{
				if ( p_User != null )
				{
					p_User.StartMethodMark(ex);
				}
				long stm = System.currentTimeMillis();
				ex.SqlRun();
				m_RecordDetail.append(ex);
				m_RecordDetail.append(",");
				m_RecordDetail.append(p_User.GetRoleGID());
				m_RecordDetail.append(",");
				m_RecordDetail.append(System.currentTimeMillis() - stm);
				m_RecordDetail.append("\r\n");
//				System.out.println("########### SQLExecute:" + ex + " starttm:" + stm + " endtm:" + System.currentTimeMillis() + " Use[" + (System.currentTimeMillis() - stm) + "]");
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, ex);
				
//				_SQLMethodDetailLog.Log(ex, eSQLMethodType.EXECUTE, System.currentTimeMillis() - stm, m_All.size(), all.size());
				_AddToLogic(ex);
				m_ProcessNum++;
				m_ProcessNumTotal++;
			}
			catch (Exception e)
			{
				if ( p_User != null )
				{
					p_User.LogException(e);
				}
				else
				{
					Log.out.LogException(e);
				}
				ex.Close(eCloseReasonType.SQLRUN_EXCEPTION.ID());
				ex.Finish();
			}
			finally
			{
				if ( p_User != null )
				{
					p_User.EndMethodMark(ex);
				}
			}
		}
	}

	private Queue<MethodEx> _GetRunMethodEx()
	{
		MethodEx m = null;
		long stm = System.currentTimeMillis();
		synchronized (m_All)
		{
			while(!m_All.isEmpty())
			{
				m = m_All.poll();
				
				if ( m.IsAllDataReady() )
				{
					_AddToLogic(m);
					m = null;
				}
				else
				{
					m_Run.add(m);
					if ( m_Run.size() > m_RunNum)
					{
						break;
					}
				}
			}
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "SqlMgrImpl::_GetRunMethodEx用时[[[:" + (System.currentTimeMillis() - stm));
		}
		return m_Run;
	}

	private void _Record(long stm)
	{
		long tm = System.currentTimeMillis() - m_RecordTime;
		if ( tm > 500 )
		{
			if ( ShowMgr.CanShow() )
			{
				ShowMgr.GetInfo().setSqlNum(m_ProcessNum * 1000 / tm);
				ShowMgr.GetInfo().setRunSQLNumTotal(m_ProcessNumTotal);
				ShowMgr.GetInfo().setSQLListNum(m_All.size());
			}
			
			_SQLInfoLog.Log(m_ProcessNum, m_ProcessNumTotal, m_All.size(), m_MaxUpdateTime);
			m_RecordTime = System.currentTimeMillis();
			m_ProcessNum = 0;
		}
		
		long usetm = System.currentTimeMillis() - stm;
		if ( usetm > 0 )
		{
			if ( ShowMgr.CanShow() )
			{
				ShowMgr.GetInfo().setSQLUseTime(usetm);
			}
			m_MaxUpdateTime = usetm > m_MaxUpdateTime ? usetm : m_MaxUpdateTime;
			
			if ( usetm >= 1000 )
			{
				_LogicDetailInfoLog.SQLLog(usetm, Mgr.GetNetMgr().GetNum() ,Mgr.GetUserMgr().GetNum(), m_RecordDetail.toString());
			}
		}
	}

	private void _SetBusyTime(long stm)
	{
		long usetm = System.currentTimeMillis() - stm;
		usetm = usetm <= 0 ? 1 : usetm;
		m_BusyFlag.add(usetm);
		if ( m_BusyFlag.size() > 5 )
		{
			m_BusyFlag.poll();
		}
	}
}
