/**
 * MethodParamUserNick.java 2012-6-27上午11:02:47
 */
package core.detail.impl.methodparam;

import utility.Debug;
import core.detail.*;
import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodParamRoleNick extends MethodParamUser
{
	private String m_sRoleNick;
	private short m_ServerID;
	
	public MethodParamRoleNick()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#Clone()
	 */
	@Override
	public MethodParam Clone()
	{
		return new MethodParamRoleNick();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer) throws Exception
	{
		m_sRoleNick = buffer.GetString();
		m_ServerID = buffer.GetShort();
		Debug.Assert(m_ServerID > 0, "");
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#BindUser()
	 */
	@Override
	public void BindUser()
	{
		if ( m_User == null )
		{
			m_User = Mgr.GetUserMgr().GetUserByNick(m_sRoleNick, m_ServerID, true);
		}
	}

}
