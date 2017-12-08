/**
 * MethodExImpl.java 2012-6-12下午10:17:39
 */
package core.detail.impl;

import java.lang.reflect.*;

import utility.*;

import core.*;
import core.detail.eUserLockType;
import core.detail.impl.methodparam.*;
import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodExImpl implements MethodEx
{
	private Object			m_Call;
	private Method			m_Method;
	private MethodParam[] 	m_Params;
	private User			m_User;
	private int				m_ClassID;
	private int				m_MethodID;
	private boolean			m_RunWithoutDataReady = false;
	private boolean			m_NeedBindUser = false;
	private StringBuilder	m_ToString = new StringBuilder();
	
	/**
	 * @param call		调用的对象,在这里是全局的唯一对象,如Login.GetInstance()
	 * @param m			调用的函数
	 * @param p_params	调用的参数列表包装,用来从网络解析数据
	 * @param classid	类id
	 * @param methodid	函数id
	 * @param p_runwithoutdataready	是否能不检查用户的数据而直接运行
	 */
	public MethodExImpl(Object call, Method m, MethodParam[] p_params, int classid, int methodid, boolean p_runwithoutdataready)
	{
		m_Call = call;
		m_Method = m;
		m_Params = p_params;
		m_ClassID = classid;
		m_MethodID = methodid;
		m_RunWithoutDataReady = p_runwithoutdataready;
		
		for ( int i = 1; i < m_Params.length; ++i )
		{
			if ( m_Params[i] instanceof MethodParamUser )
			{
				m_NeedBindUser = true;
			}
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#CanExecute()
	 */
	@Override
	public boolean CanExecute()
	{
		if ( m_User.IsDisabled() && !m_User.GetState().IsLock() )
		{
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#Clone()
	 */
	@Override
	public MethodEx Clone()
	{
		MethodParam[] ps = new MethodParam[m_Params.length];
		for ( int i = 0; i < ps.length; ++i )
		{
			ps[i] = m_Params[i].Clone();
		}
		MethodExImpl ex =  new MethodExImpl(m_Call, m_Method, ps, m_ClassID, m_MethodID, m_RunWithoutDataReady);
		ex.m_NeedBindUser = m_NeedBindUser;
		return ex;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#Close()
	 */
	@Override
	public void Close(int reason)
	{
		for ( MethodParam mp : m_Params )
		{
			mp.SetLock(eUserLockType.LOCK_DATA, false);
		}
		
		if ( m_User != null )
		{
			m_User.Close(reason, 0);
		}
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#Finish()
	 */
	@Override
	public void Finish()
	{
		//TODO
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#GetClassID()
	 */
	@Override
	public int GetClassID()
	{
		return m_ClassID;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#GetMethodID()
	 */
	@Override
	public int GetMethodID()
	{
		return m_MethodID;
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
	 * @see core.detail.interface_.MethodEx#IsAllDataReady()
	 */
	@Override
	public boolean IsAllDataReady()
	{
		for ( MethodParam mp : m_Params )
		{
			if ( !mp.DataReady() )
			{
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer, User user) throws Exception
	{
		Debug.Assert(m_Params.length >= 1, "");
		Debug.Assert(m_Params[0] instanceof MethodParamRefUser, "");
		MethodParamRefUser m = (MethodParamRefUser) m_Params[0];
		m.BindDefaultUser(user);
		m_User = user;
		for ( int i = 1; i < m_Params.length; ++i )
		{
			m_Params[i].ParseMsg(buffer);
		}
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#Run()
	 */
	@Override
	public RunResult Run() throws Exception
	{
		_BindParamUser();
		
		if ( _IsParamLock() )
		{
			return RunResult.BLOCK;
		}
		
		if ( !_IsParamDataReady() )
		{
			return RunResult.DATA;
		}
		
		if ( !_Check() )
		{
			return RunResult.RUNDIRECT;
		}
		
		RunDirect();
		return RunResult.RUNDIRECT;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#RunDirect()
	 */
	@Override
	public void RunDirect() throws Exception
	{
		for ( MethodParam mp : m_Params )
		{
			mp.SetLock(eUserLockType.LOCK_DATA, false);
		} 
		
		//对于已经设定无用的用户,不再执行逻辑操作
		if ( m_User.GetState().GetDisable() )
		{
			return;
		}
		
		if ( m_User.GetState().GetDisconnect() )
		{
			return;
		}
		
		_Execute();
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#SqlRun()
	 */
	@Override
	public void SqlRun() throws Exception
	{
		for ( int i = 0; i < m_Params.length; ++i )
		{
			m_Params[i].SQLRun();
		}
	}

	public String toString()
	{
		{
			m_ToString.setLength(0);
			m_ToString.append("RFC[")
					  .append(m_ClassID).append(",").append(m_MethodID)
					  .append(",").append(Str.GetLastStr(m_Method.getDeclaringClass().getName(), '.'))
					  .append(".").append(m_Method.getName())
					  .append("]");
			m_ToString.append(" param(");
			for (MethodParam p : m_Params)
			{
				m_ToString.append(p.Get()).append(",");
			}
			m_ToString.append(")");
			
		}
		return m_ToString.toString();
	}

	/**
	 * 如果参数是解析为User对象,那么执行
	 */
	private void _BindParamUser()
	{
		if ( m_NeedBindUser == false )
		{
			return;
		}
		for ( int i = 1; i < m_Params.length; ++i )
		{
			if ( m_Params[i] instanceof MethodParamUser )
			{
				MethodParamUser m = (MethodParamUser) m_Params[i];
				m.BindUser();
			}
		}
	}

	private void _Execute() throws Exception
	{
		try
		{
//			System.out.println("+++++++++++正在执行:" + this);
			m_User.StartMethodMark(this);
			
			m_User.OnRunDirect();
			
			Object[] params = new Object[m_Params.length];
			for ( int i = 0 ; i < params.length; ++i )
			{
				params[i] = m_Params[i].Get();
			}
			
			m_Method.invoke(m_Call, params);
		}
		finally
		{
			m_User.EndMethodMark(this);
			Finish();
		}
	}

	/**
	 * 检查参数所需要的数据是否加载好了
	 */
	private boolean _IsParamDataReady()
	{
		if ( !m_RunWithoutDataReady )
		{
			for ( MethodParam mp : m_Params )
			{
				if ( !mp.DataReady() )
				{
					mp.SetLock(eUserLockType.LOCK_DATA, true);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 检查参数是否被阻塞(只对带数据类型的参数有效,如MethodParamRefUser等,对普通基础类型无效,如MethodParamInt)
	 * 
	 * @tip 阻塞分成对象正在进行sql操作,或者其他网络操作,又或者逻辑层主动阻塞(未开放)
	 */
	private boolean _IsParamLock()
	{
		for ( MethodParam mp : m_Params )
		{
			if ( mp.IsLock() )
			{
				return true;
			}
		}
		return false;
	}

	protected boolean _Check()
	{
		return true;
	}
}
