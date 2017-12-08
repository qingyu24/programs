/**
 * MyUserSelector.java 2013-2-20上午10:51:04
 */
package logic;

import core.User;
import core.UserSelector;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * @example
 * 选择所有的在线用户包含昵称"abcd"的用户
 * public static class SpecialUserNickUserSelctor extends MyUserSelector
	{
		public static SpecialUserNickUserSelctor Instance = new SpecialUserNickUserSelctor();
		
		
		@Override
		public boolean OnSelectUser(MyUser p_User)
		{
			return p_User.GetNick().contains("abcd");
		}
		
		public static void main(String[] args)
		{
			ArrayList<User> us = SpecialUserNickUserSelctor.Instance.GetSelectUsers();
			for (User u : us)
			{
			}
		}
	}
 */
public abstract class MyUserSelector extends UserSelector
{
	public abstract boolean OnSelectUser(MyUser p_User);

	/* (non-Javadoc)
	 * @see core.UserSelector#OnCanSelector(core.User)
	 */
	@Override
	protected boolean OnCanSelector(User p_User)
	{
		return OnSelectUser((MyUser)p_User);
	}
}
