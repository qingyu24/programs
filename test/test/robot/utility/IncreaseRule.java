/**
 * IncreaseRule.java 2013-2-4上午11:41:59
 */
package test.robot.utility;

import test.robot.*;
import test.robot.RobotConfig;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class IncreaseRule implements NameRule
{
	private String m_Name = RobotConfig.RobotName;
	private int m_Index = 0;
	
	/* (non-Javadoc)
	 * @see test.robot.utility.NameRule#GetName(test.robot.Robot)
	 */
	@Override
	public String GetName(Robot r)
	{
		m_Index++;
		return m_Name + m_Index;
	}
}
