package core.db;

import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBLong;
import core.db.DBString;
import core.db.RoleDataBase;

public class GlobalData extends RoleDataBase 
{
	public DBLong RoleID;
	public DBInt	TemplateID;
	public DBString NickName;
	public DBInt	RoleLV;
	public DBDateTime LoginTime;
	public DBInt	TempVipLevel;
	public DBInt	VipLevel;
	public DBInt	ServerID;
	
	public void RefreshLoginTime()
	{
		LoginTime.Set(System.currentTimeMillis());
	}

	public int GetVipLevel()
	{
		int temp = TempVipLevel.Get();
		int level = VipLevel.Get();
		return (temp > level ? temp : level);
	}
}
