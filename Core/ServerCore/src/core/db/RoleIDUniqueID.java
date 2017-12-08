/**
 * RoleIDUniqueID.java 2013-5-2下午10:42:05
 */
package core.db;

import core.RootConfig;
import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;
import utility.Debug;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RoleIDUniqueID
{
	private int m_ServerID = -1;
	private long m_Value = -1;
	public static final int SPACENUM = 10000;
	
	public RoleIDUniqueID()
	{
		m_ServerID = RootConfig.GetInstance().ServerUniqueID;
	}
	
	public void SetBaseValue(long baseid)
	{
		if (baseid == 0)
		{
			m_Value = m_ServerID;
			m_Value = m_Value << (64 - 12);
		}
		else
		{
			m_Value = baseid + SPACENUM;
		}
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_READ_CONFIG_FILE, "用[" + baseid + "初始化角色生成器,下一个生成的值为:" + m_Value);
	}
	
	public long Get()
	{
		Debug.Assert(m_Value != -1, "必须先初始化值m_ServerID");
		long v = m_Value;
		m_Value += SPACENUM;
		return v;
	}
}
