/**
 * AutoCreateRoleStep.java 2012-7-31下午5:17:49
 */
package test.robot.step;

import logic.config.*;
import test.robot.*;
import test.robot.value.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class AutoCreateRoleStep extends Step
{
	private boolean m_Execute = false;
	private int m_RoleTemplate = 0;
	private boolean m_CheckCreateRole = false;
	
	public AutoCreateRoleStep()
	{
		m_RoleTemplate = RoleConfig.GetRandomRoleTemplateID();
	}
	
	/* (non-Javadoc)
	 * @see test.robot.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		return new AutoCreateRoleStep();
	}

	/* (non-Javadoc)
	 * @see test.robot.Step#Execute(test.robot.Robot)
	 */
	@Override
	public boolean Execute(Robot r) throws InterruptedException
	{
		Res<Integer> r1 = r.m_LoginData.m_LoginRes;
		if ( !r1.IsSet() )
		{
			//如果没有登陆,永远不执行创建角色
			return false;
		}
		RoleRes r2 = r.m_LoginData.m_UseRoleRes;
		if ( r2.IsSet() )
		{
			//如果有进入世界的反馈,那就不用执行
			m_Execute = true;
			return true;
		}
		
		RoleRes r3 = r.m_LoginData.m_NoRleRes;
		if ( !r3.IsSet() )
		{
			//服务器还没发送空的角色消息,等待
			return false;
		}
		
		if ( r3.RoleID() == 0 )
		{
			if ( !m_Execute )
			{
				m_Execute = true;
				m_CheckCreateRole = true;
				RFCFn.Login_Create(r, r.GetCreateRoleName(), m_RoleTemplate);
			}
		}
		return m_Execute;
	}

	/* (non-Javadoc)
	 * @see test.robot.Step#Executed(test.robot.Robot)
	 */
	@Override
	public boolean Executed(Robot r) throws Exception
	{
		if ( !m_Execute )
		{
			return false;
		}
		
		if ( m_CheckCreateRole )
		{
			return r.m_LoginData.m_CreateRoleRes.IsSet();
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see test.robot.Step#Result(test.robot.Robot)
	 */
	@Override
	public boolean Result(Robot r) throws InterruptedException
	{
		if ( m_CheckCreateRole )
		{
			Res<Boolean> res = r.m_LoginData.m_CreateRoleRes;
			return res.Result();
		}
		return true;
	}

}
