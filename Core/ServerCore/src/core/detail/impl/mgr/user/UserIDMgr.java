/**
 * UserIDMgr.java 2012-7-3下午2:55:33
 */
package core.detail.impl.mgr.user;

import java.util.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 用来分配用户id
 */
public class UserIDMgr
{
	private int m_Min;
	private int m_Max;
	private int m_Curr;
	private Queue<Integer> m_All = new LinkedList<Integer>();
	
	public UserIDMgr(int min,int max)
	{
		m_Min = min;
		m_Max = max;
		m_Curr = m_Min;
	}
	/**
	 * 获取一个可用的id
	 */
	public int GetID()
	{
		if ( m_All.isEmpty() )
		{
			if ( m_Curr > m_Max )
			{
				return -1;
			}
			int id = m_Curr;
			m_Curr++;
			return id;
		}
		else
		{
			int id = m_All.poll();
			return id;
		}
	}

	/**
	 * 释放一个id
	 */
	public void ReleaseID(int id)
	{
		m_All.add(id);
	}
}
