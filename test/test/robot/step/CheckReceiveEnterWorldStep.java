/**
 * CheckReceiveEnterWorldStep.java 2012-8-1下午3:42:14
 */
package test.robot.step;

import test.robot.Robot;
import test.robot.value.RoleRes;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class CheckReceiveEnterWorldStep extends Step
{

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		return new CheckReceiveEnterWorldStep();
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Execute(test.robot.Robot)
	 */
	@Override
	public boolean Execute(Robot r)
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Executed(test.robot.Robot)
	 */
	@Override
	public boolean Executed(Robot r) throws Exception
	{
		return r.m_LoginData.m_UseRoleRes.IsSet();
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Result(test.robot.Robot)
	 */
	@Override
	public boolean Result(Robot r) throws InterruptedException
	{
		RoleRes res = r.m_LoginData.m_UseRoleRes;
		return res.RoleTemplateID() > 0 && res.RoleID() > 0;
	}

}
