/**
 * SQLLogMgrImpl.java 2013-2-4下午5:07:57
 */
package core.detail.impl.mgr;

import java.util.*;

import core.*;
import core.detail.*;
import core.detail.impl.*;
import core.detail.impl.log.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SQLLogMgrImpl implements SQLLogMgr , Runnable
{
	private Queue<String> m_SQL = new LinkedList<String>();
	private Queue<String> m_TempQueue = new LinkedList<String>();
	private boolean m_Run = true;
	private int	m_NeedSaveLogTotal = 0;
	private int m_SaveLogTotal = 0;
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.SQLLogMgr#AddSQLLog(java.lang.String)
	 */
	@Override
	public void AddSQLLog(String sql)
	{
		long stm = System.currentTimeMillis();
		synchronized (m_SQL)
		{
			m_SQL.add(sql);	
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "SQLLogMgrImpl::AddSQLLog用时[[[:" + (System.currentTimeMillis() - stm));
		}
		m_NeedSaveLogTotal++;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		while (m_Run)
		{
			if ( Mgr.GetSqlMgr().BusyRatio() <= RootConfig.GetInstance().ExecuteSaveLogBusyRatio )
			{
				Queue<String> all = _GetQueue(RootConfig.GetInstance().ExecuteSaveLogNum);
				_Execute(all);
			}
			SystemFn.Sleep(200);
		}
		
		Queue<String> all = _GetQueue(100000000);
		_Execute(all);
		
		Mgr.GetThreadMgr().Remove(eThreadType.SQLLOG);
		
		synchronized (this)
		{
			this.notifyAll();
		}
	}
	
	private void _Execute(Queue<String> all)
	{
		int sz = all.size();
		long stm = System.currentTimeMillis();
		while (!all.isEmpty())
		{
			String sql = all.poll();
			try
			{
				Log.ExecuteSQL(sql);
//				DBMgr.SaveLog(sql);
				m_SaveLogTotal++;
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		if (sz != 0)
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQLLOG, sz, System.currentTimeMillis() - stm, m_SQL.size(), m_NeedSaveLogTotal, m_SaveLogTotal);
		}
	}
	
	private Queue<String> _GetQueue(int num)
	{
		m_TempQueue.clear();
		long stm = System.currentTimeMillis();
		synchronized (m_SQL)
		{
			while (!m_SQL.isEmpty())
			{
				m_TempQueue.add(m_SQL.poll());
				if (m_TempQueue.size() >= num )
				{
					break;
				}
			}
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "SQLLogMgrImpl::_GetQueue用时[[[:" + (System.currentTimeMillis() - stm));
		}
		return m_TempQueue;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MgrBase#Stop()
	 */
	@Override
	public void Stop()
	{
		m_Run = false;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MgrBase#IsRun()
	 */
	@Override
	public boolean IsRun()
	{
		return m_Run;
	}

}
