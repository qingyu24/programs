/**
 * ManagerMgr.java 2012-6-11下午10:56:01
 */
package core.detail.impl.mgr;

//import utility.*;
import core.*;
import core.detail.*;
import core.detail.impl.*;
import core.detail.impl.log.*;
import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ManagerMgr
{
	private static ManagerMgr m_Instance = new ManagerMgr();
	
	public static ManagerMgr GetInstance()
	{
		return m_Instance;
	}
	
	private NetMgrImpl		m_NetMgr	= new NetMgrImpl(new ListenNio(), RootConfig.GetInstance().ServerPort, true, eThreadType.NET);
	private LogicMgrImpl	m_LogicMgr	= new LogicMgrImpl();
	private SqlMgrImpl		m_SQLMgr	= new SqlMgrImpl();
	private UserMgrImpl		m_UserMgr	= new UserMgrImpl();
	private TimerMgrImpl	m_TimerMgr	= new TimerMgrImpl();
	private ThreadMgrImpl	m_ThreadMgr	= new ThreadMgrImpl();
	private SQLLogMgrImpl	m_SqlLogMgr	= new SQLLogMgrImpl();
	
	private NetMgrImpl		m_GmNetMgr	= new NetMgrImpl(new ListenNioGM(), RootConfig.GetInstance().GMPort, false, eThreadType.GMNET);

	public NetMgr GetGMNetMgr()
	{
		return m_GmNetMgr;
	}

	public LogicMgr GetLogicMgr()
	{
		return m_LogicMgr;
	}
	
	public SqlMgr GetSQLMgr()
	{
		return m_SQLMgr;
	}
	
	public ThreadMgr GetThreadMgr()
	{
		return m_ThreadMgr;
	}
	
	public TimerMgr GetTimerMgr()
	{
		return m_TimerMgr;
	}
	
	public UserMgr GetUserMgr()
	{
		return m_UserMgr;
	}
	
	public NetMgr GetNetMgr()
	{
		return m_NetMgr;
	}
	
	public SQLLogMgr GetSQLLogMgr()
	{
		return m_SqlLogMgr;
	}

	public void StartAllThread()
	{
		Thread.setDefaultUncaughtExceptionHandler(ThreadExceptionProcess.GetInstance());
			
		Thread io = new Thread(m_NetMgr, "NetIO");
		Thread logic = new Thread(m_LogicMgr, "Logic");
		Thread sql = new Thread(m_SQLMgr, "SQL");
		Thread gmio = new Thread(m_GmNetMgr, "GMNetIO");
		Thread sqllog = new Thread(m_SqlLogMgr, "SQLLog");
		
		io.start();
		logic.setPriority(8);
		logic.start();
		sql.start();
		gmio.start();
		sqllog.start();
		
		Mgr.GetThreadMgr().Reg(io, eThreadType.NET);
		Mgr.GetThreadMgr().Reg(logic, eThreadType.LOGIC);
		Mgr.GetThreadMgr().Reg(sql, eThreadType.SQL);
		Mgr.GetThreadMgr().Reg(gmio, eThreadType.GMNET);
		Mgr.GetThreadMgr().Reg(sqllog, eThreadType.SQLLOG);
	}
	
	public void StopAllThread()
	{
		try
		{
			_StopNetThread();
		}
		catch(Exception e)
		{
			Log.out.LogException(e);
		}
		
		try
		{
			_StopLogicThread();
		}
		catch(Exception e)
		{
			Log.out.LogException(e);
		}
		
		try
		{
			_StopSQLThread();
		}
		catch(Exception e)
		{
			Log.out.LogException(e);
		}
		
		try
		{
			_StopSQLLogThread();
		}
		catch(Exception e)
		{
			Log.out.LogException(e);
		}
		
		try
		{
			_StopThreadTask();
		}
		catch(Exception e)
		{
			Log.out.LogException(e);
		}
	
		try
		{
			_StopGMNetThread();
		}
		catch(Exception e)
		{
			Log.out.LogException(e);
		}
	
		try
		{
			_StopMainThread();
		}
		catch(Exception e)
		{
			Log.out.LogException(e);
		}
		
//		Debug.Assert(Mgr.GetThreadMgr().IsEmpty(), "所有的线程必须都关闭完成");
	}

	/**
	 * 停掉用户的入口,让用户无法登陆
	 */
	public void StopUserEntry()
	{
		try
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_STOP_USER_ENTRY, "StopUserEntry::Start");
			m_NetMgr.Stop();
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_STOP_USER_ENTRY, "StopUserEntry::Finish");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void _StopMainThread()
	{
		synchronized (Root.GetInstance())
		{
			Root.GetInstance().notifyAll();
		}
	}
	
	private void _StopGMNetThread()
	{
		if ( !m_GmNetMgr.IsRun() )
		{
			return;
		}
		
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_START_STOP_THREAD, "GMNetMgr");
		m_GmNetMgr.Stop();
		_WaitStopThread(m_GmNetMgr, "GMNetMgr", RootConfig.GetInstance().SD_CommonThreadWaitTime * 1000);
	}
	
	private void _StopLogicThread()
	{
		if ( !m_LogicMgr.IsRun() )
		{
			return;
		}
		
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_START_STOP_THREAD, "LogicMgr");
		m_LogicMgr.Stop();
		_WaitStopThread(m_LogicMgr, "LogicMgr", RootConfig.GetInstance().SD_CommonThreadWaitTime * 1000);
	}
	
	private void _StopNetThread()
	{
		if ( !m_NetMgr.IsRun() )
		{
			return;
		}
		
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_START_STOP_THREAD, "NetMgr");
		m_NetMgr.Stop();
		_WaitStopThread(m_NetMgr, "NetMgr", RootConfig.GetInstance().SD_CommonThreadWaitTime * 1000);
	}
	
	private void _StopSQLThread()
	{
		if ( !m_SQLMgr.IsRun() )
		{
			return;
		}
		
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_START_STOP_THREAD, "SQLMgr");
		m_SQLMgr.Stop();
		_WaitStopThread(m_SQLMgr, "SQLMgr", RootConfig.GetInstance().SD_SQLThreadWaitTime * 1000);
	}
	
	private void _StopSQLLogThread()
	{
		if ( !m_SqlLogMgr.IsRun() )
		{
			return;
		}
		
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_START_STOP_THREAD, "SQLLogMgr");
		m_SqlLogMgr.Stop();
		_WaitStopThread(m_SqlLogMgr, "SQLLogMgr", RootConfig.GetInstance().SD_LogThreadWaitTime * 1000);
	}
	
	private void _StopThreadTask()
	{
		if (!ThreadTask.GetInstance().IsRun())
		{
			return;
		}
		
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_START_STOP_THREAD, "ThreadTask");
		ThreadTask.GetInstance().Stop();
		_WaitStopThread(ThreadTask.GetInstance(), "ThreadTask", RootConfig.GetInstance().SD_CommonThreadWaitTime * 1000);
	}
	
	private void _WaitStopThread(Object o, String s, int tm)
	{
		synchronized (o)
		{
			try
			{
				if ( tm > 0 )
				{
					o.wait(tm);
				}
				else
				{
					o.wait();
				}
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_THREAD_STOP, "[" + s + "正常退出]");
			}
			catch (InterruptedException e)
			{
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_THREAD_STOP, "[" + s + "退出时出现异常:]" + e.getMessage());
			}
		}
	}
}
