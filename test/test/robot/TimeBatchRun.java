/**
 * TimeBatch.java 2012-7-31上午11:46:14
 */
package test.robot;

import static org.junit.Assert.*;

import test.robot.step.Step;
import test.robot.utility.NameRule;
import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 批量运行的机器人,在规定的时间内不断执行,先执行完的会等待后执行完的,在所有都执行完后进行下一轮,用于峰值的压力测试
 * 
 * 具体的执行由步骤控制,在设定的步骤最大时间内(非每个步骤本身的时间),如果步骤没有执行成功则认为操作失败
 * 如果所有机器人都执行完毕,那么等待5秒后进行下一个循环
 * 否则在步骤设定的时间内一直等待,直到时间结束.时间结束时,则认为没有执行完成的机器人执行失败
 * 在进入下一个循环前,如果没有一个机器人执行成功,则退出循环
 */
public class TimeBatchRun implements Runnable
{
	public class CheckFinish implements Runnable
	{
		private long m_EndTime = 0;
		private boolean[] m_Run = null;
		private boolean[] m_Res = null;
		private Step[] m_Steps = null;
		private boolean m_Loop = true;
		private long m_StartTime = 0;
		private int m_LastRun = 0;
		
		public CheckFinish(int maxtm, int num)
		{
			m_LastRun = 0;
			m_StartTime = System.currentTimeMillis();
			m_Run = new boolean[num];
			m_Res = new boolean[num];
			m_Steps = new Step[num];
			for ( int i = 0; i < num; ++i )
			{
				m_Run[i] = false;
				m_Res[i] = false;
			}
			for ( int i = 0; i < m_Steps.length; ++i )
			{
				m_Steps[i] = m_Step.Clone();
			}
			m_EndTime = System.currentTimeMillis() + maxtm;
		}
		
		@Override
		public void run()
		{
			_ShowResult();
			while ( m_Loop && System.currentTimeMillis() < m_EndTime )
			{
				try
				{
					for ( int i = 0; i < m_Run.length; ++i )
					{
						LoopRun(i);
					}
					_ShowResult();
					
					Thread.sleep(1);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					Stop();
				}
			}
		}
		
		private void _ShowResult()
		{
			if ( System.currentTimeMillis() - m_StartTime > 5000 )
			{
				m_StartTime = System.currentTimeMillis();
				int run = 0, res = 0;
				for ( boolean c : m_Run )
				{
					if ( c )
					{
						run++;
					}
				}
				
				for ( boolean c : m_Res )
				{
					if ( c )
					{
						res++;
					}
				}
				System.out.println(Debug.GetCurrTime() + "\tRobot run[" + run + "] res[" + res + "]");
				
				if ( m_LastRun != 0 && run <= m_LastRun )
				{
					Stop();
				}
				m_LastRun = run;
			}
		}

		private void LoopRun(int i) throws Exception
		{
			Robot r = Robot.GetRobots(i);
			
			LoopExecute(i, r);
			LoopExecuted(i, r);
				
			CheckAllFinish();
		}
		
		private void LoopExecute(int i, Robot r) throws InterruptedException
		{
			//如果没执行完,那么就执行,如果是多重执行,由Step对象控制
			if ( !m_Run[i] )
			{
				boolean c = m_Steps[i].Execute(r);
				if ( c )
				{
					m_Run[i] = true;
				}
			}
		}
		
		private void LoopExecuted(int i, Robot r) throws Exception
		{
			//是否获得了结果,如果没获得,那么就再查查看是否已经有结果了
			if ( !m_Res[i] )
			{
				boolean c = m_Steps[i].Executed(r);
				if ( c )
				{
					m_Res[i] = true;
					if ( m_Steps[i].Result(r) )
					{
						m_RobotRes++;
					}
				}
			}
		}
		
		private boolean IsAllFinish()
		{
			for ( boolean c : m_Run )
			{
				if ( !c )
				{
					return false;
				}
			}
			
			for ( boolean c : m_Res )
			{
				if ( !c )
				{
					return false;
				}
			}
			return true;
		}
		
		private void CheckAllFinish()
		{
			if ( IsAllFinish() )
			{
				Stop();
			}
		}
		
		private void Stop()
		{
			m_Loop = false;
			Notify();
		}
	}
	
	private int m_MaxTime;
	private int m_RobotNum;
	private int m_RobotRes;
	private int m_WaitTm;
	private int m_MaxRunNum = -1;
	private int m_RunNum = 0;
	private long m_EndTime;
	private int m_RunWaitTime = 5000;
	private Step m_Step;
	private NameRule m_LoginRule = null;
	private NameRule m_CreateRoleRule = null;
	
	public TimeBatchRun(int maxruntm, int robotnum, int maxwaittm)
	{
		this(maxruntm, robotnum, maxwaittm, null ,null);
	}
	
	/**
	 * @param maxruntm 最大运行时间(毫秒)
	 * @param robotnum 机器人数量(单机最好不要超过500)
	 * @param maxwaittm 单次运行的最大等待时间(建议10s以上)(毫秒)
	 */
	public TimeBatchRun(int maxruntm, int robotnum, int maxwaittm, NameRule loginRule, NameRule createRoleRule)
	{
		m_RunNum = 0;
		m_MaxTime = maxruntm;
		m_RobotNum = robotnum;
		m_WaitTm = maxwaittm;
		m_EndTime = System.currentTimeMillis() + m_MaxTime;
		m_LoginRule = loginRule;
		m_CreateRoleRule = createRoleRule;
		Debug.Assert(m_MaxTime > 0, "");
		Debug.Assert(m_RobotNum > 0, "");
		Debug.Assert(m_WaitTm > 0, "");
	}
	
	public void SetMaxRunNum(int num)
	{
		m_MaxRunNum = num;
	}
	
	public void SetRunWaitTime(int tm)
	{
		m_RunWaitTime = tm;
	}
	
	/**
	 * 设置执行步骤
	 */
	public void SetStep(Step step)
	{
		m_Step = step;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		try
		{
			Debug.Assert(m_Step != null, "");
			int n = 0;
			while ( CanRun() )
			{
				m_RobotRes = 0;
				for ( int i = 0; i < m_RobotNum; ++i )
				{
					Robot.CreateRobots(i, m_LoginRule, m_CreateRoleRule);
				}
				
				new Thread(new CheckFinish(m_WaitTm, m_RobotNum), "CheckThread").start();
				Wait(m_WaitTm);
				
				System.out.println("# [" + ++n + "] Execute[" + m_RobotRes + "]");
//				assertEquals("没有一个机器人成功!", m_RobotRes > 0 , true);
				
				for ( int i = 0; i < m_RobotNum; ++i )
				{
					Robot r = Robot.GetRobots(i);
					r.Close(0 , 0);
				}
				
				Thread.sleep(m_RunWaitTime);
				
				m_RunNum++;
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
			fail("抛出了异常:" + e);
		}
		finally
		{
			for ( int i = 0; i < m_RobotNum; ++i )
			{
				Robot r = Robot.GetRobots(i);
				if ( r != null )
				{
					r.Finish();
				}
			}
		}
	}
	
	private boolean CanRun()
	{
		if ( m_MaxRunNum > 0 && m_RunNum >= m_MaxRunNum )
		{
			return false;
		}
		
		return System.currentTimeMillis() < m_EndTime;
	}
	
	private void Wait(int waittm) throws InterruptedException
	{
		synchronized (this)
		{
			this.wait(waittm);
		}
	}
	
	private void Notify()
	{
		synchronized (this)
		{
			this.notify();
		}
	}
}
