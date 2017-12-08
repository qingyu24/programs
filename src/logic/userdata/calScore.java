package logic.userdata;

import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBLong;
import core.db.RoleDataBase;
import core.detail.impl.socket.SendMsgBuffer;

/**
 * The persistent class for the calscore database table.
 * 
 */

public class calScore extends RoleDataBase {
   
	public DBInt GID;
	public DBLong RoleID;
	public DBInt beginScore;
	public DBDateTime beginTime;
	public DBInt endScore;
	public DBDateTime endtime;
	public DBInt changeScore;
	public DBLong winnerId;
	public DBLong roomUser1;
	public DBLong roomUser2;
	public DBLong roomUser3;

	public void packData(SendMsgBuffer buffer) {
		buffer.Add(GID.Get());
		buffer.Add(RoleID.Get());
		buffer.Add(beginScore.Get());
		buffer.Add(beginTime.GetMillis());
		buffer.Add(endScore.Get());
		buffer.Add(endtime.GetMillis());
		buffer.Add(changeScore.Get());
		buffer.Add(winnerId.Get());
		buffer.Add(roomUser1.Get());
		buffer.Add(roomUser2.Get());
		buffer.Add(roomUser3.Get());

	}
	
	

}