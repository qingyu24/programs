package logic.module.log.sql;

import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBLong;

public class LoadDataLog 
{
	public DBInt			ServerID;			//
	public DBDateTime		Tm;					//
	public DBLong			RoleID;				//
	public DBInt			LoadDataOpt;		//0:开始加载,1:加载完成
}
