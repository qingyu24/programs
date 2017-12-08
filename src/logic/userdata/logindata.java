package logic.userdata;
import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBLong;
import core.db.DBString;
import core.db.RoleDataBase;
import core.detail.impl.socket.SendMsgBuffer;
/**
*@author niuhao
*@version 0.0.1
*@create by en_mysql_to_class.py
*@time:Jul-26-17 10:00:45
**/
public class logindata extends RoleDataBase
{
	public DBString UserName;//

	public DBLong UserID;//

	public DBString Password;//

	public DBDateTime LastLoginTime;//

	public DBInt Forbid;//

	public DBInt ServerID;//

	public DBInt ChannelID;//

	public void packData(SendMsgBuffer buffer){
		buffer.Add(UserName.Get());
		buffer.Add(UserID.Get());
		buffer.Add(Password.Get());
		buffer.Add(LastLoginTime.GetMillis());
		buffer.Add(Forbid.Get());
		buffer.Add(ServerID.Get());
		buffer.Add(ChannelID.Get());
	}
}
