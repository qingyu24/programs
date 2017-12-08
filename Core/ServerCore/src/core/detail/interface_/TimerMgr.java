/**
 * TimerMgr.java 2012-7-7下午1:49:54
 */
package core.detail.interface_;

import java.text.ParseException;

import core.Tick;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface TimerMgr
{

	/**
	 * 添加一个触发器
	 * 
	 * @param p_nSecond
	 *            多久触发一次
	 * @param tick
	 *            触发的对象
	 * @param num
	 *            触发的次数
	 * 
	 * @return 返回创建的定时器id
	 */
	long AddTimer(int p_nSecond, Tick tick, int num ,int markID);
	
	/**
	 * 添加一个以毫秒为单位的计时器
	 */
	long AddMilliTimer(long p_nMilliSecond, Tick tick, int num ,int markID);
	
	/**
	 * 更新操作,内部这时来进行定时器的调度
	 */
	void Update();

	/**
	 * 添加一个触发器
	 * 
	 * @param p_nSecond
	 *            多久触发一次
	 * @param tick
	 *            触发的对象
	 * @param p_sTime
	 *            设定的事件
	 * @return 返回创建的定时器id
	 * 
	 * @throws ParseException 
	 */
	long AddDefineTimer(int p_nSecond, Tick tick, String p_sTime ,int markID) throws ParseException;
	
	long AddDefineDayTimer(int p_nSecond, Tick tick, String p_sDayTime ,int markID) throws ParseException;

	/**
	 * 添加一个触发器
	 * 
	 * @param p_nSecond
	 *            多久触发一次
	 * @param tick
	 *            触发的对象
	 * @param p_sTime
	 *            设定的事件
	 * @return 返回创建的定时器id
	 * 
	 * @throws ParseException 
	 */
	long AddSolidDefineTimer(int p_nSecond, Tick tick, String p_sTime ,int markID) throws ParseException;
	
	long AddSolidDefineDayTimer(int p_nSecond, Tick tick, String p_sDayTime ,int markID) throws ParseException;

	/**
	 * 删除定时器
	 * 
	 * @param p_lTimerID
	 *            定时器id
	 */
	void RemoveTimer(long p_lTimerID);
	
	/**
	 * 删除所有相同的markid的定时器
	 */
	void RemoveBatchByMarkID(int markID);
}
