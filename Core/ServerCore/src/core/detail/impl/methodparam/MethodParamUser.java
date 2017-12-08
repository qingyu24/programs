/**
 * MethodParamUser.java 2012-6-13下午10:07:21
 */
package core.detail.impl.methodparam;

import java.util.ArrayList;

import core.*;
import core.detail.*;
import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public abstract class MethodParamUser implements MethodParam
{
	protected 	User m_User;
	
	protected MethodParamUser()
	{
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#Clone()
	 */
	@Override
	public abstract MethodParam Clone();

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public abstract void ParseMsg(MsgBuffer buffer) throws Exception;

	public abstract void BindUser();

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#DataReady()
	 */
	@Override
	public boolean DataReady()
	{
		if ( m_User == null )
		{
			return true;
		}
		if ( !m_User.KeyDataReady() )
		{
			return false;
		}
		return m_User.DataReady();
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#IsLock()
	 */
	@Override
	public boolean IsLock()
	{
		if ( m_User == null )
		{
			return false;
		}
		return m_User.GetState().IsLock();
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#Get()
	 */
	@Override
	public Object Get()
	{
		return m_User;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#SQLRun()
	 */
	@Override
	public void SQLRun() throws Exception
	{
		if ( m_User == null )
		{
			return;
		}
		
		if ( !m_User.KeyDataReady() )
		{
			m_User.ExecuteKeyDataSQLRun();
			m_User.GetState().SetKeyDataLoad(true);
		}
		if ( !m_User.KeyDataReady() )
		{
			return;
		}
		ArrayList<SQLRun> rs = m_User.GetSQLRun();
		for ( SQLRun r : rs )
		{
			r.Execute(m_User);
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#SetLock(core.detail.eUserLockType, boolean)
	 */
	@Override
	public void SetLock(eUserLockType type, boolean b)
	{
		if ( m_User != null )
		{
			m_User.GetState().SetLock(type, b);	
		}
	}
}
