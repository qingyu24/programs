/**
 * MethodParamString.java 2012-6-13下午10:04:03
 */
package core.detail.impl.methodparam;

import java.io.UnsupportedEncodingException;

import core.detail.impl.socket.MsgBuffer;
import core.detail.interface_.MethodParam;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodParamString extends MethodParamBase
{
	private String m_String;
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#Clone()
	 */
	@Override
	public MethodParam Clone()
	{
		return new MethodParamString();
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public void ParseMsg(MsgBuffer buffer) throws UnsupportedEncodingException
	{
		m_String = buffer.GetString();
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#Get()
	 */
	@Override
	public Object Get()
	{
		return m_String;
	}
}
