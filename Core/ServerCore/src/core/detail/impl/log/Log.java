/**
Created by murphy 10/12/2012
example:
	Log.Instance().debug("HelloWorld");
	Log.Instance().info("HelloWorld");
	Log.Instance().error("HelloWorld");
 */

package core.detail.impl.log;

import java.util.Formatter;
import java.util.Locale;

import org.apache.log4j.*;

import core.*;

import utility.*;

public class Log 
{
	public final static User out = new LogUser();
	private static boolean m_Init = false;
	
	private static void Init()
	{
		if ( !m_Init )
		{
			PropertyConfigurator.configure("data/config/log4jconf.properties");
			m_Init = true;
		}
	}
	
	private Logger m_Logger;
	private String m_MethodMark = "";
	private static boolean m_OutPutErr = false;
	private static Logger m_StaticLogger;
	
	public Log(String name)
	{
		Init();
		m_Logger = Logger.getLogger(name);
		if ( m_StaticLogger == null )
		{
			m_StaticLogger = Logger.getLogger("");
		}
	}
	
	public static void SetOutPutErr(boolean c)
	{
		m_OutPutErr = c;
	}
	
	public void SetLog(User p_User, LogType type, Object... params)
	{
		Formatter formatter = new Formatter(Locale.CHINESE);
		if ( type.SystemLog() )
		{
			if ( type.ErrorLog() )
			{
				SystemError(formatter, type, params);
			}
			else if ( type.WarningLog() )
			{
				SystemWarning(formatter, type, params);
			}
			else if ( type.InfoLog() )
			{
				SystemInfo(formatter, type, params);
			}
			else if (type.SQLLog() )
			{
				SystemSQL(formatter, type, params);
			}
			else
			{
				Debug.Assert(false, "");
			}
			if ( m_OutPutErr )
			{
				_GetMsg(formatter, type, params);
				System.err.println(formatter);
			}
		}
		else if ( type.LogicLog() )
		{
			if ( type.ErrorLog() )
			{
				Error( formatter, type, p_User, params );
			}
			else if ( type.WarningLog() )
			{
				Warning(formatter, type, p_User, params);
			}
			else if ( type.InfoLog() )
			{
				Info(formatter, type, p_User, params);
			}
			else if ( type.DebugLog() )
			{
				Debug(formatter, type, p_User, params);
			}
			else if ( type.SQLLog() )
			{
				SQL(formatter, type, params);
			}
			else
			{
				Debug.Assert(false, "");
			}
		}
	}
	
	public void SystemError(Formatter formatter, LogType type, Object... params)
	{
		_GetMsg(formatter, type, params);
		m_Logger.log(SystemLevel.SYSTEM_ERROR, formatter);
	}
	
	public void SystemWarning(Formatter formatter, LogType type, Object... params)
	{
		_GetMsg(formatter, type, params);
		m_Logger.log(SystemLevel.SYSTEM_WARNING, formatter);
	}
	
	public void SystemInfo(Formatter formatter, LogType type, Object... params)
	{
		_GetMsg(formatter, type, params);
		m_Logger.log(SystemLevel.SYSTEM_INFO, formatter);
	}
	
	public void SystemSQL(Formatter formatter, LogType type, Object[] params)
	{//因为发现log4j存sql日志卡,因此提到一个线程来试试
		SQL(formatter, type, params);
	}
	
	public static void ExecuteSQL(String sql)
	{
		if (m_StaticLogger != null )
		{
			m_StaticLogger.log(SystemLevel.SQL, sql);
		}
		else
		{
			System.err.println("无法找到全局的Logger");
		}
	}
	
	public void Error(Formatter formatter, LogType type, User p_User, Object... params)
	{
		_GetMsg(formatter, type, p_User, params);
		m_Logger.error(formatter);
	}
	
	public void Warning(Formatter formatter, LogType type, User p_User, Object... params)
	{
		_GetMsg(formatter, type, p_User, params);
		m_Logger.warn(formatter);
	}
	
	public void Info(Formatter formatter, LogType type, User p_User, Object... params)
	{
		_GetMsg(formatter, type, p_User, params);
		m_Logger.info(formatter);
	}
	
	public void Debug(Formatter formatter, LogType type, User p_User, Object... params)
	{
		_GetMsg(formatter, type, p_User, params);
		m_Logger.debug(formatter);
	}
	
	public void SQL(Formatter formatter, LogType type, Object[] params)
	{
		_GetMsg1(formatter, type, params);
		m_Logger.log(SystemLevel.SQL, formatter);
//		m_Logger.log(SystemLevel.SQL, _GetMsg1(type, params));
//		String s = _GetMsg1(type, params);
//		Mgr.GetSQLLogMgr().AddSQLLog(s);
	}
	
	private void _GetMsg(Formatter formatter, LogType type, User p_User, Object... params)
	{
		_GetMsg(formatter, type, params);
		StringBuilder b = (StringBuilder) formatter.out();
		b.insert(0, "]");
		b.insert(0, p_User.GetRoleGID());
		b.insert(0, "[");
	}
	
	private void _GetMsg(Formatter formatter, LogType type, Object... params)
	{
		_GetMsg1(formatter, type, params);
		StringBuilder b = (StringBuilder) formatter.out();
		b.insert(0, m_MethodMark);
		if ( type.SystemLog() )
		{
			b.insert(0, "#");
		}
		else
		{
			b.insert(0, "*");
		}
	}
	
	private void _GetMsg1(Formatter formatter,LogType type, Object... params)
	{
		String s = type.Serialize();
		if ( s == null )
		{
			return;
		}
		
		try
		{
			formatter.format(s, params);
		}
		catch(Exception e)
		{
			//对日志的异常不记录,经常会包含's'的字符串而导致再次记录的错误
			Log.out.LogException(e, false);
		}
	}

	public void SetMethodMark(String mark)
	{
		m_MethodMark = mark;
	}
	
	public void RemoveMethodMark()
	{
		m_MethodMark = "";
	}
}
