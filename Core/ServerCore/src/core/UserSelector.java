/**
 * UserSelector.java 2013-2-20上午10:22:49
 */
package core;

import java.util.*;

import core.detail.Mgr;
import core.detail.UserBase;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 用户选择器,用来从用户管理器
 */
public abstract class UserSelector
{
	private ArrayList<User> m_All = new ArrayList<User>();
	private Map<Long,User> m_All1 = new HashMap<Long, User>();
	private Map<String,User> m_All2 = new HashMap<String, User>();
	private boolean			m_Init = false;
	
	public UserSelector()
	{
		Mgr.GetUserMgr().RegUserSelector(this);
	}
	
	/**
	 * 是否把这个用户放入选择器
	 * 
	 * @return 返回true表示选中这个用户
	 */
	final public boolean CanSelector(User p_User)
	{
		boolean c = OnCanSelector(p_User);
		if (c)
		{
			m_All.add(p_User);
			m_All1.put(p_User.GetRoleGID(), p_User);
			m_All2.put(p_User.GetServerNick(), p_User);
		}
		return c;
	}
	
	/**
	 * 得到所有选中的用户
	 */
	final public ArrayList<User> GetSelectUsers()
	{
		_Init();
		return m_All;
	}
	
	/**
	 * 用roleid得到其中的一个用户
	 */
	final public User GetUserByRID(long rid)
	{
		_Init();
		return m_All1.get(rid);
	}
	
	/**
	 * 用昵称得到其中的一个用户
	 */
	final public User GetUserByNick(String nick, int p_serverid)
	{
		_Init();
		return m_All2.get(UserBase.ServerNick(nick, p_serverid));
	}

	final public void Reset()
	{
		m_All.clear();
		m_All1.clear();
		m_All2.clear();
		m_Init = false;
	}
	
	private void _Init()
	{
		if ( m_Init == false )
		{
			Mgr.GetUserMgr().GetUserBySelector(this);
			m_Init = true;
		}
	}
	
	protected abstract boolean OnCanSelector(User p_User);
}
