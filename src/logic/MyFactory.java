/**
 * MyFactory.java 2012-6-11下午9:47:15
 */
package logic;

import core.Factory;
import core.Root;
import core.User;
import core.detail.impl.log.Log;
import core.exception.RegException;
import logic.module.character.CharacterImpl;
import logic.module.login.Login;
import logic.module.proxy.ProxyImpl;
import logic.module.room.RoomImpl;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 功能配置
 */
public class MyFactory implements Factory
{
	public MyFactory()
	{
	}
	/* (non-Javadoc)
	 * @see Core.Factory#GetUser()
	 */
	@Override
	public User NewUser()
	{
		return new MyUser();
	}

	/* (non-Javadoc)
	 * @see Core.Factory#OnRegAllCallBack()
	 */
	@Override
	public void OnRegAllCallBack()
	{
		Root r = Root.GetInstance();
		
		try
		{
			r.Reg(Login.GetInstance());
			r.Reg(new CharacterImpl());
			r.Reg(new RoomImpl());
			r.Reg(new ProxyImpl());			
			//todo;
			
		}
		catch(RegException e)
		{
			System.out.println("### 注册失败!!!");
			Log.out.LogException(e);
		}
	}
	/* (non-Javadoc)
	 * @see core.Factory#OnServerCloseCallBack(int)
	 */
	@Override
	public void OnServerCloseCallBack(int second)
	{
		if ((second > 10 && second % 10 == 0) 
			|| (second <= 10 && second % 2 == 0))
		{
			String notice = ""+second+"秒后关服,请玩家主动下线,以避免不必要的损失!!!";
			//MyUser.TopNoticeToAll(eNoticeType.UNDEFINE, notice);
		}
	}
	/* (non-Javadoc)
	 * @see core.Factory#OnLinkProcessMsgTooMuch(core.User)
	 */
	@Override
	public void OnLinkProcessMsgTooMuch(User p_User) throws Exception
	{
		//Login.GetInstance().OnLinkProcessMsgTooMuch(p_User);
	}
	/* (non-Javadoc)
	 * @see core.Factory#OnUserNumChange(int, int)
	 */
	@Override
	public void OnUserNumChange(int currnum, int maxnum)
	{
		//Login.GetInstance().OnUserNumChange(currnum, maxnum);
	}
	/* (non-Javadoc)
	 * @see core.Factory#OnLinkNumChange(int, int)
	 */
	@Override
	public void OnLinkNumChange(int currnum, int maxnum)
	{
		//Login.GetInstance().OnLinkNumChange(currnum, maxnum);
	}
	/* (non-Javadoc)
	 * @see core.Factory#NeedSendEchoMsg(core.User)
	 */
	@Override
	public void NeedSendEchoMsg(User p_User)
	{
		//Echo.GetInstance().AskClientEchoMsg((MyUser)p_User);
	}
	/* (non-Javadoc)
	 * @see core.Factory#GetEncryptBuffer()
	 */
	@Override
	public byte[] GetEncryptBuffer()
	{
//		StringBuilder sb = new StringBuilder();
//		for (TaskTemplateData c : TaskConfig.GetAllConfig())
//		{
//			sb.append(c.ReceiveTaskSpeek);
//		}
//		return sb.toString().getBytes();
		return SecurityBuffer.m_Buffer;
	}
}
