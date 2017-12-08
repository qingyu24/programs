/**
 * MethodParamRoleNetID.java 2012-6-27上午11:10:40
 */
package core.detail.impl.methodparam;

import core.detail.*;
import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodParamRoleNetID extends MethodParamUser
{
	private int m_NetID;
	
	public MethodParamRoleNetID()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#Clone()
	 */
	@Override
	public MethodParam Clone()
	{
		return new MethodParamRoleNetID();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer) throws Exception
	{
		m_NetID = buffer.GetInt();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#BindUser()
	 */
	@Override
	public void BindUser()
	{
		if ( m_User == null )
		{
			m_User = Mgr.GetUserMgr().GetUserByNetID(m_NetID);
		}
	}
	
}
