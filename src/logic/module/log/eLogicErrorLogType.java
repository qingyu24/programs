/**
 * eLogicErrorLogType.java 2012-10-23下午4:04:20
 */
package logic.module.log;

import core.LogType;
import utility.Debug;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eLogicErrorLogType implements LogType
{
	LOGIC_ERROR_WEAHTER_DATA_ERROR,
	LOGIC_ERROR_WEATHER_READ_CONFIG,
	LOGIC_ERROR_TRANSFER_CONFIG,
	LOGIC_ERROR_TRANSFER_CONFIG_ITEM,
	LOGIC_ERROR_TRANSFER_NONE_ROLEID,
	LOGIC_ERROR_TRANSFER_NO_LEVELUP,
	LOGIC_ERROR_TRANSFER_DELITEM,
	LOGIC_ERROR_TRANSFER_LEVER,
	
	LOGIC_ERROR_BUILD_ERROR_ROLEID,
	LOGIC_ERROR_BUILD_ERROR_GID,
	LOGIC_ERROR_BUILD_ERROR_CID,
	LOGIC_ERROR_BUILD_CONFIG,
	LOGIC_ERROR_BUILD_ITEM_CONFIG,
	LOGIC_ERROR_BUILD_ROLE_ID,
	LOGIC_ERROR_BUILD_QUALITY,
	LOGIC_ERROR_BUILD_NOTICKET,
	;

	/* (non-Javadoc)
	 * @see core.LogType#Serialize()
	 */
	@Override
	public String Serialize()
	{
		switch ( this )
		{
		case LOGIC_ERROR_WEAHTER_DATA_ERROR:	return "用户的天气数据解析时出错(%s)";
		case LOGIC_ERROR_WEATHER_READ_CONFIG:	return "读取天气配置出错,无法找到id(%d)";
		case LOGIC_ERROR_TRANSFER_CONFIG:		return "在(%s) 转职配置出错,无法找到id(%d)";
		case LOGIC_ERROR_TRANSFER_CONFIG_ITEM:	return "读取转职配置物品数据时,索引(%d)越界";
		case LOGIC_ERROR_TRANSFER_NONE_ROLEID:	return "使用没有的角色id(%d)进行转职";
		case LOGIC_ERROR_TRANSFER_NO_LEVELUP:	return "转职已经到头了,不能再转职了(%d)";
		case LOGIC_ERROR_TRANSFER_DELITEM:		return "有足够的物品(%d) (%d)个,但是扣除(%d)失败";
		case LOGIC_ERROR_TRANSFER_LEVER:		return "不能对离队的角色进行转职(%d)";
		
		case LOGIC_ERROR_BUILD_ERROR_ROLEID:	return "没有找到要打造物品对应的角色id(%d)";
		case LOGIC_ERROR_BUILD_ERROR_GID:		return "无法找到要打造物品的GID 参数:(%d)(%d)";
		case LOGIC_ERROR_BUILD_ERROR_CID:		return "实际物品的模板id(%d) 发送的模板id(%d) 不同";
		case LOGIC_ERROR_BUILD_CONFIG:			return "没有找到物品(%d)对应的打造配置表";
		case LOGIC_ERROR_BUILD_ITEM_CONFIG:		return "没有找到物品(%d)对应的配置表";
		case LOGIC_ERROR_BUILD_ROLE_ID:			return "打造装备时从配置表中无法找到角色模板id(%d)";
		case LOGIC_ERROR_BUILD_QUALITY:			return "打造装备的等级(%d)太高,超过了角色的等级(%d),无法打造";
		case LOGIC_ERROR_BUILD_NOTICKET:		return "装备(%d)转职打造缺少打造券(%d),无法打造";
		default: Debug.Assert(false , "未执行的类型:" + this);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see core.LogType#SystemLog()
	 */
	@Override
	public boolean SystemLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#LogicLog()
	 */
	@Override
	public boolean LogicLog()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see core.LogType#DebugLog()
	 */
	@Override
	public boolean DebugLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#InfoLog()
	 */
	@Override
	public boolean InfoLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#WarningLog()
	 */
	@Override
	public boolean WarningLog()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.LogType#ErrorLog()
	 */
	@Override
	public boolean ErrorLog()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see core.LogType#SQLLog()
	 */
	@Override
	public boolean SQLLog()
	{
		return false;
	}

}
