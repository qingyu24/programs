/**
 * MethodParamArrayListCustom.java 2012-7-12下午3:36:27
 */
package core.detail.impl.methodparam;

import java.util.ArrayList;

import core.SerialData;
import core.detail.impl.socket.MsgBuffer;
import core.detail.interface_.MethodParam;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodParamArrayListCustom extends MethodParamBase
{
	private ArrayList<SerialData> m_All = new ArrayList<SerialData>();
	private Class<?> m_Type;
	
	public MethodParamArrayListCustom(Class<?> type)
	{
		m_Type = type;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#Clone()
	 */
	@Override
	public MethodParam Clone()
	{
		return new MethodParamArrayListCustom(m_Type);
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer) throws Exception
	{
		short sz = buffer.GetShort();
		for ( int i = 0; i < sz; ++i )
		{
			SerialData d = (SerialData) m_Type.newInstance();
			d.DeSerialData(buffer);
			m_All.add(d);
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#Get()
	 */
	@Override
	public Object Get()
	{
		return m_All;
	}

}
