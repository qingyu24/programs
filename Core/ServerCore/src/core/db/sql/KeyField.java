/**
 * BindKeyField.java 2012-8-28下午3:54:28
 */
package core.db.sql;

import core.db.*;
import core.db.mysql.*;
import core.detail.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 对于引用的结构来说，根据绑定的key来确定是否指向的数据
 * 
 * 结构1
 * GID  RoleID Value1
 * 1	2		10
 * 2	1		11
 * 
 * 结构2
 * GID	RoleID BindValue1
 * 1	1		？
 * 
 * 结构2的BindValue1指向结构1的Value1，当使用GID绑定时，BindValue1为10，当使用RoleID绑定时，BindVvalue1为11
 */
public class KeyField
{
	public final eFieldKey key;
	private Object m_Value;
	
	public KeyField(eFieldKey key)
	{
		this.key = key;
	}

	/**
	 * 取出数据
	 */
	public void FetchValue(String fieldName, DBValue fieldValue)
	{
		if ( m_Value != null )
		{
			return;
		}
		if ( !fieldName.equals(key.GetName()) )
		{
			return;
		}
		m_Value = SystemFn.GetValueFromDBValue(fieldValue);
	}

	/**
	 * 是否对数据初始化成功了
	 * 
	 * @return 成功初始化了返回true
	 */
	public boolean IsInitValue()
	{
		return m_Value != null;
	}
	
	public String toString()
	{
		return super.toString() + " Field[" + key + "] Value[" + m_Value + "]";
	}

	/**
	 * 获取绑定的值
	 */
	public Object GetValue()
	{
		return m_Value;
	}

	/**
	 * 重试值
	 */
	public void ResetValue()
	{
		m_Value = null;
	}
}
