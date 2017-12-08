/**
 * eSystemErrorLogType.java 2012-10-23上午11:15:21
 */
package core.detail.impl.log;

import utility.Debug;
import core.LogType;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eSystemErrorLogType implements LogType
{
	SYSTEM_ERROR_EXCEPTION,
	SYSTEM_ERROR_USERMGR_REMOVE_TEMPUSER,
	SYSTEM_ERROR_THREAD_CARSH,
	
	SYSTEM_ERROR_READ_NET_ERROR,
	;

	/* (non-Javadoc)
	 * @see core.LogType#Serialize()
	 */
	@Override
	public String Serialize()
	{
		switch(this)
		{
		case SYSTEM_ERROR_EXCEPTION:				return "%s";
		case SYSTEM_ERROR_USERMGR_REMOVE_TEMPUSER:	return "临时用户[%s]加载完成了,但是关键数据没有准备好";
		case SYSTEM_ERROR_THREAD_CARSH:				return "线程[%d]挂掉了 囧囧囧囧囧囧囧囧囧囧";
		case SYSTEM_ERROR_READ_NET_ERROR:			return "Link::OnRead异常,导致关闭 uid[%d] user[%s]";
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
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#ErrorLog()
	 */
	@Override
	public boolean ErrorLog()
	{
		return true;
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
