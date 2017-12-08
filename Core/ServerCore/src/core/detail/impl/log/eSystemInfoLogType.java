/**
 * eSystemInfoLogType.java 2012-10-23上午11:17:01
 */
package core.detail.impl.log;

import utility.*;
import core.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eSystemInfoLogType implements LogType
{
	SYSTEM_INFO_NORMAL,
	SYSTEM_INFO_OPEN_PORT,
	SYSTEM_INFO_REOPEN_PORT,
	SYSTEM_INFO_OPEN_PORT_FINISH,
	SYSTEM_INFO_OPEN_SQL,
	SYSTEM_INFO_OPEN_SQL_FINISH,
	SYSTEM_INFO_ADD_TIMER,
	SYSTEM_INFO_REMOVE_TIMER,
	SYSTEM_INFO_REMOVE_TIMER_MANUAL,
	SYSTEM_INFO_REMOVE_TIMER_DISCONNECT,
	SYSTEM_INFO_SQL_STATEMENT,
	SYSTEM_INFO_SQL_READ_NUM,
	SYSTEM_INFO_USERMGR_ADD,
	SYSTEM_INFO_USERMGR_ADD_FINISH,
	SYSTEM_INFO_USERMGR_ADD_TEMP,
	SYSTEM_INFO_USERMGR_REMOVE,
	SYSTEM_INFO_USERMGR_MERGE,
	SYSTEM_INFO_USERMGR_DISCONNECT,
	SYSTEM_INFO_USERMGR_RELEASE_FULL,
	SYSTEM_INFO_USERMGR_RELEASE_NORMAL,
	SYSTEM_INFO_UNLOAD_MODULE,
	SYSTEM_INFO_METHOD_REG,
	SYSTEM_INFO_METHOD_REG_FINISH,
	SYSTEM_INFO_METHOD_ADD_METHOD,
	SYSTEM_INFO_LINK_USER,
	SYSTEM_INFO_LINK_CLOSE,
	SYSTEM_INFO_LINK_READY_REMOVE,
	SYSTEM_INFO_LINK_CHECK_TIME,
	SYSTEM_INFO_LINK_LIMIT,
	SYSTEM_INFO_LINK_OK,
	SYSTEM_INFO_EXIT_START,
	SYSTEM_INFO_EXIT_FINISH,
	SYSTEM_INFO_SAVE_ALL_USERDATA,
	SYSTEM_INFO_STOP_USER_ENTRY,
	SYSTEM_INFO_THREAD_STOP,
	SYSTEM_INFO_START_STOP_THREAD,
	SYSTEM_INFO_GLOBAL_DATA,
	
	SYSTEM_INFO_DETAIL,
	SYSTEM_INFO_SQLLOG,
	SYSTEM_INFO_READ_CONFIG_FILE,
	SYSTEM_INFO_READ_CONFIG_FILE_FINISHED
	;

	/* (non-Javadoc)
	 * @see core.LogType#Serialize()
	 */
	@Override
	public String Serialize()
	{
		switch (this)
		{
		case SYSTEM_INFO_NORMAL: 			return "[%s]";
		case SYSTEM_INFO_OPEN_PORT: 		return "正在打开端口[%d]";
		case SYSTEM_INFO_REOPEN_PORT: 		return "端口异常,正在准备重新打开端口!!!";
		case SYSTEM_INFO_OPEN_PORT_FINISH: 	return "打开端口[%d]成功";
		case SYSTEM_INFO_OPEN_SQL: 			return "开始连接数据库[%s]";
		case SYSTEM_INFO_OPEN_SQL_FINISH: 	return "数据库连接成功[%s]";
		case SYSTEM_INFO_ADD_TIMER:		 	return "添加一个定时器[%d][%s]";
		case SYSTEM_INFO_REMOVE_TIMER:		return "功能完成,自动移除了一个定时器[%d]";
		case SYSTEM_INFO_REMOVE_TIMER_MANUAL:return "逻辑层主动移除了一个定时器[%d]";
		case SYSTEM_INFO_REMOVE_TIMER_DISCONNECT:	return "用户下线导致移除了一个定时器[%d]";
		case SYSTEM_INFO_SQL_STATEMENT:		return "用时[%d] SQL:[%s]";
		case SYSTEM_INFO_SQL_READ_NUM:		return "读取到[%d]条数据";
		case SYSTEM_INFO_USERMGR_ADD:		return "UserMgr添加一个用户,nk[%s] gid[%d] netid[%d] this[%s]";
		case SYSTEM_INFO_USERMGR_ADD_FINISH:return "UserMgr成功添加一个用户,nk[%s] gid[%d] netid[%d] this[%s]";
		case SYSTEM_INFO_USERMGR_ADD_TEMP:	return "UserMgr添加一个临时用户,nk[%s] gid[%d] netid[%d] this[%s]";
		case SYSTEM_INFO_USERMGR_MERGE:		return "用户 nk[%s] gid[%d] netid[%d] this[%s] 合并了用户 nk[%s] gid[%d] netid[%d] this[%s]";
		case SYSTEM_INFO_USERMGR_DISCONNECT:return "User对象断开了 nk[%s] gid[%d] netid[%d] this[%s] reason:[%d] ex:[%d]";
		case SYSTEM_INFO_USERMGR_RELEASE_FULL:return "因为用户数量过大,断开 释放一批User对象  nk[%s] gid[%d] netid[%d] this[%s]";
		case SYSTEM_INFO_USERMGR_RELEASE_NORMAL:return "平时检查释放的User对象  nk[%s] gid[%d] netid[%d] this[%s]";
		case SYSTEM_INFO_USERMGR_REMOVE:	return "UserMgr成功释放一个用户,nk[%s] gid[%d] netid[%d] this[%s]";
		case SYSTEM_INFO_UNLOAD_MODULE:		return "模块%s被卸载,所有相关的功能无法执行";
		case SYSTEM_INFO_METHOD_REG:		return "开始注册模块[%s]";
		case SYSTEM_INFO_METHOD_REG_FINISH:	return "完成注册[%s]";
		case SYSTEM_INFO_METHOD_ADD_METHOD:	return "添加[%d,%d][%s,%s]";
		case SYSTEM_INFO_LINK_USER:			return "为连接[%s]创建了用户[%s]";
		case SYSTEM_INFO_LINK_CLOSE:		return "断开了连接[%s]";
		case SYSTEM_INFO_LINK_READY_REMOVE:	return "准备从管理器去掉用户[%s][%s]";
		case SYSTEM_INFO_LINK_CHECK_TIME:	return "当前时间[%s],最后一次收到的时间[%s],最大等待时间[%d]";
		case SYSTEM_INFO_LINK_LIMIT:		return "到达连接上限,强行断开连接";
		case SYSTEM_INFO_LINK_OK:			return "有一个客户端连接上来linkhash[%d] userhash[%d]";
		case SYSTEM_INFO_EXIT_START:		return "服务器正准备关闭,请稍候";
		case SYSTEM_INFO_EXIT_FINISH:		return "服务器执行完清理工作,GameOver:" + Debug.GetCurrTime();
		case SYSTEM_INFO_SAVE_ALL_USERDATA:	return "保存所有的用户数据[%s] " + Debug.GetCurrTime();
		case SYSTEM_INFO_STOP_USER_ENTRY:	return "停止用户的入口[%s] " + Debug.GetCurrTime();
		case SYSTEM_INFO_THREAD_STOP:		return "线程正常停止[%s] " + Debug.GetCurrTime();
		case SYSTEM_INFO_START_STOP_THREAD:	return "开始停止线程[%s] " + Debug.GetCurrTime();
		case SYSTEM_INFO_GLOBAL_DATA:		return "系统正在保存全局数据[%s] 一共有[%d]条数据";
		case SYSTEM_INFO_DETAIL:			return "[%s]用时[%d]过多,连接数[%d]用户数[%d],细则\r\n%s";
		case SYSTEM_INFO_SQLLOG:			return "执行[%d]条sql日志,耗时[%d],剩余[%d],要保存的总数[%d],已经保存的总数[%d]";
		case SYSTEM_INFO_READ_CONFIG_FILE:  return "读取配置文件:[%s]";
		case SYSTEM_INFO_READ_CONFIG_FILE_FINISHED:  return "读取配置文件完成:[%s]";
		
		default: Debug.Assert(false , "未执行的类型:" + this);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see core.LogType#SystemLog()
	 */
	@Override
	public boolean SystemLog()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see core.LogType#LogicLog()
	 */
	@Override
	public boolean LogicLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#DebugLog()
	 */
	@Override
	public boolean DebugLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#InfoLog()
	 */
	@Override
	public boolean InfoLog()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see core.LogType#WarningLog()
	 */
	@Override
	public boolean WarningLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#ErrorLog()
	 */
	@Override
	public boolean ErrorLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#SQLLog()
	 */
	@Override
	public boolean SQLLog()
	{
		return false;
	}
}
