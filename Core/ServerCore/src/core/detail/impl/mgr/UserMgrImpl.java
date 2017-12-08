/**
 * UserMgrImpl.java 2012-6-27上午10:22:29
 */
package core.detail.impl.mgr;

import java.util.*;

import utility.*;

import core.*;
import core.detail.Mgr;
import core.detail.UserBase;
import core.detail.impl.*;
import core.detail.impl.log.*;
import core.detail.impl.mgr.user.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class UserMgrImpl implements UserMgr
{
	private UserStore				m_Users = new UserStore();
	private TempUserStore			m_TempUsers = new TempUserStore();
	private QueueByUse<User>		m_Order = new QueueByUse<User>();
	private Queue<User> 			m_Dis 	= new LinkedList<User>();
	private long					m_ProcessTempUserTime = 0;
	private long					m_ProcessLongUserTime = 0;
	private long					m_ProcessSaveDBTime = 0;
	private boolean					m_QuickSave = false;
	private boolean					m_ExecuteQuickSave = false;
	
	public UserMgrImpl()
	{
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#AttachUser(core.User)
	 */
	@Override
	public void AttachUser(User p_User)
	{
		Debug.Assert(p_User.IsKeyDataReady(), "");
		m_Users.Add(p_User);
		
		//TODO user可能被合并了,因此在缓存中的被合并的user必须得干掉,这个地方写的生硬,有时间改掉
		m_Order.Remove(p_User.GetRoleGID());
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#CreateUser(core.detail.interface_.Link)
	 */
	@Override
	public User CreateUser(Link p_Link)
	{
		User user = Root.GetInstance().NewUser();
		user.SetLink(p_Link);
		return user;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#Empty()
	 */
	@Override
	public boolean Empty()
	{
		return m_Users.GetNum() == 0 && m_TempUsers.GetNum() == 0 && m_Order.Size() == 0 && m_Dis.size() == 0;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#ExecuteSaveAllUserDataFinish()
	 */
	@Override
	public boolean ExecuteSaveAllUserDataFinish()
	{
		return m_QuickSave && m_ExecuteQuickSave;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#GetAllOnlineUser()
	 */
	@Override
	public ArrayList<User> GetAllOnlineUser()
	{
		return OnlineUserSelector.GetInstance().GetSelectUsers();
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#GetNum()
	 */
	@Override
	public int GetNum()
	{
		return m_Users.GetNum();
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#GetUserByGid(long)
	 */
	@Override
	public User GetUserByGid(long m_lGid, boolean create)
	{
		User u = m_Users.GetUserByGid(m_lGid);
		if ( u != null )
		{
			return u;
		}
		if ( !create )
		{
			return u;
		}
		u = m_TempUsers.GetUserByGid(m_lGid);
		if ( u != null )
		{
			if ( u.IsDisConnected() )
			{
				m_Order.Use(u);
			}
			return u;
		}
		u = Root.GetInstance().NewUser();
		u.SetRoleGID(m_lGid);
		u.GetState().SetTempUser(true);
		m_TempUsers.Add(u);
		return u;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#GetUserByNetID(int)
	 */
	@Override
	public User GetUserByNetID(int m_NetID)
	{
		return m_Users.GetUserByNetID(m_NetID);
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#GetUserByNick(java.lang.String)
	 */
	@Override
	public User GetUserByNick(String m_sRoleNick, short serverid, boolean create)
	{
		User u = m_Users.GetUserByNick(m_sRoleNick, serverid);
		if ( u != null )
		{
			return u;
		}
		if ( !create )
		{
			return u;
		}
		u = m_TempUsers.GetUserByNick(m_sRoleNick);
		if ( u != null )
		{
			if ( u.IsDisConnected() )
			{
				m_Order.Use(u);
			}
			return u;
		}
		u = Root.GetInstance().NewUser();
		u.SetNick(m_sRoleNick);
		u.SetServerID(serverid);
		u.GetState().SetTempUser(true);
		m_TempUsers.Add(u);
		return u;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#GetUserBySelector(core.UserSelector)
	 */
	@Override
	public ArrayList<User> GetUserBySelector(UserSelector s)
	{
		return m_Users.GetUserBySelector(s);
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#GetUserByUserName(java.lang.String)
	 */
	@Override
	public User GetUserByUserName(String p_username)
	{
		return m_Users.GetUserByUserName(p_username);
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#GetUserByUserName(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public User GetUserByUserName(String m_sUserName, String m_sPassword, short serverid, boolean create)
	{
		User u = m_Users.GetUserByUserName(m_sUserName);
		if ( u != null )
		{
			return u;
		}
		if ( !create )
		{
			return u;
		}
		u = m_TempUsers.GetUserByUserName(m_sUserName);
		if ( u != null )
		{
			if ( u.IsDisConnected() )
			{
				m_Order.Use(u);
			}
			return u;
		}
		u = Root.GetInstance().NewUser();
		u.SetUserName(m_sUserName);
		u.SetPassword(m_sPassword);
		u.SetServerID(serverid);
		u.GetState().SetTempUser(true);
		m_TempUsers.Add(u);
		return u;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#OnUserDisconnected(core.User)
	 */
	@Override
	public void OnUserDisconnected(User m_User)
	{
		long stm = System.currentTimeMillis();
		synchronized(m_Dis)
		{
			m_Dis.add(m_User);
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "OnUserDisconnected用时[[[:" + (System.currentTimeMillis() - stm));
		}
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#RegUserSelector(core.UserSelector)
	 */
	@Override
	public void RegUserSelector(UserSelector s)
	{
		m_Users.RegUserSelector(s);
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#SaveAllUserData()
	 */
	@Override
	public void SaveAllUserData()
	{
		m_QuickSave = true;
	}
	
	public String toString()
	{
		return super.toString() + " UserStore[" + m_Users.GetNum() + "] TempUserStore[" + m_TempUsers.GetNum() + "] QueueByUseUser[" + m_Order.Size() + "] DisUser[" + m_Dis.size() + "]";
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.UserMgr#Update()
	 */
	@Override
	public void Update()
	{
		_ProcessTempUser();
		
		_ProcessDisQueue();
		
		_ProcessUserFull();
		
		_ProcessLongUser();
		
		_ProcessSaveDBTask();
		
		_ProcessQuickSave();
	}

	private void _ExecuteUserDisconnectCallBack(User u)
	{
		try
		{
			u.Log(eSystemInfoLogType.SYSTEM_INFO_USERMGR_DISCONNECT, u.GetNick(), u.GetRoleGID(), u.GetNetID(), u, u.GetCloseReason(), u.GetCloseReasonEx());
			_CloseLog.Log(u);
			u.GetState().SetDisconnect(true);
			u.OnDisconnect();
		}
		catch (Exception e)
		{
			Log.out.LogException(e);
		}
		finally
		{
			Mgr.GetTimerMgr().RemoveBatchByMarkID(u.hashCode());
		}
	}

	private Queue<User> _GetDisQueue()
	{
		Queue<User> copyDis = new LinkedList<User>();
		long stm = System.currentTimeMillis();
		synchronized(m_Dis)
		{
			while(!m_Dis.isEmpty())
			{
				copyDis.add(m_Dis.poll());
			}
		}
		if ( System.currentTimeMillis() - stm > 50 )
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "UserMgrImpl::_GetDisQueue用时[[[:" + (System.currentTimeMillis() - stm));
		}
		return copyDis;
	}

	/**
	 * 把断开用户在逻辑线程里操作
	 */
	private void _ProcessDisQueue()
	{
		Queue<User> copyDis = _GetDisQueue();
		while(!copyDis.isEmpty())
		{
			User u = copyDis.poll();
			_ExecuteUserDisconnectCallBack(u);
			if ( u.GetState().GetManaged() )
			{
				m_Order.Add(u);
			}
			else
			{
				u.GetState().SetDisable();
			}
		}
	}

	
	/**
	 * 平常检查没用在使用的用户数据,如果到了清理时间,也清理
	 */
	private void _ProcessLongUser()
	{
		if ( Mgr.GetSqlMgr().BusyRatio() > RootConfig.GetInstance().ExecuteSaveDBSQLBusyRatio )
		{
			return;
		}
		
		if ( System.currentTimeMillis() < m_ProcessLongUserTime )
		{
			return;
		}
		
		m_ProcessLongUserTime = System.currentTimeMillis() + RootConfig.GetInstance().ProcessTempUserTm;
		Pair<User, Long> p = m_Order.PeekData();
		if ( p == null )
		{
			return;
		}
		
		if ( System.currentTimeMillis() - p.second > RootConfig.GetInstance().UserClearTime * 1000 )
		{
			User u = m_Order.Poll();
			if ( !u.GetState().GetDisable() )
			{
				u.Log(eSystemInfoLogType.SYSTEM_INFO_USERMGR_RELEASE_NORMAL, u.GetNick(), u.GetRoleGID(), u.GetNetID(), u);
				m_Users.Release(u);
			}
		}
	}

	private void _ProcessQuickSave()
	{
		if ( !m_QuickSave )
		{
			return;
		}
		
		if ( m_Dis.size() > 0 )
		{
			//有断开的用户,那就等待
			return;
		}
		
		if ( m_Order.Size() > 0 )
		{
			User u = null;
			while ((u = m_Order.Poll()) != null )
			{
				m_Users.Release(u);
			}
			return;
		}
		
		for (User u : m_Users.GetAllUsers())
		{
			m_Users.Release(u);
		}
		m_ExecuteQuickSave = true;
	}

	/**
	 * 平时对没有存过db的用户对象进行存储
	 */
	private void _ProcessSaveDBTask()
	{
		if ( !RootConfig.GetInstance().CanStartSaveDBTask() )
		{
			return;
		}
		
		if ( Mgr.GetSqlMgr().BusyRatio() > RootConfig.GetInstance().ExecuteSaveDBSQLBusyRatio )
		{
			return;
		}
		
		if ( System.currentTimeMillis() < m_ProcessSaveDBTime )
		{
			return;
		}
		
		m_ProcessSaveDBTime = System.currentTimeMillis() + RootConfig.GetInstance().ExecuteSaveDBSpaceTm * 1000;
		
		ArrayList<User> us =  m_Order.GetSaveData(RootConfig.GetInstance().ExecuteSaveDBUserNum);
		for ( User u : us )
		{
			UserBase ub = (UserBase) u;
			try
			{
				ub.AddSaveTask(false);
			}
			catch (Exception e)
			{
				u.LogException(e);
			}
		}
	}

	/**
	 * 处理临时用户,如果成功加载数据,则放入正式队列
	 */
	private void _ProcessTempUser()
	{
		if ( System.currentTimeMillis() < m_ProcessTempUserTime )
		{
			return;
		}
		m_ProcessTempUserTime = System.currentTimeMillis() + RootConfig.GetInstance().ProcessTempUserTm;
		ArrayList<User> all = m_TempUsers.GetAllUser();
		for ( User u : all )
		{
			if ( u.GetState().IsLock() )
			{
				continue;
			}
			if ( !u.GetState().GetKeyDataLoad() )
			{
				continue;
			}
			m_TempUsers.Remove(u);
			if ( u.KeyDataReady() )
			{
				m_Users.AddTempUser(u);
			}
			else
			{
				Log.out.Log(eSystemErrorLogType.SYSTEM_ERROR_USERMGR_REMOVE_TEMPUSER, u);
			}
		}
	}

	/**
	 * 当用户数量过多,尝试着释放一批没用的数据
	 */
	private void _ProcessUserFull()
	{
		if ( !m_Users.NeedClearUser() )
		{
			return;
		}
		
		int num = RootConfig.GetInstance().UserClearNum;
		while ( num > 0 )
		{
			num--;
			User u = m_Order.Poll();
			if ( u == null )
			{
				break;
			}
			u.Log(eSystemInfoLogType.SYSTEM_INFO_USERMGR_RELEASE_FULL, u.GetNick(), u.GetRoleGID(), u.GetNetID(), u);
			m_Users.Release(u);
		}
	}
}
