/**
 * Bind.java 2013-4-20下午7:30:00
 */
package core.ex.bind;

import java.lang.reflect.Method;
import java.text.Format;
import java.text.NumberFormat;

import utility.Debug;
import core.detail.impl.log.Log;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Bind
{
	private String m_value = "";
	private Method m_run;
	private Object m_obj;
	
	public Bind(String bindname, Object bindobj)
	{
		Method[] ms = bindobj.getClass().getMethods();
		for ( Method m : ms )
		{
			if ( m.getName().equalsIgnoreCase("get" + bindname) )
			{
				try
				{
					m_obj = m.invoke(bindobj, (Object[])null);
					Method[] ms1 = m_obj.getClass().getMethods();
					for ( Method m1 : ms1 )
					{
						if ( m1.getName() == "setText" )
						{
							m_run = m1;
							break;
						}
					}
					Debug.Assert(m_run != null, "");
					return;
				}
				catch (Exception e)
				{
					Log.out.LogException(e);
				}
			}
		}
	}
	
	public void SetString(String s)
	{
		m_value = s;
		_Set();
	}
	
	private void _Set()
	{
		if ( m_run != null )
		{
			try
			{
				m_run.invoke(m_obj, m_value);
			}
			catch (Exception e)
			{
				Log.out.LogException(e);
			}
		}
	}
	
	protected void SetFloat(float f, Format f1)
	{
		m_value = f1.format(f);
		_Set();
	}
	
	protected void SetFloatFormat(float f)
	{
		SetFloat(f, NumberFormat.getNumberInstance());
	}
	
	protected void SetInt(int v, Format f)
	{
		m_value = f.format(v);
		_Set();
	}
	
	protected void SetIntNumForamt(int v)
	{
		SetInt(v, NumberFormat.getNumberInstance());
	}
	
	protected void SetLong(long v, Format f)
	{
		m_value = f.format(v);
		_Set();
	}
	
	protected void SetLongNumForamt(long v)
	{
		SetLong(v, NumberFormat.getNumberInstance());
	}

	protected void SetUseTime(long usetm)
	{
		m_value = Debug.GetUseTime(usetm);
		_Set();
	}
}
