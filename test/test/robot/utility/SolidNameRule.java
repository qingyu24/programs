/**
 * SolidNameRule.java 2012-10-15上午10:27:47
 */
package test.robot.utility;

import test.robot.Robot;
import test.robot.RobotConfig;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SolidNameRule implements NameRule
{
	private String m_Name = RobotConfig.RobotName;
	
	public void SetName(String name)
	{
		m_Name = name;
	}
	
	/* (non-Javadoc)
	 * @see test.robot.utility.NameRule#GetName(test.robot.Robot)
	 */
	@Override
	public String GetName(Robot r)
	{
		return m_Name;
	}

}
