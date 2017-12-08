/**
 * VipData.java 2013-7-5上午11:57:24
 */
package mergedb.db;

import core.db.DBDateTime;
import core.db.DBInt;
import core.db.DBLong;
import core.db.RoleDataBase;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class VipData extends RoleDataBase
{
	public DBLong		RoleID;
	public DBInt		TempVipLevel;			// 使用vip卡得到的临时vip等级
	public DBDateTime	StartTm;				// 开始时间（单位：毫秒）
	public DBInt		LastTm;					// vip的持续时间（单位：分钟）
	public DBInt		VipLevel;				// 对应钻石的vip等级
	public DBInt		Diamond;				// 充值钻石,这是用来折算为永久的vip等级
	public DBLong		WorldBossGID;
	public DBInt		WorldBossAddAttackNum;
	public DBInt		WorldBossCDNum;
	public DBLong		DFWZGID;
	public DBInt		DFWZAddAttackNum;
	public DBDateTime	BuyStaminaTime;
	public DBInt		BuyStaminaNum;
	public DBLong		PVPGID;
	public DBInt		PVPAddAttackNum;
	public DBInt		PVPBirthNum;
	public DBInt		FactionBossAddAttackNum;
	public DBInt		FactionBossCDNum;
}
