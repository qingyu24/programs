package logic.module.log.sql;

import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBLong;

public class OperationLog {
	public DBInt		ServerID;			//服务器ID
	public DBDateTime	Tm;					//时间
	public DBLong		RoleID;				//角色ID
	public DBInt		OperationID;		//操作ID
	public DBInt        ItemID;             //物品id或者副本id
}


//p_user.Log(eLogicSQLLogType.LOGIC_SQL_Item_Opt,
//		RootConfig.GetInstance().ServerUniqueID,
//		eLogicSQLLogType.GetCurrTime(),
//		p_user.GetRoleGID(),
//		nSlot,
//		p_nItemCID,
//		4,
//		from.ID(),
//		extraInfo
//		);
