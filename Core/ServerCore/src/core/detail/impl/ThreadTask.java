/**
 * ThreadTask.java 2012-10-15下午4:18:01
 */
package core.detail.impl;

import java.util.*;
import java.util.Map.*;

import core.detail.*;
import core.detail.impl.log.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ThreadTask
{
	private class Execute implements Runnable
	{
		private ArrayList<Runnable> runs = new ArrayList<Runnable>();
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			while ( m_Run )
			{
				runs.clear();
				synchronized (m_All)
				{
					Iterator<Entry<Object, Runnable>> it = m_All.entrySet().iterator();
					while ( it.hasNext() )
					{
						Entry<Object, Runnable> e = it.next();
						runs.add(e.getValue());
					}
					m_All.clear();
				}
				
				for ( Runnable r : runs )
				{
					try
					{
						r.run();
					}
					catch(Exception e)
					{
						Log.out.LogException(e);
					}
				}
				
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					Log.out.LogException(e);
				}
			}
			
			Mgr.GetThreadMgr().Remove(eThreadType.THREADTASK);
			
			synchronized (m_Instance)
			{
				m_Instance.notifyAll();
			}
		}
	}
	private static ThreadTask m_Instance = new ThreadTask();
	public static ThreadTask GetInstance()
	{
		return m_Instance;
	}
	private Map<Object, Runnable> m_All = new HashMap<Object, Runnable>();
	
	private Execute m_Execute = new Execute();
	
	private boolean m_Run = true;
	
	public ThreadTask()
	{
		Thread t = new Thread(m_Execute);
		t.start();
		Mgr.GetThreadMgr().Reg(t, eThreadType.THREADTASK);
	}
	
	public void AddTask(Object execute, Runnable r)
	{
		synchronized (m_All)
		{
			m_All.put(execute, r);
		}
	}
	
	public boolean IsRun()
	{
		return m_Run;
	}

	public void Stop()
	{
		m_Run = false;
	}
}
