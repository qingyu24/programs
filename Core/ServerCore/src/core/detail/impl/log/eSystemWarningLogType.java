/**
 * eSystemWarningLogType.java 2012-10-23上午11:16:15
 */
package core.detail.impl.log;

import utility.Debug;
import core.LogType;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eSystemWarningLogType implements LogType
{
	SYSTEM_WARNING_REMOVE_UNFIND_TIMER_EVENT,
	;

	/* (non-Javadoc)
	 * @see core.LogType#Serialize()
	 */
	@Override
	public String Serialize()
	{
		switch(this)
		{
		case SYSTEM_WARNING_REMOVE_UNFIND_TIMER_EVENT:	return "移除定时器[%d]时,无法在定时器管理器中发现这个定时器";
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
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#WarningLog()
	 */
	@Override
	public boolean WarningLog()
	{
		return true;
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
