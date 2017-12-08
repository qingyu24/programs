package logic.module.log;

/**
 * @author Hey
 *	用于返回日志记录所需要的数据结构
 */
public class DataForLog
{
	/**
	 * @author Hey
	 * 属性改变结果(用于记录日志)
	 */
	public static class DataForLog_AttributeChange
	{
		public int Type;			// 属性类型(金币：AttrStatic.ATTR_TYPE_MONEY)
		public String ValueName;	// 属性名称
		public int ChangeValue;		// 属性数值改变量
		public int OldValue;		// 更改前的属性数值
		public int CurrValue;		// 更改后(当前)的属性数值
		public int From;			// 属性改变类型(哪个系统模块/操作导致的属性改变，用于日志记录)
	}
	
	/**
	 * @author Hey
	 *	Vip数据改变结果(用于记录日志)
	 */
	public static class DataForLog_VipChange
	{
		public int OldVipLv;		// 更改前的Vip等级
		public int OldDiamond;		// 更改前的充值钻石
		public int AddDiamond;		// 增加充值钻石数量
		public int CurrVipLv;		// 当前Vip等级
		public int CurrDiamond;		// 当前充值钻石数量
	}
}