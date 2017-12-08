/**
 * MethodParamFloat.java 2012-6-27上午10:48:34
 */
package core.detail.impl.methodparam;

import core.detail.impl.socket.MsgBuffer;
import core.detail.interface_.MethodParam;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodParamFloat extends MethodParamBase
{
	private float m_Value;
	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#Clone()
	 */
	@Override
	public MethodParam Clone()
	{
		return new MethodParamFloat();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer) throws Exception
	{
		m_Value = buffer.GetFloat();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#Get()
	 */
	@Override
	public Object Get()
	{
		return m_Value;
	}

}
