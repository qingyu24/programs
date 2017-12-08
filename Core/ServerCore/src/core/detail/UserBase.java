/**
 * UserBase.java 2012-6-11下午9:38:18
 */
package core.detail;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import utility.*;

import core.*;
import core.detail.impl.*;
import core.detail.impl.log.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public abstract class UserBase implements User
{
	private UserState 	m_UserState = new UserState();
	private Link		m_Link;
	private String		m_sNick;
	private long		m_lGID;
	private int			m_nNetID;
	private String		m_sUserName;
	private String		m_sPassword;
	private short		m_nServerID;
	private Log			m_Log;
	private short		m_SaveDBNum = 0;
	private boolean		m_AllDataLoadFinish = false;
	private int			m_CloseReason = 0;
	private int			m_CloseEx = 0;
	private boolean		m_SaveFlag = false;
	
	private ArrayList<UserData> m_AllUserData	= new ArrayList<UserData>();
	
	protected UserBase()
	{
		m_Log = new Log(String.format("[UserLog%d]", hashCode()));
	}
	
	protected UserBase(String logname)
	{
		m_Log = new Log(logname);
	}
	
	/* (non-Javadoc)
	 * @see core.User#GetState()
	 */
	@Override
	public UserState GetState()
	{
		return m_UserState;
	}
	
	/* (non-Javadoc)
	 * @see core.User#Close()
	 */
	@Override
	public void Close(int reason, int ex)
	{
		if ( m_Link != null )
		{
			long stm = System.currentTimeMillis();
			m_Link.Close(reason, ex);
			if ( System.currentTimeMillis() - stm > 50 )
			{
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "UserBase::Close用时[[[:" + (System.currentTimeMillis() - stm));
			}
		}
	}

	/* (non-Javadoc)
	 * @see core.User#KeyDataReady()
	 */
	@Override
	public boolean KeyDataReady()
	{
		return NickReady() && GIDReady();
	}
	
	/* (non-Javadoc)
	 * @see core.User#NickReady()
	 */
	@Override
	public boolean NickReady()
	{
		return m_sNick != null && m_sNick.isEmpty() == false;
	}

	/* (non-Javadoc)
	 * @see core.User#GidReady()
	 */
	@Override
	public boolean GIDReady()
	{
		return m_lGID != 0;
	}
	
	@Override
	public void SetLink(Link p_link)
	{
		m_Link = p_link;
	}

	/* (non-Javadoc)
	 * @see core.User#SetNick(java.lang.String)
	 */
	@Override
	public void SetNick(String p_sNick)
	{
		m_sNick = p_sNick;
	}

	/* (non-Javadoc)
	 * @see core.User#SetRoleGID(long)
	 */
	@Override
	public void SetRoleGID(long p_lGID)
	{
		m_lGID = p_lGID;
	}

	/* (non-Javadoc)
	 * @see core.User#SetNetID(int)
	 */
	@Override
	public void SetNetID(int p_nNetID)
	{
		m_nNetID = p_nNetID;
	}
	
	/* (non-Javadoc)
	 * @see core.User#SetUserName(java.lang.String)
	 */
	@Override
	public void SetUserName(String p_sUserName)
	{
		m_sUserName = p_sUserName;
	}

	/* (non-Javadoc)
	 * @see core.User#GetNick()
	 */
	@Override
	public String GetNick()
	{
		return m_sNick;
	}

	/* (non-Javadoc)
	 * @see core.User#GetRoleGID()
	 */
	@Override
	public long GetRoleGID()
	{
		return m_lGID;
	}

	/* (non-Javadoc)
	 * @see core.User#GetNetID()
	 */
	@Override
	public int GetNetID()
	{
		return m_nNetID;
	}

	/* (non-Javadoc)
	 * @see core.User#GetLink()
	 */
	@Override
	public Link GetLink()
	{
		return m_Link;
	}

	/* (non-Javadoc)
	 * @see core.User#IsKeyDataReady()
	 */
	@Override
	public boolean IsKeyDataReady()
	{
		return IsNickReady() && IsGIDReady();
	}

	/* (non-Javadoc)
	 * @see core.User#IsNickReady()
	 */
	@Override
	public boolean IsNickReady()
	{
		return m_sNick != null && !m_sNick.isEmpty();
	}

	/* (non-Javadoc)
	 * @see core.User#IsGIDReady()
	 */
	@Override
	public boolean IsGIDReady()
	{
		return m_lGID != 0;
	}
	
	/* (non-Javadoc)
	 * @see core.User#UserNameReady()
	 */
	@Override
	public boolean UserNameReady()
	{
		return m_sUserName != null && !m_sUserName.isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see core.User#GetUserName()
	 */
	@Override
	public String GetUserName()
	{
		return m_sUserName;
	}
	
	/* (non-Javadoc)
	 * @see core.User#IsDisConnected()
	 */
	@Override
	public boolean IsDisConnected()
	{
		return m_Link == null || m_Link.IsDisConnected();
	}
	
	/**
	 * 注册一个{@link UserData}
	 *
	 * @param p_ID			注册的id
	 * @param p_UserData	注册的用户数据
	 */
	protected void AddToUserData(UserData p_UserData)
	{
		for ( UserData ud : m_AllUserData )
		{
			if ( p_UserData == ud )
			{
				Debug.Assert(false, "重复注册userdata:" + p_UserData);
			}
		}
		m_AllUserData.add(p_UserData);
	}
	
	/* (non-Javadoc)
	 * @see core.User#DataReady()
	 */
	@Override
	public boolean DataReady()
	{
		//有任何异常都认为数据没有准备好
		for ( UserData ud : m_AllUserData )
		{
			boolean c = false;
			try
			{
				c = ud.DataReady();
				if ( !c )
				{
					return false;
				}
			}
			catch (Exception e)
			{
				LogException(e);
				return false;
			}
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see core.User#GetSQLRun(int)
	 */
	@Override
	public ArrayList<SQLRun> GetSQLRun()
	{
		ArrayList<SQLRun> s = new ArrayList<SQLRun>();
		for ( UserData ud : m_AllUserData )
		{
			boolean c = false;
			try
			{
				c = ud.DataReady();
			}
			catch (Exception e)
			{
				LogException(e);
			}
			if (c)
			{
				continue;
			}
			
			SQLRun r = null;
			try
			{
				r = ud.GetSQLRun();
			}
			catch (Exception e)
			{
				LogException(e);
			}
			if ( r != null )
			{
				s.add(r);
			}
		}
		return s;
	}

	/* (non-Javadoc)
	 * @see core.User#ExecuteKeyDataSQLRun()
	 */
	@Override
	public abstract void ExecuteKeyDataSQLRun() throws Exception;

	/* (non-Javadoc)
	 * @see core.User#OnRelease()
	 */
	@Override
	public void OnRelease() throws Exception
	{
		AddSaveTask(true);
	}

	/* (non-Javadoc)
	 * @see core.User#Merge(core.User)
	 */
	@Override
	public void Merge(User u)
	{
		UserBase ub = (UserBase) u;
		m_AllUserData = ub.m_AllUserData;
		
		SystemFn.SetAllFieldValue(this, u);
	}
	
	/* (non-Javadoc)
	 * @see core.User#SaveAllToDB()
	 */
	@Override
	public void SaveAllToDB()
	{
		for ( UserData ud : m_AllUserData )
		{
			try
			{
				if ( ud.DataReady() )
				{
					ud.SaveToDB();
				}
				DBMgr.Commit();
			}
			catch (Exception e)
			{
				LogException(e, "SaveFlag:" + m_SaveFlag);
			}
		}
	}
	
	public void AddSaveTask(boolean force) throws Exception
	{
		m_SaveFlag = force;
		if ( force )
		{
			if ( GetState().CanSaveData() )
			{
				Mgr.GetSqlMgr().AddTask(new SaveMethod(this));
			}
		}
		else
		{
			if ( Mgr.GetSqlMgr().IsBusy() )
			{
				System.out.println("sql繁忙,暂时不存储");
				return;
			}
			
			if ( GetState().CanSaveData() )
			{
				m_SaveDBNum++;
				Mgr.GetSqlMgr().AddTask(new SaveMethod(this));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see core.User#OnDisconnect()
	 */
	@Override
	public void OnDisconnect() throws Exception
	{
		m_AllDataLoadFinish = false;
	}
	
	public abstract void OnAllDataLoadFinish() throws Exception;

	/* (non-Javadoc)
	 * @see core.User#Log(core.LogType, java.lang.Object[])
	 */
	@Override
	public void Log(LogType type, Object... params)
	{
		m_Log.SetLog(this, type, params);
	}

	/* (non-Javadoc)
	 * @see core.User#LogTrace(core.LogType, java.lang.Object[])
	 */
	@Override
	public void LogTrace(LogType type, Object... params)
	{
		new Throwable().printStackTrace(System.out);
		m_Log.SetLog(this, type, params);
	}

	/* (non-Javadoc)
	 * @see core.User#LogException(java.lang.Exception)
	 */
	@Override
	public void LogException(Throwable e)
	{
		LogException(e, true);
	}
	
	/* (non-Javadoc)
	 * @see core.User#LogException(java.lang.Throwable, boolean)
	 */
	@Override
	public void LogException(Throwable e, boolean log)
	{
		if ( e instanceof InvocationTargetException )
		{
			InvocationTargetException e1 = (InvocationTargetException) e;
			e1.getTargetException().printStackTrace();
		}
		else
		{
			e.printStackTrace();
		}
		m_Log.SetLog(this, eSystemErrorLogType.SYSTEM_ERROR_EXCEPTION, e.toString());
		
		if (log)
		{
			ExceptionLog.Log(e, this);
		}
	}

	/* (non-Javadoc)
	 * @see core.User#LogException(java.lang.Exception, java.lang.String)
	 */
	@Override
	public void LogException(Throwable e, String s)
	{
		e.printStackTrace();
		ExceptionLog.Log(e, this);
		m_Log.SetLog(this, eSystemErrorLogType.SYSTEM_ERROR_EXCEPTION, s + "\n" + e.toString());
	}

	/* (non-Javadoc)
	 * @see core.User#StartMethodMark(core.detail.interface_.MethodEx)
	 */
	@Override
	public void StartMethodMark(MethodEx mex)
	{
		m_Log.SetMethodMark(mex.toString());
	}

	/* (non-Javadoc)
	 * @see core.User#EndMethodMark(core.detail.interface_.MethodEx)
	 */
	@Override
	public void EndMethodMark(MethodEx mex)
	{
		m_Log.RemoveMethodMark();
	}

	/* (non-Javadoc)
	 * @see core.User#OnRunDirect()
	 */
	@Override
	public void OnRunDirect()
	{
		if ( !m_AllDataLoadFinish && DataReady() && GetState().GetDisconnect() == false )
		{
			m_AllDataLoadFinish = true;
			try
			{
				OnAllDataLoadFinish();
			}
			catch (Exception e)
			{
				LogException(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see core.User#IsDisabled()
	 */
	@Override
	public boolean IsDisabled()
	{
		return GetState().GetDisable();
	}

	/* (non-Javadoc)
	 * @see core.User#IsNeedFirstSaveToDB()
	 */
	@Override
	public boolean IsNeedFirstSaveToDB()
	{
		return m_SaveDBNum == 0;
	}
	
	public String toString()
	{
		return super.toString() + "[" + (m_sUserName == null ? "" : m_sUserName) + "," + m_lGID + "," + (m_sNick == null ? "" : m_sNick) + "]"; 
	}

	/* (non-Javadoc)
	 * @see core.User#SetCloseReason(int)
	 */
	@Override
	public void SetCloseReason(int reason, int ex)
	{
		m_CloseReason = reason;
		m_CloseEx = ex;
	}

	/* (non-Javadoc)
	 * @see core.User#GetCloseReason()
	 */
	@Override
	public int GetCloseReason()
	{
		return m_CloseReason;
	}

	/* (non-Javadoc)
	 * @see core.User#GetCloseReasonEx()
	 */
	@Override
	public int GetCloseReasonEx()
	{
		return m_CloseEx;
	}

	/* (non-Javadoc)
	 * @see core.User#SetPassword(java.lang.String)
	 */
	@Override
	public void SetPassword(String password)
	{
		m_sPassword = password;
	}

	/* (non-Javadoc)
	 * @see core.User#GetPassword()
	 */
	@Override
	public String GetPassword()
	{
		return m_sPassword;
	}

	/* (non-Javadoc)
	 * @see core.User#SetServerID(int)
	 */
	@Override
	public void SetServerID(short serverid)
	{
		m_nServerID = serverid;
	}

	/* (non-Javadoc)
	 * @see core.User#GetServerID()
	 */
	@Override
	public short GetServerID()
	{
		return m_nServerID;
	}

	/* (non-Javadoc)
	 * @see core.User#GetServerNick()
	 */
	@Override
	public String GetServerNick()
	{
		Debug.Assert(m_nServerID > 0, "UserBase#GetServerNick ServerID Error");
		return ServerNick(m_sNick, m_nServerID);
	}
	
	public static String ServerNick(String nick, int serverid)
	{
		return String.format("%s@%d", nick, serverid);
	}

	/* (non-Javadoc)
	 * @see core.User#GetServerUserName()
	 */
	@Override
	public String GetServerUserName()
	{
		return ServerNick(m_sUserName, m_nServerID);
	}
}
