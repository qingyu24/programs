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
public class account extends RoleDataBase
{
	public DBLong RoleID;//用户id

	public DBString Password;//

	public DBInt TID;//

	public DBString openid;//微信传过来的openid

	public DBString nickName;//

	public DBString headIcon;//头像图片地址

	public DBInt roomCard;//房卡

	public DBString unionid;//

	public DBString province;//省份

	public DBString city;//城市

	public DBInt sex;//性别

	public DBInt prizecount;//

	public DBInt winCount;//

	public DBInt actualCard;//

	public DBInt totalCard;//

	public DBDateTime createTime;//

	public DBDateTime lastLoginTime;//

	public DBString status;//

	public DBString isGame;//

	public void packData(SendMsgBuffer buffer){
		buffer.Add(RoleID.Get());
		buffer.Add(Password.Get());
		buffer.Add(TID.Get());
		buffer.Add(openid.Get());
		buffer.Add(nickName.Get());
		buffer.Add(headIcon.Get());
		buffer.Add(roomCard.Get());
		buffer.Add(unionid.Get());
		buffer.Add(province.Get());
		buffer.Add(city.Get());
		buffer.Add(sex.Get());
		buffer.Add(prizecount.Get());
		buffer.Add(winCount.Get());
		buffer.Add(actualCard.Get());
		buffer.Add(totalCard.Get());
		buffer.Add(createTime.GetMillis());
		buffer.Add(lastLoginTime.GetMillis());
		buffer.Add(status.Get());
		buffer.Add(isGame.Get());
	}
}
