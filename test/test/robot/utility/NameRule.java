/**
 * NameRule.java 2012-8-3下午2:51:22
 */
package test.robot.utility;

import test.robot.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface NameRule
{
	/**
	 * 获取的名字以机器人的自身id来生成
	 */
	public final static NameRule normalRule = new NormalNameRule();
	
	/**
	 * 获取的名字以随机数来生成
	 */
	public final static NameRule randomRule = new RandomNameRule();
	
	/**
	 * 所有的机器人名字固定
	 */
	public final static NameRule solidRule = new SolidNameRule();
	
	/**
	 * 名字按照索引递增
	 */
	public final static NameRule increaseRule = new IncreaseRule();
	
	public String GetName(Robot r);
}
