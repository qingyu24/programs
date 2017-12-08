/**
 * LoginMgrImpl.java 2012-6-13下午11:32:42
 */
package core.detail.impl.mgr;

import java.util.*;

import utility.Debug;

import core.*;
import core.detail.*;
import core.detail.impl.eThreadType;
import core.detail.impl.log.*;
import core.detail.interface_.*;
import core.ex.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LogicMgrImpl implements LogicMgr, Runnable
{
	private class FrameExecuter extends ExecuteObj<Frame>
	{
		public FrameExecuter(Frame f, int maxAllowError)
		{
			super(f, maxAllowError);
		}
	}
	
	private class TimerMgrExecuter extends ExecuteObj<TimerMgr>
	{
		public TimerMgrExecuter(TimerMgr t, int maxAllowError)
		{
			super(t, maxAllowError);
		}
	}
	
	private Queue<MethodEx> m_SqlQueue = new LinkedList<MethodEx>();
	private Queue<MethodEx> m_NetQueue = new LinkedList<MethodEx>();
	private Queue<MethodEx> m_TempQueue = new LinkedList<MethodEx>();
	
	private Queue<FrameExecuter> m_Frames = new LinkedList<FrameExecuter>();
	private TimerMgrExecuter m_TimerMgrExecuter;
	private boolean m_RunLoop = true;
	
	private long m_ProcessNum;
	private long m_RecordTime = System.currentTimeMillis();
	private long m_ProcessNumTotal;
	private long m_LogicUseTime = 0;
	private StringBuilder m_RecordDetail = new StringBuilder();
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.LoginMgr#AddNetTask(core.detail.interface_.MethodEx)
	 */
	@Override
	public void AddNetTask(MethodEx mex)
	{
		long stm = System.currentTimeMillis();
		synchronized (m_NetQueue)
		{
			m_NetQueue.add(mex);
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "LogicMgrImpl::AddNetTask用时[[[:" + (System.currentTimeMillis() - stm));
		}
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.LoginMgr#AddSqlTask(core.detail.interface_.MethodEx)
	 */
	@Override
	public void AddSqlTask(MethodEx mex)
	{
		long stm = System.currentTimeMillis();
		synchronized (m_SqlQueue)
		{
			m_SqlQueue.add(mex);
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "LogicMgrImpl::AddSqlTask用时[[[:" + (System.currentTimeMillis() - stm));
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.LogicMgr#AddUpdater(core.Frame)
	 */
	@Override
	public void AddUpdater(Frame f)
	{
		m_Frames.add(new FrameExecuter(f, 10));
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
	 * @see core.detail.interface_.LoginMgr#Run()
	 */
	@Override
	public void run()
	{
		while(m_RunLoop)
		{
			long stm = System.currentTimeMillis();
			
			m_RecordDetail.setLength(0);
			
			_BeginLoop();
			
			_ExecuteSqlQueue();
			
			_ExecuteNetQueue();
			
			_EndLoop();
			
			_Record(stm);
			
			SystemFn.Sleep(1);
		}
		
		_ExecuteLastTask();
		
		Mgr.GetThreadMgr().Remove(eThreadType.LOGIC);
		
		synchronized (this)
		{
			this.notifyAll();
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
	
	private void _AddNetQueue(Queue<MethodEx> queue)
	{
		long stm = System.currentTimeMillis();
		synchronized (m_NetQueue)
		{
			while (!queue.isEmpty())
			{
				MethodEx mex = queue.poll();
				m_NetQueue.add(mex);
			}
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "LogicMgrImpl::_AddNetQueue用时[[[:" + (System.currentTimeMillis() - stm));
		}
	}
	
	private void _BeginLoop()
	{
	}
	
	private void _EndLoop()
	{
		long stm = System.currentTimeMillis();
		_UpdateTimer();
		m_RecordDetail.append("UpdateTimer:");
		m_RecordDetail.append(System.currentTimeMillis() - stm);
		m_RecordDetail.append("\r\n");
		
		stm = System.currentTimeMillis();
		_UpdateFrame();
		m_RecordDetail.append("UpdateFrame:");
		m_RecordDetail.append(System.currentTimeMillis() - stm);
		m_RecordDetail.append("\r\n");
		
		stm = System.currentTimeMillis();
		_UpdateUserMgr();
		m_RecordDetail.append("UpdateUser:");
		m_RecordDetail.append(System.currentTimeMillis() - stm);
		m_RecordDetail.append("\r\n");
	}
	
	/**
	 * 如何还有任何任务没有执行,那么执行
	 */
	private void _ExecuteLastTask()
	{
		_ExecuteSqlQueue();
		
		_ExecuteNetQueue();
	}
	
	/**
	 * 执行Met队列中的MethodEx对象
	 */
	private void _ExecuteNetMethodEx(MethodEx mex, Queue<MethodEx> temp)
	{
		Debug.Assert(mex.GetUser() != null, "MethodEx用户对象不能为空" + mex);
		User p_User = mex.GetUser();
		try
		{
			long stm = System.currentTimeMillis();
			MethodEx.RunResult r = mex.Run();
			m_RecordDetail.append(mex);
			m_RecordDetail.append(",");
			m_RecordDetail.append(p_User.GetRoleGID());
			m_RecordDetail.append(",");
			m_RecordDetail.append(System.currentTimeMillis() - stm);
			m_RecordDetail.append(",");
			m_RecordDetail.append(r);
			m_RecordDetail.append("\r\n");
			switch(r)
			{
			case RUNDIRECT:
				//已经运行了,什么也不用做,自动释放
				m_ProcessNum++;
				m_ProcessNumTotal++;
				break;
			case DATA:
				//数据没加载好,那么加入sql队列
				Mgr.GetSqlMgr().AddTask(mex);
				break;
			case BLOCK:
				//阻塞的对象,等待重新调度
				temp.add(mex);
				break;
			default:
				Debug.Assert(false, "未实现的代码");
			}
		}
		catch (Exception e)
		{
			p_User.LogException(e);
			mex.Close(eCloseReasonType.LOGICRUN_EXCEPTION.ID());
		}
		finally
		{
			p_User.EndMethodMark(mex);
		}
	}

	/**
	 * 执行网络队列里的操作
	 */
	private void _ExecuteNetQueue()
	{
		Queue<MethodEx> queue = _GetNetQueue();
		Queue<MethodEx> temp = new LinkedList<MethodEx>();
		while (!queue.isEmpty())
		{
			MethodEx mex = queue.poll();
			if ( mex.CanExecute() )
			{
				_ExecuteNetMethodEx(mex, temp);
			}
			else
			{
				//TODO 
			}
		}
		_AddNetQueue(temp);
	}
	
	/**
	 * 执行SQL队列中的MethodEx对象
	 */
	private void _ExecuteSQLMethodEx(MethodEx mex)
	{
		Debug.Assert(mex.GetUser() != null, "MethodEx用户对象不能为空" + mex);
		User p_User = mex.GetUser();
		try
		{
			long stm = System.currentTimeMillis();
			mex.RunDirect();
			m_RecordDetail.append(mex);
			m_RecordDetail.append(",");
			m_RecordDetail.append(p_User.GetRoleGID());
			m_RecordDetail.append(",");
			m_RecordDetail.append(System.currentTimeMillis() - stm);
			m_RecordDetail.append("\r\n");
			
			m_ProcessNum++;
			m_ProcessNumTotal++;
		}
		catch (Exception e)
		{
			p_User.LogException(e);
			mex.Close(eCloseReasonType.LOGICSQLRUN_EXCEPTION.ID());
		}
	}
	
	/**
	 * 执行SQL队列里面的操作
	 */
	private void _ExecuteSqlQueue()
	{
		Queue<MethodEx> queue = _GetSQLQueue();
		while (!queue.isEmpty())
		{
			MethodEx mex = queue.poll();
			if ( mex.CanExecute() )
			{
				_ExecuteSQLMethodEx(mex);
			}
			else
			{
				//TODO 
			}
		}
	}
	
	private Queue<MethodEx> _GetNetQueue()
	{
		return _GetQueue(m_NetQueue, 100);
	}
	
	private Queue<MethodEx> _GetQueue(Queue<MethodEx> queue, int maxnum)
	{
		m_TempQueue.clear();
		long stm = System.currentTimeMillis();
		synchronized (queue)
		{
			while(!queue.isEmpty())
			{
				MethodEx mex = queue.poll();
				m_TempQueue.add(mex);
				
				if ( m_TempQueue.size() > maxnum)
				{
					break;
				}
			}
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "LogicMgrImpl::_GetQueue用时[[[:" + (System.currentTimeMillis() - stm));
		}
		return m_TempQueue;
	}
	
	private Queue<MethodEx> _GetSQLQueue()
	{
		return _GetQueue(m_SqlQueue, 100);
	}

	private TimerMgrExecuter _GetTimerMgrExecuter()
	{
		if ( m_TimerMgrExecuter == null )
		{
			m_TimerMgrExecuter = new TimerMgrExecuter(Mgr.GetTimerMgr(), 10);
		}
		return m_TimerMgrExecuter;
	}

	private void _Record(long stm)
	{
		long tm = System.currentTimeMillis() - m_RecordTime;
		if ( tm > 1000 )
		{
			if ( ShowMgr.CanShow() )
			{
				ShowMgr.GetInfo().setLogicNum(m_ProcessNum * 1000 / tm);
				ShowMgr.GetInfo().setProcessLogicNumTotal(m_ProcessNumTotal);
				ShowMgr.GetInfo().setLogicSQLListNum(m_SqlQueue.size());
				ShowMgr.GetInfo().setLogicNetListNum(m_NetQueue.size());
			}
			
			_LogicInfoLog.Log(m_ProcessNum, m_ProcessNumTotal, m_SqlQueue.size(), m_NetQueue.size(), m_LogicUseTime);
			
			m_RecordTime = System.currentTimeMillis();
			m_ProcessNum = 0;
			m_LogicUseTime = 0;
		}
		
		long usetm = System.currentTimeMillis() - stm;
		if ( usetm != 0 )
		{
			if ( ShowMgr.CanShow() )
			{
				ShowMgr.GetInfo().setLogicTime(usetm);
			}
			m_LogicUseTime = usetm > m_LogicUseTime ? usetm : m_LogicUseTime;
			
			if ( usetm >= 500 )
			{
				System.err.println("==================logic use time:" + usetm);
				_LogicDetailInfoLog.LogicLog(usetm ,Mgr.GetNetMgr().GetNum() ,Mgr.GetUserMgr().GetNum(), m_RecordDetail.toString());
			}
		}
	}

	/**
	 * Frame的Update操作
	 */
	private void _UpdateFrame()
	{
		for ( FrameExecuter f : m_Frames )
		{
			if ( !f.CanRun() )
			{
				continue;
			}
			
			try
			{
				f.Get().OnUpdate();
			}
			catch (Exception e)
			{
				f.AddError();
				Log.out.LogException(e, "Frame::Update产生异常次数" + f.GetErrorNum());
				if ( !f.CanRun() )
				{
					Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_UNLOAD_MODULE, f.Get());
				}
			}
		}
	}

	/**
	 * 定时器Update操作
	 */
	private void _UpdateTimer()
	{
		TimerMgrExecuter e = _GetTimerMgrExecuter();
		if ( !e.CanRun() )
		{
			return;
		}
		try
		{
			e.Get().Update();
		}
		catch( Exception e1 )
		{
			e.AddError();
			Log.out.LogException(e1, "TimerMgr::Update产生异常次数" + e.GetErrorNum());
			if ( !e.CanRun() )
			{
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_UNLOAD_MODULE, e.Get());
			}
		}
	}

	/**
	 * UserMgr的Update操作
	 * 
	 * @tip 这个没有TimerMgr或者是Frame对异常检测的原因是,它太核心了,根本就不能允许出现这样的问题,而且如果它出问题了,那还不如把整个系统给停了
	 */
	private void _UpdateUserMgr()
	{
		Mgr.GetUserMgr().Update();
	}
}
