/**
 * DBLoaderEx.java 2012-8-27下午4:52:49
 */
package core;

import java.util.concurrent.*;

import utility.Rand;

import core.detail.Mgr;
import core.detail.impl.log.Log;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public abstract class DBLoaderEx<T> implements DBLoader, Tick
{
	protected ConcurrentLinkedQueue<T> m_Datas = new ConcurrentLinkedQueue<T>();
	private T m_Seed;
	
	private class SaveGlobalSQLRun implements SQLRun
	{
		/* (non-Javadoc)
		 * @see core.SQLRun#Execute(core.User)
		 */
		@Override
		public void Execute(User p_User) throws Exception
		{
			for ( T t : m_Datas )
			{
				DBMgr.UpdateRoleData(t);
			}
			DBMgr.Commit();
		}
	}
	
	private class SaveGlobalMethod extends CustomMethod
	{
		public SaveGlobalMethod()
		{
			super(Log.out, new SaveGlobalSQLRun());
		}

		/* (non-Javadoc)
		 * @see core.detail.interface_.MethodEx#CanExecute()
		 */
		@Override
		public boolean CanExecute()
		{
			return true;
		}

		/* (non-Javadoc)
		 * @see core.CustomMethod#RunDirect()
		 */
		@Override
		public void RunDirect() throws Exception
		{
		}
	}
	
	protected DBLoaderEx(T p_Seed)
	{
		m_Seed = p_Seed;
		Root.GetInstance().AddLoopTimer(this, 2 * 60 + Rand.GetIn100(), null);
		
		DBMgr.AddDBLoader(this);
	}
	
	/**
	 * 构造函数
	 * @param p_Seed
	 *            用来读取用的参数,简单的来说就是new T()的对象
	 * @param p_nSaveSecond
	 *            定时保存的时间间隔(秒)
	 */
	protected DBLoaderEx(T p_Seed, boolean p_bSave)
	{
		m_Seed = p_Seed;
		if ( p_bSave )
		{
			Root.GetInstance().AddLoopTimer(this, 2 * 60 + Rand.GetIn100(), null);
		}
		
		DBMgr.AddDBLoader(this);
	}
	
	protected T[] ToArray(T[] ts)
	{
		return m_Datas.toArray(ts);
	}
	
	protected void Add(T t)
	{
		m_Datas.add(t);
	}
	
	protected int Size()
	{
		return m_Datas.size();
	}
	
	/* (non-Javadoc)
	 * @see core.Tick#OnTick(long)
	 */
	@Override
	public void OnTick(long p_lTimerID) throws Exception
	{
		if ( Mgr.GetSqlMgr().IsBusy() )
		{
			return;
		}
		Mgr.GetSqlMgr().AddTask(new SaveGlobalMethod());
	}

	/* (non-Javadoc)
	 * @see core.DBLoader#OnLoad()
	 */
	@Override
	public void OnLoad() throws Exception
	{
		T[] ts = DBMgr.ReadAllDataAddStore(m_Seed);
		for ( T t : ts)
		{
			m_Datas.add(t);
		}
	}

	/* (non-Javadoc)
	 * @see core.DBLoader#OnSave()
	 */
	@Override
	public void OnSave()
	{
		
	}
}
