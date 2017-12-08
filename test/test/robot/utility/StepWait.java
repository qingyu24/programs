/**
 * StepWait.java 2012-8-4上午10:44:47
 */
package test.robot.utility;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 单个步骤的最大等待时间,{@link TimeBatchRun}会忽略这些,使用自身的最大时间设定
 */
public class StepWait
{
	/**
	 * 最多等待5秒
	 */
	public final static StepWait s5 = new StepWait(5*1000);
	
	/**
	 * 最多等待10秒
	 */
	public final static StepWait s10 = new StepWait(10*1000);
	
	/**
	 * 最多等待20秒
	 */
	public final static StepWait s20 = new StepWait(20*1000);
	
	/**
	 * 最多等待30秒
	 */
	public final static StepWait s30 = new StepWait(30*1000);
	
	/**
	 * 最多等待60秒
	 */
	public final static StepWait s60 = new StepWait(60*1000);
	
	private int m_Time = -1;
	public StepWait(int tm)
	{
		m_Time = tm;
	}
	
	public int GetTime()
	{
		return m_Time;
	}
}
