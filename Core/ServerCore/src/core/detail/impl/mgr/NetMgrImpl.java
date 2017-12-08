/**
 * NetMgrImpl.java 2012-6-16下午9:13:55
 */
package core.detail.impl.mgr;


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
public class NetMgrImpl implements NetMgr, Runnable
{
	//最大重复初始化的次数
	private static final int	m_MaxInitNum = 5;
	
	private Listen	 	m_Listen = new ListenNio();
	private int 		m_CurrPort = 0;
	private int			m_UsePort = 0;
	private boolean 	m_Init = false;
	private int			m_InitNum = 0;
	private boolean		m_Run = true;
	private boolean		m_AutoChangePort = false;
	private eThreadType	m_ThreadType;
	
	/**
	 * @param canAutoChangePort 设置为true时,如果预设的端口出现问题,那么会用+1的端口重新初始化,如果+1的失败,那么会再用预设的端口初始化,如此重复5次
	 */
	public NetMgrImpl(Listen l, int port, boolean canAutoChangePort, eThreadType type)
	{
		m_Listen = l;
		m_UsePort = port;
		m_AutoChangePort = canAutoChangePort;
		m_ThreadType = type;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MgrBase#IsRun()
	 */
	@Override
	public boolean IsRun()
	{
		return m_Run;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.NetMgr#Reset()
	 */
	@Override
	public void Reset()
	{
		m_Run = true;
		m_Init = false;
		m_InitNum = 0;
		m_CurrPort = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		while ( m_Run && !m_Init )
		{
			_StartListen();
		}
		
		Mgr.GetThreadMgr().Remove(m_ThreadType);
		
		synchronized (this)
		{
			notifyAll();
		}
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.NetMgr#Stop()
	 */
	@Override
	public void Stop()
	{
//		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_STOP_USER_ENTRY, "NetMgrImple::Start");
		m_Run = false;
		m_Listen.Stop();
//		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_STOP_USER_ENTRY, "NetMgrImple::Finish");
	}

	private int _GetPort()
	{
		if ( m_AutoChangePort )
		{
			if ( m_CurrPort == 0 )
			{
				m_CurrPort = m_UsePort;
			}
			else
			{
				if ( m_CurrPort == m_UsePort )
				{
					m_CurrPort++;
				}
				else
				{
					m_CurrPort = m_UsePort;
				}
			}
			return m_CurrPort;
		}
		else
		{
			return m_UsePort;
		}
	}

	private boolean _NeedRetry()
	{
		if ( m_AutoChangePort )
		{
			return m_InitNum < m_MaxInitNum;
		}
		else
		{
			return false;
		}
	}

	private void _StartListen()
	{
		try
		{
			m_InitNum++;
			m_Listen.Init(_GetPort());
			m_Listen.StartListen();	///<正常会一直卡在这监听网络数据
		}
		catch (Exception e)
		{
			Log.out.LogException(e);
			m_Listen.Reset();
			if ( _NeedRetry() )
			{
				m_Init = false;
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_REOPEN_PORT);
			}
			else
			{
				m_Run = false;
				CrashDoctor.GetInstance().OnCrashThread(Thread.currentThread().getId());
			}
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.NetMgr#GetNum()
	 */
	@Override
	public int GetNum()
	{
		if ( m_Listen != null )
		{
			return m_Listen.GetLinkNum();
		}
		return 0;
	}
}
