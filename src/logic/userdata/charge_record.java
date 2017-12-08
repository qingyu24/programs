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
public class charge_record extends RoleDataBase
{
	public DBInt GID;//

	public DBLong RoleID;//

	public DBLong TargetRoleID;//

	public DBInt card;//

	public DBDateTime DateTime;//

	public DBString Name;//

	public void packData(SendMsgBuffer buffer){
		buffer.Add(GID.Get());
		buffer.Add(RoleID.Get());
		buffer.Add(TargetRoleID.Get());
		buffer.Add(card.Get());
		buffer.Add(DateTime.GetMillis());
		buffer.Add(Name.Get());
	}
}
