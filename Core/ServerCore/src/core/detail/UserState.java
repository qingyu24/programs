/**
 * UserState.java 2012-6-16下午2:37:38
 */
package core.detail;

import java.util.*;

import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 以后在使用热数据时特别检查这个state,不应该使用老对象的state
 */
public class UserState
{
	private boolean m_Disable	= false;
	private boolean	m_TempUser	= false;
	private boolean	m_KeyDataLoad = false;
//	private boolean m_SaveSQL = false;
	private boolean m_Managed = false;
	private boolean m_Disconnect = false;
	private ArrayList<eUserLockType> m_Locks = new ArrayList<eUserLockType>();
	
	public void SetLock(eUserLockType type, boolean b)
	{
		if ( b )
		{
			Debug.Assert(m_Locks.contains(type) == false, "用户状态中已经有:" + type + "的锁了,不允许重复锁定");
			m_Locks.add(type);
		}
		else
		{
			m_Locks.remove(type);
		}
	}
	
	public boolean CanSaveData()
	{
		return !m_Locks.contains(eUserLockType.LOCK_SAVE);
	}

	public boolean IsLock()
	{
		return !m_Locks.isEmpty();
	}

	public void SetDisable()
	{
		m_Disable = true;
	}

	public boolean GetDisable()
	{
		return m_Disable;
	}

	public void SetTempUser(boolean b)
	{
		m_TempUser = b;
	}

	public boolean GetTempUser()
	{
		return m_TempUser;
	}

	public void SetKeyDataLoad(boolean b)
	{
		m_KeyDataLoad = b;
	}

	public boolean GetKeyDataLoad()
	{
		return m_KeyDataLoad;
	}
	
	public void SetDisconnect(boolean c)
	{
		m_Disconnect = c;
	}
	
	public boolean GetDisconnect()
	{
		return m_Disconnect;
	}

//	public void SetSaveSQL(boolean b)
//	{
//		m_SaveSQL = b;
//	}
//
//	public boolean GetSaveSQL()
//	{
//		return m_SaveSQL;
//	}

	/**
	 * 是否被UserStore管理
	 */
	public void SetManaged(boolean b)
	{
		m_Managed = b;
	}
	
	public boolean GetManaged()
	{
		return m_Managed;
	}
}
