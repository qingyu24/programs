/**
 * Batch.java 2012-7-31上午11:44:30
 */
package test.robot.step;

import test.robot.Robot;
import test.robot.utility.StepWait;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public abstract class Step
{
	private StepWait m_Wait = StepWait.s5;
	
	public void SetWait(StepWait w)
	{
		m_Wait = w;
	}
	
	public StepWait GetWait()
	{
		return m_Wait;
	}
	
	/**
	 * Clone自己
	 */
	public abstract Step Clone();
	
	/**
	 * 给服务器发送的消息
	 * 
	 * @return 返回是否还需要执行,true表示结束了,无须再执行
	 * @throws InterruptedException 
	 */
	public abstract boolean Execute(Robot r) throws InterruptedException;
	
	/**
	 * 对反馈的检测
	 * 
	 * @return 表示是否已经获得了执行的结果了,true表示收到了
	 */
	public abstract boolean Executed(Robot r) throws Exception;
	
	/**
	 * 获取结果,成功则返回true
	 * @throws InterruptedException 
	 */
	public abstract boolean Result(Robot r) throws InterruptedException;
}
