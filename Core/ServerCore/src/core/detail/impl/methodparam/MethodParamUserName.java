/**
 * MethodParamUserName.java 2013-4-9下午3:29:26
 */
package core.detail.impl.methodparam;

import core.detail.Mgr;
import core.detail.impl.socket.MsgBuffer;
import core.detail.interface_.MethodParam;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodParamUserName extends MethodParamUser
{
	private String m_sUserName;
	private String m_sPassword;
	private int	m_nServerID;
	
	public MethodParamUserName()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#Clone()
	 */
	@Override
	public MethodParam Clone()
	{
		return new MethodParamUserName();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer) throws Exception
	{
		m_sUserName = buffer.GetString();
		m_sPassword = buffer.GetString();
		m_nServerID = buffer.GetInt();
		
//		System.err.println("ReadMsg:" + m_sUserName + "," + m_sPassword + "," + m_nServerID);
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#BindUser()
	 */
	@Override
	public void BindUser()
	{
		if ( m_User == null )
		{
			m_User = Mgr.GetUserMgr().GetUserByUserName(m_sUserName, m_sPassword, (short)m_nServerID, true);
		}
	}

}
