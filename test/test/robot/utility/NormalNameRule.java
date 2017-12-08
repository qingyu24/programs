/**
 * NormalNameRule.java 2012-10-15上午10:27:13
 */
package test.robot.utility;

import test.robot.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class NormalNameRule implements NameRule
{

	/* (non-Javadoc)
	 * @see test.robot.utility.NameRule#GetName(test.robot.Robot)
	 */
	@Override
	public String GetName(Robot r)
	{
		return RobotConfig.RobotName + r.GetID();
	}

}
