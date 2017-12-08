/**
 * ThreadMgrImpl.java 2012-10-17下午2:30:13
 */
package core.detail.impl.mgr;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import utility.Debug;

import core.detail.impl.*;
import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ThreadMgrImpl implements ThreadMgr
{
	private Map<Long, eThreadType> m_All = new ConcurrentHashMap<Long, eThreadType>();
	/* (non-Javadoc)
	 * @see core.detail.interface_.ThreadMgr#GetCurrThreadType()
	 */
	@Override
	public eThreadType GetCurrThreadType()
	{
		long id = Thread.currentThread().getId();
		if ( m_All.containsKey(id) )
		{
			return m_All.get(id);
		}
		return eThreadType.NULL;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.ThreadMgr#IsEmpty()
	 */
	@Override
	public boolean IsEmpty()
	{
		System.err.println(toString());
		return m_All.isEmpty();
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.ThreadMgr#Reg(java.lang.Thread, core.detail.impl.eThreadType)
	 */
	@Override
	public void Reg(Thread t, eThreadType type)
	{
		Debug.Assert(m_All.containsKey(t.getId()) == false, "");
		m_All.put(t.getId(), type);
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.ThreadMgr#Remove(core.detail.impl.eThreadType)
	 */
	@Override
	public void Remove(eThreadType type)
	{
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_THREAD_STOP, "[StartRemove:" + type + "]");
		long id = Thread.currentThread().getId();
		Debug.Assert(m_All.containsKey(id), "ThreadMgrImple::Remove只能移除已经注册的线程,线程[" + id + "]无法找到");
		Debug.Assert(m_All.get(id) == type, "ThreadMgrImple::Remove只运行本线程主动移除,线程[" + id + "]的类型是[" + m_All.get(id) + "],而指定移除的类型是[" + type + "]");
		m_All.remove(id);
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_THREAD_STOP, "["+ id + "," + type + "]");
	}

	public String toString()
	{
		String s = super.toString() + "\n";
		Iterator<Entry<Long, eThreadType>> it = m_All.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<Long, eThreadType> e = it.next();
			s += "[" + e.getKey() + "," + e.getValue() + "]";
		}
		return s;
	}
}
