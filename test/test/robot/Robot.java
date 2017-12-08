/**
 * Robot.java 2012-7-11下午4:06:10
 */
package test.robot;

import java.io.*;
import java.net.*;
import java.util.*;

import core.detail.UserBase;
import core.detail.impl.socket.*;
import test.robot.module.chat.ChatData;
import test.robot.module.login.*;
import test.robot.module.task.*;
import test.robot.utility.NameRule;


/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Robot extends UserBase implements Runnable 
{
	@SuppressWarnings("unused")
	private static RobotConfig m_Config = new RobotConfig();
	@SuppressWarnings("unused")
	private static RobotRoot m_Root = new RobotRoot();
	
	public LoginData m_LoginData;
	public TaskData m_TaskData;
	public ChatData m_ChatData;
	
	private RobotLink m_Link;
	private SendMsgBuffer m_Buffer = new SendMsgBuffer(1024);
	private int m_ID;
	
	private static Map<Integer,Robot> m_All = new HashMap<Integer,Robot>();
	
	private ArrayList<ResData> m_AllData = new ArrayList<ResData>();
	
	private NameRule m_LoginRule = NameRule.normalRule;
	private NameRule m_CreateRoleRule = NameRule.normalRule;
	
	private boolean m_Run = true;
	
	private Robot(int id)
	{
		try
		{
			m_ID = id;
			m_Link = new RobotLink(this);
			m_LoginData = new LoginData(this);
			_Add(m_LoginData);
			
			m_TaskData = new TaskData(this);
			_Add(m_TaskData);
			
			m_ChatData = new ChatData(this);
			_Add(m_ChatData);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String GetLoginName()
	{
		return m_LoginRule.GetName(this);
	}
	
	public String GetCreateRoleName()
	{
		return m_CreateRoleRule.GetName(this);
	}
	
	public void SetLoginNameRule(NameRule r)
	{
		if ( r != null )
		{
			m_LoginRule = r;
		}
	}
	
	public void SetCreateRoleNameRule(NameRule r)
	{
		if ( r != null )
		{
			m_CreateRoleRule = r;
		}
	}

	public SendMsgBuffer GetSendBuffer()
	{
		return m_Buffer;
	}

	public int GetID()
	{
		return m_ID;
	}
	
	public static Robot GetARobot() throws UnknownHostException, IOException
	{
		Robot r = new Robot(-1);
		r.GetLink().LinkToServer();
		new Thread(r).start();
		return r;
	}
	
	public static Robot CreateRobots(int id) throws UnknownHostException, IOException
	{
		return CreateRobots(id, null, null);
	}
	
	/**
	 * 创建一个机器人
	 *
	 * @param id 				机器人id，重复的话会重设机器人
	 * @param loginRule			 机器人登陆时的名字规则，可为null，会使用默认的名字规则NameRule.normalRule
	 * @param createRoleRule	机器人创建角色时的名字规则，可为null，会使用默认的名字规则NameRule.normalRule
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static Robot CreateRobots(int id, NameRule loginRule, NameRule createRoleRule) throws UnknownHostException, IOException
	{
		if ( m_All.containsKey(id) )
		{
			Robot r = m_All.get(id);
			r.SetLoginNameRule(loginRule);
			r.SetCreateRoleNameRule(createRoleRule);
			assert(r != null);
			assert(r.GetLink().IsDisConnected());
			r.GetLink().LinkToServer();
			r.Reset();
			return r;
		}
		Robot r = new Robot(id);
		r.SetLoginNameRule(loginRule);
		r.SetCreateRoleNameRule(createRoleRule);
		r.GetLink().LinkToServer();
		m_All.put(id, r);
		new Thread(r).start();
		return r;
	}
	
	private void Reset()
	{
		for ( ResData r : m_AllData )
		{
			r.Reset();
		}
	}

	public static Robot GetRobots(int id)
	{
		if ( m_All.containsKey(id) )
		{
			return m_All.get(id);
		}
		assert(false);
		return null;
	}

	public RobotLink GetLink()
	{
		return m_Link;
	}
	
	@Override
	public void Close(int reason, int ex)
	{
		if ( m_Link != null )
		{
			m_Link.Close(reason, ex);
		}
		super.Close(reason, ex);
	}
	
	public void Finish()
	{
		m_Run = false;
	}
	
	public boolean IsNeedWait()
	{
		return m_ID == -1;
	}
	
	public boolean IsNeedNotify()
	{
		return IsNeedWait();
	}
	
	public void Wait() throws InterruptedException
	{
		if ( IsNeedWait() )
		{
			synchronized (this)
			{
				this.wait(RobotConfig.MsgWaitTime);
			}
		}
	}
	
	public void Notify()
	{
		if ( IsNeedNotify() )
		{
			synchronized (this)
			{
				this.notify();
			}
		}
	}
	
	private void _Add(ResData r)
	{
		m_AllData.add(r);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		while ( m_Run )
		{
			if ( !m_Link.IsDisConnected() )
			{
				m_Link.OnSend();
			
				m_Link.OnRead();
			}
			
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see core.UserBase#OnDisconnect()
	 */
	@Override
	public void OnDisconnect()
	{
		
	}

	/* (non-Javadoc)
	 * @see core.UserBase#ExecuteKeyDataSQLRun()
	 */
	@Override
	public void ExecuteKeyDataSQLRun()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see core.detail.UserBase#OnAllDataLoadFinish()
	 */
	@Override
	public void OnAllDataLoadFinish() throws Exception
	{
		// TODO Auto-generated method stub
		
	}

}
