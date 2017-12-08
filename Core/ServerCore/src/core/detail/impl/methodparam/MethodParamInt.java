/**
 * MethodParamInt.java 2012-6-27上午10:39:36
 */
package core.detail.impl.methodparam;

import core.detail.impl.socket.MsgBuffer;
import core.detail.interface_.MethodParam;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodParamInt extends MethodParamBase
{
	private int m_Value;
	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#Clone()
	 */
	@Override
	public MethodParam Clone()
	{
		return new MethodParamInt();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer)
	{
		m_Value = buffer.GetInt();
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
