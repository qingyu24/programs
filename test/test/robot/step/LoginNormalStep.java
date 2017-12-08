/**
 * LoginNormalStep.java 2012-7-31下午12:29:58
 */
package test.robot.step;

import test.robot.*;
import test.robot.value.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 走正常登陆流程的步骤
 */
public class LoginNormalStep extends Step
{
	private boolean m_Execute = false;
	
	public LoginNormalStep()
	{
	}
	
	/* (non-Javadoc)
	 * @see test.robot.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		return new LoginNormalStep();
	}
	
	/* (non-Javadoc)
	 * @see test.robot.Step#Execute(test.robot.Robot)
	 */
	@Override
	public boolean Execute(Robot r)
	{
		if ( !m_Execute )
		{
			m_Execute = true;
			RFCFn.Login_Enter(r, r.GetLoginName(), "123", 1, 3);
		}
		return m_Execute;
	}

	/* (non-Javadoc)
	 * @see test.robot.Step#Executed(test.robot.Robot)
	 */
	@Override
	public boolean Executed(Robot r) throws Exception
	{
		Res<Integer> res = r.m_LoginData.m_LoginRes;
		if ( res.IsSet() )
		{
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see test.robot.Step#Result()
	 */
	@Override
	public boolean Result(Robot r) throws InterruptedException
	{
		return r.m_LoginData.m_LoginRes.Result() == 1;
	}
}
