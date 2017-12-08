/**
 * eUserLockType.java 2012-12-27下午4:33:02
 */
package core.detail;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eUserLockType
{
	LOCK_DATA,		//数据没有加载好,正在sql线程加载
	LOCK_SAVE,		//数据正在sql线程保存
	LOCK_ASYN,		//为以后的跨服预留
	;
}
