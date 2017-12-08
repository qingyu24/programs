package logic.module.log.sql;

import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBLong;
import core.db.DBString;

public class RegisterLog
{
	public DBInt			ServerID;
	public DBDateTime		Tm;
	public DBLong 			UserID;
	public DBString			UserName;
	public DBString			IP;
	public DBString			Device;
	public DBString			DeviceModel;
	public DBInt			Type;

}
