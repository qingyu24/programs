/**
 * RobotRich.java 2012-7-11上午10:03:16
 */
package test.robot;

import core.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RobotRoot extends Root
{
	public RobotRoot()
	{
		super();
		m_Factory = new RobotFactory();
		
		RegAll();
	}
}
