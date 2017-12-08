package logic.userdata;

import java.util.ArrayList;

import core.detail.impl.socket.SendMsgBuffer;
import logic.module.room.CardInfo;
import logic.module.room.RoomRule;
import logic.module.room.RoomUser;
import logic.module.room.TestUser;

public abstract class IDeskBalance<T> {

	public RoomUser m_user;
	public int genzhuang; //跟庄;
	
	public void packData(SendMsgBuffer buffer){}
	
	public void reset(){}
	
	public int calScore(RoomUser winner)
	{
		return 0;
	}
	
	public void processHu(CardInfo card, int size, ArrayList<RoomUser> users, RoomUser winner){}
	
	public void processGang(CardInfo card,ArrayList<RoomUser> users, RoomUser winner){}
	
	public void sumHu(boolean _hasHui, int _qd, int _zimo, int _ytl, int _qys, int _qysytl, int _hy, int _piao,
			ArrayList<TestUser> users, RoomRule rr, long dianpa) {
	}

	public void logScore() {};
	
}
