/**
 * MethodParamLong.java 2012-6-27上午10:47:46
 */
package core.detail.impl.methodparam;

import core.detail.impl.socket.MsgBuffer;
import core.detail.interface_.MethodParam;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodParamLong extends MethodParamBase
{
	private long m_Value;
	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#Clone()
	 */
	@Override
	public MethodParam Clone()
	{
		return new MethodParamLong();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.methodparam.MethodParamBase#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer) throws Exception
	{
		m_Value = buffer.GetLong();
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
