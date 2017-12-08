/**
 * MethodExGMImpl.java 2013-1-24下午2:16:34
 */
package core.detail.impl;

import java.lang.reflect.Method;

import core.eCloseReasonType;
import core.detail.GMUser;
import core.detail.interface_.MethodParam;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MethodExGMImpl extends MethodExImpl
{
	private int m_NeedLevel = 10000;

	public MethodExGMImpl(Object call, Method m, MethodParam[] p_params, int classid, int methodid, boolean p_runwithoutdataready, int needlv)
	{
		super(call, m, p_params, classid, methodid, p_runwithoutdataready);
		m_NeedLevel = needlv;
	}

	protected boolean _Check()
	{
		GMUser u = (GMUser)GetUser();
		if ( u.GetGMLevel() < m_NeedLevel )
		{
			u.Close(eCloseReasonType.GM_LEVEL.ID(), 0);
			return false;
		}
		else
		{
			return true;
		}
	}
}
