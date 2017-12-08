/**
 * LogEvent.java 2012-10-27下午3:00:28
 */
package core;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface LogEvent
{
	/**
	 * 当日志事件触发时的回调
	 *
	 * @param p_User 如果为全局的(Log.out),那么这个为null
	 */
	void OnLogEvent(User p_User, LogType type, Object[] params) throws Exception;
}
