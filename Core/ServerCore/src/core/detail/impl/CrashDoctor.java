/**
 * CrashDoctor.java 2012-10-17下午2:21:53
 */
package core.detail.impl;

import core.*;
import core.db.sql.*;
import core.detail.*;
import core.detail.impl.log.*;
import core.ex.ShowMgr;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class CrashDoctor
{
	private static CrashDoctor m_Instance = new CrashDoctor();
	
	public static CrashDoctor GetInstance()
	{
		return m_Instance;
	}
	private boolean m_Stop = false;
	
	CrashDoctor()
	{
		
	}
	
	/**
	 * 当某个线程"挂了"后的回调
	 */
	public void OnCrashThread(long threadid)
	{
		Log.out.Log(eSystemErrorLogType.SYSTEM_ERROR_THREAD_CARSH, threadid);
	}
	
	public void OnCtrlCShutDown()
	{
		if ( m_Stop )
		{
			return;
		}
		m_Stop = true;
		
		ShowMgr.GetInfo().Close();
		
		_ReadyCloseServer(RootConfig.GetInstance().SD_CloseServerCountdown);
		
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_EXIT_START);
		
		Mgr.Get().StopUserEntry();
		
		SystemFn.Sleep(2000);
		
		Log.SetOutPutErr(true);
		
		SaveAllData();
		
		SystemFn.Sleep(1000);
		
		Mgr.Get().StopAllThread();
		
		SystemFn.Sleep(1000);
		
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_EXIT_FINISH);
	}
	
	public void SaveAllData()
	{
		try
		{
			SaveAllUserData();
		}
		catch(Exception e)
		{
			Log.out.LogException(e);
		}
		
		try
		{
			SaveOtherData();
		}
		catch(Exception e)
		{
			Log.out.LogException(e);
		}
	}
	
	public void SaveAllUserData()
	{
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SAVE_ALL_USERDATA, "SaveAllUserData::Start");
		
		Mgr.GetUserMgr().SaveAllUserData();
		
		while (true)
		{
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SAVE_ALL_USERDATA, Mgr.GetUserMgr().toString());
			
			if ( Mgr.GetUserMgr().ExecuteSaveAllUserDataFinish())
			{
				break;
			}
			
			SystemFn.Sleep(500);
		}
		
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SAVE_ALL_USERDATA, "SaveAllUserData::Finish");
	}
	
	public void SaveOtherData()
	{
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SAVE_ALL_USERDATA, "SaveOtherData::Start");
		//保存全局数据
		DBStores.GetInstance().SaveAllData();
		
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SAVE_ALL_USERDATA, "SaveOtherData::Finish");
	}
	
	public void TestOnCtrlCShutDown()
	{
		new Thread()
		{
			public void run()
			{
				CrashDoctor.GetInstance().OnCtrlCShutDown();
			}
		}.start();
	}
	
	private void _ReadyCloseServer(int second)
	{
		while (second > 0 )
		{
			System.err.println(""+ second + "秒后关服");
			Root.GetInstance().OnServerCloseCallBack(second);
			second--;
			SystemFn.Sleep(1000);
		}
	}
}
