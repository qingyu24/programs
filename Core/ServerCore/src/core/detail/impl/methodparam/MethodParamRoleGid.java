/**
 * MethodParamRoleGid.java 2012-6-27上午11:07:13
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
public class MethodParamRoleGid extends MethodParamUser
{
	private long m_lGid;
	
	public MethodParamRoleGid()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#Clone()
	 */
	@Override
	public MethodParam Clone()
	{
		return new MethodParamRoleGid();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer) throws Exception
	{
		m_lGid = buffer.GetLong();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#BindUser()
	 */
	@Override
	public void BindUser()
	{
		if ( m_User == null )
		{
			m_User = Mgr.GetUserMgr().GetUserByGid(m_lGid, true);
		}
	}
	

}
