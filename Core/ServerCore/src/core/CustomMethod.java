/**
 * SaveMethod.java 2012-7-6下午1:45:08
 */
package core;

import utility.*;
import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public abstract class CustomMethod implements MethodEx
{
	protected SQLRun	m_SqlRun;
	protected User		m_User;
	
	public CustomMethod(User p_User, SQLRun p_SqlRun)
	{
		m_SqlRun = p_SqlRun;
		m_User = p_User;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#Clone()
	 */
	@Override
	public MethodEx Clone()
	{
		Debug.Assert(false, "必定不会执行");
		return null;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#ParseMsg(core.detail.impl.socket.MsgBuffer, core.User)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer, User m_User) throws Exception
	{
		Debug.Assert(false, "必定不会执行");
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#Run()
	 */
	@Override
	public RunResult Run() throws Exception
	{
		Debug.Assert(false, "必定不会执行");
		return null;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#SqlRun()
	 */
	@Override
	public void SqlRun() throws Exception
	{
		if ( m_SqlRun != null )
		{
			m_SqlRun.Execute(m_User);
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#Close()
	 */
	@Override
	public void Close(int reason)
	{
		if ( m_User != null )
		{
			m_User.Close(reason, 0);
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#IsAllDataReady()
	 */
	@Override
	public boolean IsAllDataReady()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#GetUser()
	 */
	@Override
	public User GetUser()
	{
		return m_User;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#Finish()
	 */
	@Override
	public void Finish()
	{
		// TODO Log
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#RunDirect()
	 */
	@Override
	public abstract void RunDirect() throws Exception;

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#GetClassID()
	 */
	@Override
	public int GetClassID()
	{
		return 0;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#GetMethodID()
	 */
	@Override
	public int GetMethodID()
	{
		return 0;
	}
}
