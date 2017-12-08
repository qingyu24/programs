/**
 * Root.java 2012-6-11下午9:39:06
 */
package core;

import java.text.*;
import java.util.*;

import utility.*;
import core.detail.*;
import core.detail.impl.*;
import core.detail.impl.log.*;
import core.detail.impl.mgr.methodmgr.MethodType;
import core.detail.interface_.*;
import core.ex.*;
import core.exception.RegException;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Root
{
	static private Root m_Instance = null;
	
	protected Factory m_Factory;
	
	protected Root()
	{
		if ( m_Instance == null )
		{
			m_Instance = this;
			Mgr.GetThreadMgr().Reg(Thread.currentThread(), eThreadType.MAIN);
			
			if ( RootConfig.GetInstance().ShutdownHook )
			{
				Runtime.getRuntime().addShutdownHook(new Thread()
				{
					public void run()
					{
						CrashDoctor.GetInstance().OnCtrlCShutDown();
					}
				}
				);
			}
			return;
		}
		Debug.Assert(false, "");
	}
	
	static public Root GetInstance()
	{
		return m_Instance;
	}
	
	public void StopMainThread()
	{
		Mgr.GetThreadMgr().Remove(eThreadType.MAIN);
	}
	
	/**
	 * 触发所有的网络注册
	 */
	protected void RegAll()
	{
		m_Factory.OnRegAllCallBack();
	}
	
	protected void RFCBuild()
	{
		for ( MethodMgr m : MethodType.GetAllMethodMgr() )
		{
			m.RFCBuild();
		}
	}
	
	/**
	 * 开启线程
	 */
	protected void StartAllThread()
	{
		Mgr.Get().StartAllThread();
	}
	
	/**
	 * 打开显示
	 */
	protected void StartShow()
	{
		ShowMgr.Run();
	}
	
	public void OnServerCloseCallBack(int second)
	{
		try
		{
			m_Factory.OnServerCloseCallBack(second);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Factory GetFactory()
	{
		return m_Factory;
	}
	
	/**
	 * 获取一个用户对象
	 */
	public User NewUser()
	{
		return m_Factory.NewUser();
	}
	
	/**
	 * 注册一个网络消息回调对象
	 *
	 * @param p_regObj 注册的实例对象
	 */
	public void Reg(Object p_regObj) throws RegException
	{
		Mgr.GetRunMethodMgr(MethodType.NORMAL).Reg(p_regObj);
		Mgr.GetRunMethodMgr(MethodType.TEST).Reg(p_regObj);
	}
	
	/**
	 * 注册一个GM网络消息
	 * 
	 * @param p_regObj 注册的实例对象
	 */
	public void RegGM(Object p_regObj) throws RegException
	{
		Mgr.GetRunMethodMgr(MethodType.GM).Reg(p_regObj);
	}

	/**
	 * 添加一个SQL任务
	 *
	 * @param p_User	所对应的User对象
	 * @param p_SqlRun	要执行的SQL功能
	 */
	public void AddSQLRun(MethodEx p_Method)
	{
		Mgr.GetSqlMgr().AddTask(p_Method);
	}

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param p_User
	 */
	public void AttachUser(User p_User)
	{
		Mgr.GetUserMgr().AttachUser(p_User);
	}
	
	/**
	 * 添加触发一次的定时器
	 * 
	 * @param tick
	 *            触发的对象
	 * @param p_nSecond
	 *            多久后被触发,单位秒
	 * 
	 * @return 定时器的id
	 */
	public long AddOnceTimer(Tick tick,int p_nSecond, User p_User)
	{
		return Mgr.GetTimerMgr().AddTimer(p_nSecond, tick, 1, p_User != null ? p_User.hashCode() : 0);
	}
	
	/**
	 * 添加循环触发的定时器
	 * 
	 * @param tick
	 *            触发的对象
	 * @param p_nSecond
	 *            多久后被触发,单位秒
	 *            
	 * @return 定时器的id
	 */
	public long AddLoopTimer(Tick tick,int p_nSecond, User p_User)
	{
		return Mgr.GetTimerMgr().AddTimer(p_nSecond, tick, -1, p_User != null ? p_User.hashCode() : 0);
	}
	
	public long AddLoopMilliTimer(Tick tick,long p_nMilliSecond, User p_User)
	{
		return Mgr.GetTimerMgr().AddMilliTimer(p_nMilliSecond, tick, -1, p_User != null ? p_User.hashCode() : 0);
	}
	
	/**
	 * 根据指定时间,添加循环触发的定时器
	 * <br>如果设定为中午12点触发,以30分钟的频率触发
	 * <br>当服务器在8点10开启时,就在8:30,9:00,9:30,10:00...都触发此事件
	 * <br>当服务器在12:10开启时,就在12:30,13:00,13:30...都触发此事件
	 * 
	 * @param tick
	 *            回调对象
	 * @param p_sTime
	 *            指定的时间,格式为"HH:mm:ss"
	 * @param p_nSecond
	 *            给定时间间隔
	 * 
	 * @return 定时器id,如果创建失败会返回-1
	 */
	public long AddDefineTimer(Tick tick, String p_sTime, int p_nSecond, User p_User)
	{
		try
		{
			return Mgr.GetTimerMgr().AddDefineTimer(p_nSecond, tick, p_sTime, p_User != null ? p_User.hashCode() : 0);
		}
		catch (ParseException e)
		{
			Log.out.LogException(e);
			return -1;
		}
	}
	
	/**
	 * 根据指定时间,添加循环触发的定时器
	 * <br>如果设定为2013.6.1 12:00:00触发,以12小时的频率触发
	 * <br>当服务器在2013.5.31 8:00:00开启,就在2013.5.31 12:00:00,2013.6.1 00:00:00,2013.6.1 12:00:00...触发此事件
	 * <br>当服务器在2013.6.1 13:00:00开启,就在2013.6.2 00:00:00 2013.6.2 12:00:00...触发此事件
	 *
	 * @param tick
	 * 			回调对象
	 * @param p_sDayTime
	 * 			指定的时间,格式为"年-月-日 小时:分:秒"
	 * @param p_nSecond
	 * 			给定时间间隔
	 * @param p_User
	 * 			是否和某个用户对象绑定
	 * 
	 * @return	定时器id,如果创建失败会返回-1
	 */
	public long AddDefineDayTimer(Tick tick, String p_sDayTime, int p_nSecond, User p_User)
	{
		try
		{
			return Mgr.GetTimerMgr().AddDefineDayTimer(p_nSecond, tick, p_sDayTime, p_User != null ? p_User.hashCode() : 0);
		}
		catch (ParseException e)
		{
			Log.out.LogException(e);
			return -1;
		}
	}
	
	/**
	 * 根据指定时间,添加循环触发的定时器
	 * <br>如果设定为中午12点触发,以30分钟的频率触发
	 * <br>当服务器在8点10开启时,就在12:00,12:30,13:00,13:30...都触发此事件
	 * <br>当服务器在12点10开启时,就在12:30,13:00,13:30...都触发事件
	 * 
	 * @param tick
	 *            回调对象
	 * @param p_sTime
	 *            指定的时间,格式为"HH:mm:ss"
	 * @param p_nSecond
	 *            给定时间间隔
	 * 
	 * @return 定时器id,如果创建失败会返回-1
	 */
	public long AddSolidDefineTimer(Tick tick, String p_sTime, int p_nSecond, User p_User)
	{
		try
		{
			return Mgr.GetTimerMgr().AddSolidDefineTimer(p_nSecond, tick, p_sTime, p_User != null ? p_User.hashCode() : 0);
		}
		catch (ParseException e)
		{
			Log.out.LogException(e);
			return -1;
		}
	}
	
	/**
	 * 根据指定时间,添加循环触发的定时器
	 * <br>如果设定为2013.6.1 12:00:00触发,以12小时的频率触发
	 * <br>当服务器在2013.5.31 8:00:00开启,就在2013.6.1 12:00:00 2013.6.2 00:00:00...触发此事件
	 * <br>当服务器在2013.6.1 13:00:00开启,就在2013.6.2 00:00:00 2013.6.2 12:00:00...触发此事件
	 * 
	 * @param tick
	 *            回调对象
	 * @param p_sTime
	 *            指定的时间,格式为"YYYY-MM-DD HH:mm:ss"
	 * @param p_nSecond
	 *            给定时间间隔
	 * 
	 * @return 定时器id,如果创建失败会返回-1
	 */
	public long AddSolidDefineDayTimer(Tick tick, String p_sDayTime, int p_nSecond, User p_User)
	{
		try
		{
			return Mgr.GetTimerMgr().AddSolidDefineDayTimer(p_nSecond, tick, p_sDayTime, p_User != null ? p_User.hashCode() : 0);
		}
		catch (ParseException e)
		{
			Log.out.LogException(e);
			return -1;
		}
	}
	
	/**
	 * 删除定时器
	 * 
	 * @param p_lTimerID
	 *            要删除的定时器id
	 */
	public void RemoveTimer(long p_lTimerID)
	{
		Mgr.GetTimerMgr().RemoveTimer(p_lTimerID);
	}

	/**
	 * 根据用户名来获取User对象
	 */
	public User GetUserByUserName(String p_username)
	{
		return Mgr.GetUserMgr().GetUserByUserName(p_username);
	}
	
	/**
	 * 更加用户昵称来获取User对象
	 */
	public User GetUserByNick(String p_nick, int p_serverid)
	{
		return OnlineUserSelector.GetInstance().GetUserByNick(p_nick, p_serverid);
	}
	
	/**
	 * 获取用户唯一id
	 */
	public User GetUserByGid(long p_gid)
	{
		return OnlineUserSelector.GetInstance().GetUserByRID(p_gid);
	}
	
	/**
	 * 获取所有在线的用户列表(在选定了角色并且注册到用户管理器的用户)
	 */
	public ArrayList<User> GetAllOnlineUser()
	{
		return Mgr.GetUserMgr().GetAllOnlineUser();
	}
	
	/**
	 * 添加一个和逻辑层同步刷新的对象
	 */
	public void AddUpdater(Frame f)
	{
		Mgr.GetLogicMgr().AddUpdater(f);
	}
}
