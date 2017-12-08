/**
 * TimeEvaluate.java 2012-8-4下午12:22:12
 */
package test.robot.utility;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 根据机器人的反馈来进行简单的加权平均,以判断某个机器人是否健康
 */
public class TimeEvaluate
{
	@SuppressWarnings("unused")
	private int[] m_All;
	/**
	 * @param num 采样数量
	 */
	public TimeEvaluate(int num)
	{
		m_All = new int[num];
	}
	
	public void Set(int usetm)
	{
		
	}
	
	public boolean IsHealth(int usetm)
	{
		return true;
	}
}
