package test.robot.module.chat;

import test.robot.Robot;

public class ChatRtnImple implements ChatRtnInterface {

	@Override
	public void ChatRes(Robot r, short channel, String res)
	{
		// TODO Auto-generated method stub
		r.m_ChatData.SetInfo(channel, res);
		//System.out.println(r+"收到:" + " value:"+ res);
	}

}
