/**
 * MethodParamUser.java 2012-6-13下午10:07:21
 */
package core.detail.impl.methodparam;

import utility.*;
import core.*;
import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodParamRefUser extends MethodParamUser
{
	public MethodParamRefUser()
	{
		super();
	}
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#Clone()
	 */
	@Override
	public MethodParam Clone()
	{
		return new MethodParamRefUser();
	}
	
	public void BindDefaultUser(User user)
	{
		m_User = user;
	}
	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer) throws Exception
	{
		Debug.Assert(false, "");
	}
	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamUser#BindUser()
	 */
	@Override
	public void BindUser()
	{
		Debug.Assert(false, "");
	}
}
