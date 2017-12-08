/**
 * MeghodMgrImpl.java 2012-6-11下午11:08:49
 */
package core.detail.impl.mgr.methodmgr;

import java.lang.reflect.*;

import utility.*;
import utility.RFC.*;

import core.detail.impl.*;
import core.detail.interface_.*;
import core.exception.*;
import core.remote.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 正常的网络函数
 */
public class MethodMgrNormalImpl extends MethodMgrBase
{
	private static MethodMgrNormalImpl m_Instance = new MethodMgrNormalImpl();
	
	public static MethodMgr GetInstance()
	{
		return m_Instance;
	}
	
	MethodMgrNormalImpl()
	{
		
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.mgr.methodmgr.MethodMgrBase#GetClassID(java.lang.Class)
	 */
	@Override
	public int GetClassID(Class<?> c)
	{
		RCC rcc = c.getAnnotation(RCC.class);
		Debug.Assert(rcc != null, new RegException("# 无法找到@RCC标识"));
		return rcc.ID();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.mgr.methodmgr.MethodMgrBase#RFCBuild()
	 */
	@Override
	public void RFCBuild()
	{
		RFCBuild.Build(m_Gather, RFCOutput.CShape, "");
		RFCBuild.Build(m_Gather, RFCOutput.Java, "");
	}

	private boolean _IsDirectRun(Method m)
	{
		RFC rfc = m.getAnnotation(RFC.class);
		if ( rfc == null )
		{
			return false;
		}
		return rfc.RunDirect();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.mgr.methodmgr.MethodMgrBase#_GetMethodEx(java.lang.Object, java.lang.reflect.Method, core.detail.interface_.MethodParam[], int, int)
	 */
	@Override
	protected MethodEx _GetMethodEx(Object p_RegObject, Method m, MethodParam[] mps, int classid, int methodid)
	{
		return new MethodExImpl(p_RegObject, m, mps, classid, methodid, _IsDirectRun(m));
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.mgr.methodmgr.MethodMgrBase#GetMethodID(java.lang.reflect.Method)
	 */
	@Override
	protected int GetMethodID(Method m)
	{
		RFC rfc = m.getAnnotation(RFC.class);
		if ( rfc == null )
		{
			return -1;
		}
		return rfc.ID();
	}
	
	/* (non-Javadoc)
	 * @see core.detail.impl.mgr.methodmgr.MethodMgrBase#IsSaveToFile()
	 */
	@Override
	protected boolean IsSaveToFile()
	{
		return true;
	}
}
