/**
 * MySQLOper.java 2012-6-28下午5:17:44
 */
package core.db.mysql.oper;

import java.lang.reflect.*;

import utility.*;

import core.db.*;
import core.detail.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public abstract class MySQLOper
{
	protected String 	m_sKeyName;
	private   long		m_LastUseTime;
	private static final int m_WaitTime = 10 * 1000 * 60;
	
	public MySQLOper(String key, Class<?> c)
	{
		m_sKeyName = key;
		Use();
		
		if ( c == null )
		{
			return;
		}
		
		if ( !m_sKeyName.equals("*") )
		{
			Debug.Assert( SystemFn.IsHaveField(c, m_sKeyName), "" );
		}
		
		for ( Field f : c.getFields() )
		{
			Debug.Assert( SystemFn.IsHaveInterface(f.getType(), DBValue.class), "字段[" + f.getName() + "]的类型必定实现了DBValue的接口" );
		}
	}
	
	protected void Use()
	{
		m_LastUseTime = System.currentTimeMillis();
	}
	
	public long GetLastUseTime()
	{
		return m_LastUseTime;
	}
	
	public boolean IsCanRemove()
	{
		return System.currentTimeMillis() - m_LastUseTime > m_WaitTime;
	}

	public abstract void Release();
}
