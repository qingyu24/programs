/**
 * Faction.java 2013-7-8下午7:09:56
 */
package modifyroleid.db;

import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBLong;
import core.db.DBString;
import core.db.DBUniqueLong;
import core.db.RoleDataBase;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Faction extends RoleDataBase
{
	public DBUniqueLong 	GID;			///<唯一id
	public DBString			Master;			///<帮主
	public DBString			Name;			///<帮派名称
	public DBInt			Exp;			///<帮派经验
	public DBLong			QQGroup;		///<帮派QQ群号
	public DBString			Notice;			///<帮派公告
	public DBString			Des;			///<帮派描述
	public DBInt			GodLevel;		///<女神等级
	public DBInt			GodExp;			///<女神经验
	public DBLong			DemonMem1;		///<除魔成员1 !!!!!!!!!!!!!!!!废弃
	public DBLong			DemonMem2;		///<除魔成员2 !!!!!!!!!!!!!!!!废弃
	public DBLong			DemonMem3;		///<除魔成员3 !!!!!!!!!!!!!!!!废弃
	public DBLong			DemonMem4;		///<除魔成员4 !!!!!!!!!!!!!!!!废弃
	public DBLong			DemonMem5;		///<除魔成员5 !!!!!!!!!!!!!!!!废弃
	public DBLong			DemonMem6;		///<除魔成员6 !!!!!!!!!!!!!!!!废弃
	public DBLong			DemonMem7;		///<除魔成员7 !!!!!!!!!!!!!!!!废弃
	public DBInt			IsDeleted;		///<删除标准
	public DBDateTime		DutyResetTime;	///<职位刷新时间
}