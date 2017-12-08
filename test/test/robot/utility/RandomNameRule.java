/**
 * RandomNameRule.java 2012-10-15上午10:25:42
 */
package test.robot.utility;

import java.util.Random;

import test.robot.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RandomNameRule implements NameRule
{
	private final static Random m_RandomBuilder = new Random();

	/* (non-Javadoc)
	 * @see test.robot.utility.NameRule#GetName(test.robot.Robot)
	 */
	@Override
	public String GetName(Robot r)
	{
		return RobotConfig.RobotName + m_RandomBuilder.nextInt();
	}

}
