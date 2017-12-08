package logic.userdata;
import core.db.DBInt;
import core.db.DBLong;
import core.db.RoleDataBase;
import core.detail.impl.socket.SendMsgBuffer;
/**
*@author niuhao
*@version 0.0.1
*@create by en_mysql_to_class.py
*@time:Jul-19-17 11:10:18
**/
public class ranking extends RoleDataBase
{
	public DBLong GID;//

	public DBLong RoleID;//

	public DBInt WinCount;//

	public void packData(SendMsgBuffer buffer){
		buffer.Add(GID.Get());
		buffer.Add(RoleID.Get());
		buffer.Add(WinCount.Get());
	}
}
