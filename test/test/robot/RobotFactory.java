/**
 * RobotFactory.java 2012-7-12上午10:22:54
 */
package test.robot;

import test.robot.module.chat.ChatRtnImple;
import test.robot.module.login.*;
import test.robot.module.task.*;
import core.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RobotFactory implements Factory
{

	/* (non-Javadoc)
	 * @see core.Factory#NewUser()
	 */
	@Override
	public User NewUser()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see core.Factory#OnRegAllCallBack()
	 */
	@Override
	public void OnRegAllCallBack()
	{
		Root r = Root.GetInstance();
		
		try
		{
			r.Reg(new LoginRtn());
			r.Reg(new TaskRtn());
			r.Reg(new ChatRtnImple());
		}
		catch(Exception e)
		{
			System.out.println("### 注册失败!!!");
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see core.Factory#OnServerCloseCallBack(int)
	 */
	@Override
	public void OnServerCloseCallBack(int second)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see core.Factory#OnLinkProcessMsgTooMuch(core.User)
	 */
	@Override
	public void OnLinkProcessMsgTooMuch(User p_User) throws Exception
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see core.Factory#OnUserNumChange(int, int)
	 */
	@Override
	public void OnUserNumChange(int currnum, int maxnum)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see core.Factory#OnLinkNumChange(int, int)
	 */
	@Override
	public void OnLinkNumChange(int currnum, int maxnum)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see core.Factory#NeedSendEchoMsg(core.User)
	 */
	@Override
	public void NeedSendEchoMsg(User p_User)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see core.Factory#GetEncryptBuffer()
	 */
	@Override
	public byte[] GetEncryptBuffer()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
