/**
 * MethodParamArrayList.java 2012-7-12下午2:57:21
 */
package core.detail.impl.methodparam;

import java.io.*;
import java.util.*;

import utility.*;

import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodParamArrayList extends MethodParamBase
{
	@SuppressWarnings("rawtypes")
	private ArrayList m_Value = new ArrayList();
	private Class<?> m_Class;
	
	public MethodParamArrayList(Class<?> c)
	{
		m_Class = c;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#Clone()
	 */
	@Override
	public MethodParam Clone()
	{
		return new MethodParamArrayList(m_Class);
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ParseMsg(MsgBuffer buffer) throws Exception
	{
		short sz = buffer.GetShort();
		for ( int i = 0; i < sz; ++i )
		{
			Object o = _GetObject(m_Class, buffer);
			m_Value.add(o);
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#Get()
	 */
	@Override
	public Object Get()
	{
		return m_Value;
	}

	private Object _GetObject(Class<?> type, MsgBuffer buffer) throws UnsupportedEncodingException
	{
		if ( type == Boolean.class )
		{
			return buffer.GetBoolean();
		}
		if ( type == Byte.class )
		{
			return buffer.GetByte();
		}
		if ( type == Double.class )
		{
			return buffer.GetDouble();
		}
		if ( type == Float.class )
		{
			return buffer.GetFloat();
		}
		if ( type == Integer.class )
		{
			return buffer.GetInt();
		}
		if ( type == Long.class )
		{
			return buffer.GetLong();
		}
		if ( type == String.class )
		{
			return buffer.GetString();
		}
		if ( type == Short.class )
		{
			return buffer.GetShort();
		}
		Debug.Assert(false, "不支持的类型[" + type + "]");
		return null;
	}
}
