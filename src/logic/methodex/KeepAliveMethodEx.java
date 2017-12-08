/**
 * KeepAliveMethodEx.java 2013-1-14上午10:29:15
 */
package logic.methodex;

import core.Root;
import core.Tick;
import core.detail.Mgr;
import logic.MyUser;
import logic.sqlrun.KeepAliveSQLRun;
import logic.sqlrun.MySQLRun;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 一个小时来随便读取下数据,以避免连接断开的情况
 */
public class KeepAliveMethodEx extends MyMethodEx implements Tick 
{
	private static KeepAliveMethodEx m_Instance = new KeepAliveMethodEx();
	
	private static class MySaveUser extends MyUser
	{
	}
	
	public static KeepAliveMethodEx GetInstance()
	{
		return m_Instance;
	}
	
	public KeepAliveMethodEx()
	{
		super(new MySaveUser(), new KeepAliveSQLRun());
		Root.GetInstance().AddLoopTimer(this, 3600, null);
	}

	/* (non-Javadoc)
	 * @see logic.methodex.MyMethodEx#OnRunDirect(logic.MyUser, logic.sqlrun.MySQLRun)
	 */
	@Override
	public void OnRunDirect(MyUser p_User, MySQLRun p_SQLRun) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see core.Tick#OnTick(long)
	 */
	@Override
	public void OnTick(long p_lTimerID) throws Exception
	{
		Mgr.GetSqlMgr().AddTask(this);
	}
}
