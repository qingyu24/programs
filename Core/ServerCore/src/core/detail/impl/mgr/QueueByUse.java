/**
 * OrderQueue.java 2012-10-18上午11:50:43
 */
package core.detail.impl.mgr;

import java.util.*;

import core.User;

import utility.Pair;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 一个根据使用时间来排序的队列,越长时间没有使用的越排在前面,一使用就会移动到队列最后
 */
public class QueueByUse<T>
{
	private static class Data<T> implements Comparable<Data<T>>
	{
		private T 		m_T;
		private long	m_LastUseTm;
		
		public Data(T t)
		{
			m_T = t;
			ResetUse();
		}
		
		public Data(T t, long tm)
		{
			m_T = t;
			m_LastUseTm = tm;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Data<T> d)
		{
			if ( m_LastUseTm < d.m_LastUseTm )
			{
				return -1;
			}
			else if ( m_LastUseTm > d.m_LastUseTm )
			{
				return 1;
			}
			else
			{
				Integer h = hashCode();
				return h.compareTo(d.hashCode());
			}
		}
		
		public T Get()
		{
			return m_T;
		}

		public long GetTime()
		{
			return m_LastUseTm;
		}
		
		public void ResetUse()
		{
			m_LastUseTm = System.currentTimeMillis();
		}
		
		public void ResetUse(long tm)
		{
			m_LastUseTm = tm;
		}
	}
	
	public static void main(String[] args)
	{
		QueueByUse<String> q = new QueueByUse<String>();
		q.Add("d", 40);
		q.Add("a", 10);
		q.Add("b", 20);
		q.Add("c", 30);
		
		String s;
		while ( (s = q.Poll()) != null )
		{
			System.out.println(s);
		}
		
		System.out.println("===============================================");
		
		q.Add("a", 10);
		q.Add("b", 20);
		q.Add("c", 30);
		q.Add("d", 40);
		
		q.Use("c", 5);
		q.Use("d", 12);
		while ( (s = q.Poll()) != null )
		{
			System.out.println(s);
		}
	}
	
	private Set<Data<T>> m_All = new TreeSet<Data<T>>();
	
	/**
	 * 添加一个对象到队列中,肯定是排在最后
	 */
	public void Add(T t)
	{
		m_All.add(new Data<T>(t));
	}
	
	public void Add(T t, long usetm)
	{
		m_All.add(new Data<T>(t, usetm));
	}
	
	public void ClearAll()
	{
		m_All.clear();
	}
	
	public ArrayList<User> GetSaveData(int max)
	{
		ArrayList<User> all = new ArrayList<User>();
		Iterator<Data<T>> it = m_All.iterator();
		while ( it.hasNext() && all.size() < max )
		{
			Data<T> d = it.next();
			User u = (User) d.m_T;
			if ( u.IsNeedFirstSaveToDB() )
			{
				all.add(u);
			}
		}
		return all;
	}
	
	/**
	 * 查看第一个
	 * 
	 * @return 当队列为空时,返回null
	 */
	public T Peek()
	{
		Iterator<Data<T>> it = m_All.iterator();
		while ( it.hasNext() )
		{
			Data<T> d = it.next();
			return d.Get();
		}
		return null;
	}
	
	public Pair<T, Long> PeekData()
	{
		Iterator<Data<T>> it = m_All.iterator();
		while ( it.hasNext() )
		{
			Data<T> d = it.next();
			return Pair.makePair(d.Get(), d.GetTime());
		}
		return null;
	}
	
	/**
	 * 取出第一个
	 * 
	 * @return 当队列为空时,返回null
	 */
	public T Poll()
	{
		Iterator<Data<T>> it = m_All.iterator();
		while ( it.hasNext() )
		{
			Data<T> d = it.next();
			m_All.remove(d);
			return d.Get();
		}
		return null;
	}
	
	public Pair<T, Long> PollData()
	{
		Iterator<Data<T>> it = m_All.iterator();
		while ( it.hasNext() )
		{
			Data<T> d = it.next();
			m_All.remove(d);
			return Pair.makePair(d.Get(), d.GetTime());
		}
		return null;
	}
	
	public void Remove(long gid)
	{
		Iterator<Data<T>> it = m_All.iterator();
		while ( it.hasNext() )
		{
			Data<T> d = it.next();
			User u = (User) d.m_T;
			if (u.GetRoleGID() == gid)
			{
				it.remove();
				break;
			}
		}
	}
	
	public int Size()
	{
		return m_All.size();
	}
	
	/**
	 * 使用一个对象,使用的对象会排在最后
	 * 
	 * @return 返回true表示成功执行
	 */
	public boolean Use(T t)
	{
		Iterator<Data<T>> it = m_All.iterator();
		while ( it.hasNext() )
		{
			Data<T> d = it.next();
			if ( d.Get() == t )
			{
				m_All.remove(d);
				d.ResetUse();
				m_All.add(d);
				return true;
			}
		}
		return false;
	}

	public boolean Use(T t, long tm)
	{
		Iterator<Data<T>> it = m_All.iterator();
		while ( it.hasNext() )
		{
			Data<T> d = it.next();
			if ( d.Get() == t )
			{
				m_All.remove(d);
				d.ResetUse(tm);
				m_All.add(d);
				return true;
			}
		}
		return false;
	}
}
