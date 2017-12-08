/**
 * UserStore.java 2012-7-3下午2:23:48
 */
package core.detail.impl.mgr.user;

import java.util.*;

import utility.*;

import core.*;
import core.detail.Mgr;
import core.detail.UserBase;
import core.detail.impl.log.*;
import core.ex.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class UserStore
{
	private User[]					m_All;
	private Map<String,Integer>		m_Nk	= new HashMap<String,Integer>();///< nick,index
	private Map<Long,Integer>		m_GID	= new HashMap<Long,Integer>();	///< gid,index
	private UserIDMgr				m_IDMgr;
	private long					m_RefreshTime = System.currentTimeMillis();
	private ArrayList<UserSelector>	m_Selectors = new ArrayList<UserSelector>();
	
	public UserStore()
	{
		RootConfig r = RootConfig.GetInstance();
		m_All = new User[r.UserMax];
		m_IDMgr = new UserIDMgr(0,r.UserMax);
	}
	
	/**
	 * 添加一个User对象,这是正常流程创建的
	 */
	public void Add(User p_User)
	{
		p_User.Log(eSystemInfoLogType.SYSTEM_INFO_USERMGR_ADD, p_User.GetServerNick(), p_User.GetRoleGID(), p_User.GetNetID(), p_User);
		if ( m_Nk.containsKey(p_User.GetServerNick()) || m_GID.containsKey(p_User.GetRoleGID()) )
		{
			User u = _GetUser(p_User);
			Debug.Assert( u.GetState().GetTempUser() || u.IsDisConnected(), "" );
			p_User.Merge(u);
			
			_RemoveUser(u, true);
			u.GetState().SetDisable();
			
			p_User.Log(eSystemInfoLogType.SYSTEM_INFO_USERMGR_MERGE,
					p_User.GetServerNick(), p_User.GetRoleGID()	, p_User.GetNetID()	, p_User, 
					u.GetServerNick()		, u.GetRoleGID()		, u.GetNetID()		, u);
		}
		_Add(p_User);
		_ResetUserSelector();
	}

	/**
	 * 添加一个由其他人发起的函数而产生的User
	 */
	public void AddTempUser(User p_User)
	{
		Debug.Assert(p_User.IsDisConnected(), "");
		p_User.Log(eSystemInfoLogType.SYSTEM_INFO_USERMGR_ADD_TEMP, p_User.GetServerNick(), p_User.GetRoleGID(), p_User.GetNetID(), p_User);
		if ( m_Nk.containsKey(p_User.GetServerNick()) || m_GID.containsKey(p_User.GetRoleGID()) )
		{
			User u = _GetUser(p_User);
			u.Merge(p_User);
			
			p_User.GetState().SetDisable();
			
			p_User.Log(eSystemInfoLogType.SYSTEM_INFO_USERMGR_MERGE, 
					u.GetServerNick()		, u.GetRoleGID()		, u.GetNetID()		, u,
					p_User.GetServerNick(), p_User.GetRoleGID()	, p_User.GetNetID()	, p_User);
		}
		else
		{
			_Add(p_User);
		}
		_ResetUserSelector();
	}
	
	public ArrayList<User> GetAllUsers()
	{
		ArrayList<User> all = new ArrayList<User>();
		for ( User u : m_All )
		{
			if ( u == null )
			{
				continue;
			}
			all.add(u);
		}
		return all;
	}
	
	public int GetNum()
	{
		return m_GID.size();
	}
	
	/**
	 * 根据角色id获取User对象
	 * 
	 * @return 无法找到返回null
	 */
	public User GetUserByGid(long m_lGid)
	{
		if ( !m_GID.containsKey(m_lGid) )
		{
			return null;
		}
		int index = m_GID.get(m_lGid);
		User user = _GetUserByIndex(index);
		Debug.Assert(user != null, "");
		return user;
	}
	
	/**
	 * 更加网络id来获取User对象
	 * 
	 * @return 无法找到返回null
	 */
	public User GetUserByNetID(int m_NetID)
	{
		if ( m_NetID < 0 || m_NetID >= m_All.length )
		{
			return null;
		}
		return m_All[m_NetID];
	}
	
	/**
	 * 根据昵称获取User对象
	 * 
	 * @return 无法找到返回null
	 */
	public User GetUserByNick(String m_sRoleNick, int serverid)
	{
		String nick = UserBase.ServerNick(m_sRoleNick, serverid);
		if ( ! m_Nk.containsKey(nick) )
		{
			return null;
		}
		int index = m_Nk.get(nick);
		User user = _GetUserByIndex(index);
		Debug.Assert(user != null, "");
		return user;
	}

	public ArrayList<User> GetUserBySelector(UserSelector s)
	{
		ArrayList<User> all = new ArrayList<User>();
		for ( User u : m_All )
		{
			if ( u == null )
			{
				continue;
			}
			if ( u.GetState().GetTempUser() )
			{
				continue;
			}
			if ( s.CanSelector(u) == false )
			{
				continue;
			}
			all.add(u);
		}
		return all;
	}

	/**
	 * 根据用户名获取User对象
	 * 
	 * @return 无法找到返回null
	 */
	public User GetUserByUserName(String p_username)
	{
		for ( User u : m_All )
		{
			if ( u != null && u.GetUserName() != null && u.GetUserName().equals(p_username) )
			{
				return u;
			}
		}
		return null;
	}

	/**
	 * 是否需要清理User
	 */
	public boolean NeedClearUser()
	{
		return m_Nk.size() >= RootConfig.GetInstance().UserStartClear;
	}
	
	public void RegUserSelector(UserSelector s)
	{
		if ( m_Selectors.contains(s) )
		{
			return;
		}
		m_Selectors.add(s);
	}

	/**
	 * 释放对象
	 */
	public void Release(User u)
	{
		u.GetState().SetDisable();
		try
		{
			u.OnRelease();
		}
		catch (Exception e)
		{
			Log.out.LogException(e);
		}
		_RemoveUser(u, false);
		_ResetUserSelector();
	}
	
	private void _Add(User p_User)
	{
		int id = m_IDMgr.GetID();
		Debug.Assert(id >= 0 && id < m_All.length, "");
		Debug.Assert(!m_Nk.containsKey(p_User.GetServerNick()), "");
		Debug.Assert(!m_GID.containsKey(p_User.GetRoleGID()), "");
		m_Nk.put(p_User.GetServerNick(), id);
		m_GID.put(p_User.GetRoleGID(), id);
		m_All[id] = p_User;
		p_User.SetNetID(id);
		
		_SetUserNum();
		p_User.GetState().SetManaged(true);
		
		p_User.Log(eSystemInfoLogType.SYSTEM_INFO_USERMGR_ADD_FINISH, p_User.GetServerNick(), p_User.GetRoleGID(), p_User.GetNetID(), p_User);
	}
	
	private User _GetUser(User p_User)
	{
		User u = null;
		if ( m_Nk.containsKey(p_User.GetServerNick()) )
		{
			int index = m_Nk.get(p_User.GetServerNick());
			u = m_All[index];
		}
		else if ( m_GID.containsKey(p_User.GetRoleGID()) )
		{
			int index = m_GID.get(p_User.GetRoleGID());
			u = m_All[index];
		}
		else
		{
			Debug.Assert(false, "");
		}
		return u;
	}
	
	private User _GetUserByIndex(int index)
	{
		Debug.Assert( index >= 0 && index < m_All.length, "");
		Debug.Assert( m_Nk.size() == m_GID.size(), "" );
		return m_All[index];
	}
	
	private void _RemoveUser(User p_User, boolean replace)
	{
		int netid = p_User.GetNetID();
		Debug.Assert(netid >= 0 && netid < m_All.length, "");
		Debug.Assert( m_All[netid] == p_User, "" );
		m_All[netid] = null;
		m_IDMgr.ReleaseID(netid);
		m_Nk.remove(p_User.GetServerNick());
		m_GID.remove(p_User.GetRoleGID());
		p_User.Log(eSystemInfoLogType.SYSTEM_INFO_USERMGR_REMOVE, p_User.GetServerNick(), p_User.GetRoleGID(), p_User.GetNetID(), p_User);
		if ( !replace )
		{
			_SetUserNum();
		}
		Mgr.GetTimerMgr().RemoveBatchByMarkID(p_User.hashCode());
	}

	private void _SetUserNum()
	{
		if ( System.currentTimeMillis() != m_RefreshTime )
		{
			m_RefreshTime = System.currentTimeMillis();
			ShowMgr.GetInfo().setUserNum(GetNum());
		}
		Root.GetInstance().GetFactory().OnUserNumChange(m_Nk.size(), RootConfig.GetInstance().UserMax);
	}
	
	private void _ResetUserSelector()
	{
		for (UserSelector s : m_Selectors)
		{
			s.Reset();
		}
	}
}
