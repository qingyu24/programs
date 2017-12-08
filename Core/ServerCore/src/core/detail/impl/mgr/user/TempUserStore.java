/**
 * TempUserStore.java 2012-7-3下午4:02:35
 */
package core.detail.impl.mgr.user;

import java.util.*;
import java.util.Map.Entry;

import core.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 由昵称,角色id访问而产生的临时User,当有连接对象时,从这个列表移除,添加到正式列表
 */
public class TempUserStore
{
	private Map<String,User>	m_Nk	= new HashMap<String,User>();	///< nick,user
	private Map<Long,User>		m_GID	= new HashMap<Long,User>();		///< gid,user
	private Map<String,User>	m_UserName = new HashMap<String, User>();	///< username,user
	
	/**
	 * 添加一个User对象
	 */
	public void Add(User p_User)
	{
		if ( p_User.NickReady() )
		{
			m_Nk.put(p_User.GetNick(), p_User);
		}
		if ( p_User.GIDReady() )
		{
			m_GID.put(p_User.GetRoleGID(), p_User);
		}
		if ( p_User.UserNameReady() )
		{
			m_UserName.put(p_User.GetUserName(), p_User);
		}
	}

	public ArrayList<User> GetAllUser()
	{
		ArrayList<User>	all	= new ArrayList<User>();
		for ( Iterator<Entry<String, User>> it = m_Nk.entrySet().iterator(); it.hasNext(); )
		{
			Entry<String, User> entry = it.next();
			all.add(entry.getValue());
		}
		for ( Iterator<Entry<Long, User>> it = m_GID.entrySet().iterator(); it.hasNext(); )
		{
			Entry<Long, User> entry = it.next();
			all.add(entry.getValue());
		}
		for ( Iterator<Entry<String, User>> it = m_UserName.entrySet().iterator(); it.hasNext(); )
		{
			Entry<String, User> entry = it.next();
			all.add(entry.getValue());
		}
		return all;
	}
	
	public int GetNum()
	{
		return m_Nk.size() + m_GID.size() + m_UserName.size();
	}

	/**
	 * 根据角色id获取User对象
	 */
	public User GetUserByGid(long m_lGid)
	{
		if ( !m_GID.containsKey(m_GID) )
		{
			return null;
		}
		return m_GID.get(m_GID);
	}

	/**
	 * 根据昵称获取User对象
	 */
	public User GetUserByNick(String m_sRoleNick)
	{
		if ( !m_Nk.containsKey(m_sRoleNick) )
		{
			return null;
		}
		return m_Nk.get(m_sRoleNick);
	}

	/**
	 * 根据用户名获取User对象
	 */
	public User GetUserByUserName(String m_sUserName)
	{
		if ( !m_UserName.containsKey(m_sUserName) )
		{
			return null;
		}
		return m_UserName.get(m_sUserName);
	}

	public void Remove(User u)
	{
		String nk = u.GetNick();
		if ( nk != null )
		{
			m_Nk.remove(nk);
		}
		
		long gid = u.GetRoleGID();
		m_GID.remove(gid);
		
		String username = u.GetUserName();
		m_UserName.remove(username);
	}
}
