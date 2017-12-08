/**
 * OnlineUserSelector.java 2013-2-20下午5:26:35
 */
package core.detail.impl;

import core.User;
import core.UserSelector;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class OnlineUserSelector extends UserSelector
{
	private static OnlineUserSelector m_Instance = new OnlineUserSelector();
	
	public static OnlineUserSelector GetInstance()
	{
		return m_Instance;
	}
	
	/* (non-Javadoc)
	 * @see core.UserSelector#OnCanSelector(core.User)
	 */
	@Override
	protected boolean OnCanSelector(User p_User)
	{
		return !p_User.GetState().GetDisconnect();
	}
}