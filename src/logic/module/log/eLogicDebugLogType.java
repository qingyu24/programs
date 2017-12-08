/**
 * eLogicDebugLogType.java 2012-10-23下午3:53:54
 */
package logic.module.log;

import core.LogType;
import utility.Debug;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eLogicDebugLogType implements LogType
{
	LOGIC_INFO_EXCEL_START_READ,
	LOGIC_INFO_EXCEL_READ_FINISH,
	
	LOGIC_TASK_REFRESH_DATA,
	LOGIC_TASK_REFRESH_NO_NEED,
	LOGIC_TASK_REFRESH_ALL_DATA,
	LOGIC_TASK_RECEIVE,
	LOGIC_TASK_GIVEUP,
	LOGIC_TASK_FINISH,
	LOGIC_TASK_SYN_OPER,
	
	LOGIC_FACTION_BOSS_ACTIVITY_STATUS,
	LOGIC_FACTION_BOSS_ACTIVITY_TIME,
	LOGIC_SQL_RECORD,
	;

	/* (non-Javadoc)
	 * @see core.LogType#Serialize()
	 */
	@Override
	public String Serialize()
	{
		switch ( this )
		{
		case LOGIC_INFO_EXCEL_START_READ:	return "开始读取[%s][%s]";
		case LOGIC_INFO_EXCEL_READ_FINISH:	return "读取完成[%s][%s]";
		case LOGIC_TASK_REFRESH_DATA:		return "刷新标识(%d)";
		case LOGIC_TASK_REFRESH_NO_NEED:	return "标识一样无需刷新";
		case LOGIC_TASK_REFRESH_ALL_DATA:	return "刷新所有的任务数据";
		case LOGIC_TASK_RECEIVE:			return "要接收任务(%d)";
		case LOGIC_TASK_GIVEUP:				return "要放弃任务(%d)";
		case LOGIC_TASK_FINISH:				return "要完成任务(%d)";
		case LOGIC_TASK_SYN_OPER:			return "SynOper(%s)(%d)";
		case LOGIC_FACTION_BOSS_ACTIVITY_STATUS: return "旅团BOSS活动(%d)(%s)";
		case LOGIC_FACTION_BOSS_ACTIVITY_TIME: return "旅团BOSS活动(%s)(%s)";
		case LOGIC_SQL_RECORD:         return "牌的操记录:%d : %s";
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
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#LogicLog()
	 */
	@Override
	public boolean LogicLog()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see core.LogType#DebugLog()
	 */
	@Override
	public boolean DebugLog()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see core.LogType#InfoLog()
	 */
	@Override
	public boolean InfoLog()
	{
		return false;
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
