/**
 * FreeRun.java 2012-8-4上午11:11:59
 */
package test.robot;

import java.io.IOException;
import java.net.UnknownHostException;

import test.robot.step.*;
import test.robot.utility.NameRule;
import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 自由运行的机器人,在规定的时间内,每个机器人会不断的执行操作,并不等待其他机器人,自己执行完毕后会重置,再重新执行,用于长时间的测试
 */
public class FreeRun implements Runnable
{
	private int m_MaxTime;
	private int m_RobotNum;
	private int m_RobotRes;
	private long m_EndTime;
	private Step m_CloneStep;
	private RunRobot[] m_Robots;
	private NameRule m_LoginRule = null;
	private NameRule m_CreateRoleRule = null;
	
	private enum State
	{
		INIT,
		RUN,
		FINISH,
		WAITINIT,
	}
	
	private class RobotData
	{
		private Step m_Step;
		private State m_State;
		private boolean m_bExecute = false;
		private boolean m_bExecuted = false;
		private boolean m_bSuccee = false;
		private long m_InitTime;
		
		public RobotData(Step s)
		{
			m_Step = s.Clone();
			m_State = State.RUN;
		}
		
		/**
		 * 步骤是否完成,并不代表是否结束
		 */
		public boolean IsFinish()
		{
			return m_State == State.FINISH;
		}
		
		public boolean IsExecute()
		{
			return m_State == State.RUN;
		}
		
		public boolean CanInit()
		{
			return m_State == State.WAITINIT && System.currentTimeMillis() >= m_InitTime;
		}
		
		/**
		 * 任务的结果,在步骤完成后可以获取
		 */
		public boolean Result()
		{
			return m_bSuccee;
		}
		
		public void Execute(Robot r) throws Exception
		{
			if ( !m_bExecute )
			{
				boolean c = m_Step.Execute(r);
				if ( c )
				{
					m_bExecute = true;
				}
			}
			
			if ( !m_bExecuted )
			{
				boolean c = m_Step.Executed(r);
				if ( c )
				{
					m_bExecuted = true;
					m_bSuccee = m_Step.Result(r);
					
					m_State = State.FINISH;
				}
			}
		}

		public void Reset()
		{
			m_State = State.WAITINIT;
			m_InitTime = System.currentTimeMillis() + 5000;
		}
		
		public void Init()
		{
			m_Step = m_Step.Clone();
			m_State = State.RUN;
			m_bExecute = false;
			m_bExecuted = false;
			m_bSuccee = false;
			m_InitTime = 0;
		}
	}
	
	private class RunRobot implements Runnable
	{
		private int m_ID = 0;
		private RobotData m_Data;
		private boolean m_Run = true;
		public RunRobot(int id)
		{
			m_ID = id;
			m_Data = new RobotData(m_CloneStep);
		}
		
		public boolean IsRun()
		{
			return m_Run;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			m_Run = true;
			while ( m_Run && System.currentTimeMillis() < m_EndTime )
			{
				try
				{
					Robot r = Robot.GetRobots(m_ID);
					if ( m_Data.IsExecute() )
					{
						m_Data.Execute(r);
					}
					else if ( m_Data.IsFinish() )
					{
						boolean succ = m_Data.Result();
						if (succ)
						{
							m_RobotRes++;
							if ( m_RobotRes % 10 == 0 )
							{
								System.out.println("# 完成[" + m_RobotRes + "]次" + Debug.GetCurrTime());
							}
						}
						r.Close(0 , 0);
						m_Data.Reset();
					}
					else if ( m_Data.CanInit() )
					{
						Robot.CreateRobots(m_ID, m_LoginRule, m_CreateRoleRule);
						m_Data.Init();
					}
					
					Thread.sleep(10);
				}
				catch(Exception e)
				{
					m_Run = false;
				}
			}
		}
		
		public void Run()
		{
			m_Run = true;
			new Thread(this).start();
		}
		
	}
	
	public FreeRun(int maxruntime, int robotnum)
	{
		this(maxruntime, robotnum, null, null);
	}
	
	/**
	 * @param maxruntime 最大运行时间
	 * @param robotnum 机器人数量
	 */
	public FreeRun(int maxruntime, int robotnum , NameRule loginRule, NameRule createRoleRule)
	{
		m_MaxTime = maxruntime;
		m_RobotNum = robotnum;
		m_EndTime = System.currentTimeMillis() + m_MaxTime;
		m_Robots = new RunRobot[robotnum];
		m_LoginRule = loginRule;
		m_CreateRoleRule = createRoleRule;
	}
	
	/**
	 * 设置执行步骤
	 */
	public void SetStep(Step step)
	{
		m_CloneStep = step;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		Debug.Assert(m_CloneStep != null, "");
		
		System.out.println( "# " + Debug.GetCurrTime() );
		
		for ( int i = 0; i < m_RobotNum; ++i )
		{
			try
			{
				Robot.CreateRobots(i, m_LoginRule, m_CreateRoleRule);
			}
			catch (UnknownHostException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for ( int i = 0; i < m_Robots.length; ++i )
		{
			m_Robots[i] = new RunRobot(i);
			m_Robots[i].Run();
		}
		
		while ( CanRun() )
		{
			for ( int i = 0; i < m_Robots.length; ++i )
			{
				if ( !m_Robots[i].IsRun() )
				{
					m_Robots[i].Run();
				}
			}
			
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private boolean CanRun()
	{
		return System.currentTimeMillis() < m_EndTime;
	}
}
