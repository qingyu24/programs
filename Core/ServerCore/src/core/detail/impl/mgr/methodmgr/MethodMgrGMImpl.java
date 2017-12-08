/**
 * GMMethodMgrImpl.java 2013-1-19上午10:35:11
 */
package core.detail.impl.mgr.methodmgr;

import java.lang.reflect.Method;

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
 * GM指令需要的网络接口函数
 */
public class MethodMgrGMImpl extends MethodMgrBase
{
	private static MethodMgrGMImpl m_Instance = new MethodMgrGMImpl();
	
	public static MethodMgr GetInstance()
	{
		return m_Instance;
	}
	
	MethodMgrGMImpl()
	{
		
	}
	
	@Override
	public int GetClassID(Class<?> c)
	{
		GMRCC rcc = c.getAnnotation(GMRCC.class);
		Debug.Assert(rcc != null, new RegException("# 无法找到@GMRCC标识"));
		return rcc.ID();
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodMgr#RFCBuild()
	 */
	@Override
	public void RFCBuild()
	{
		RFCBuild.Build(m_Gather, RFCOutput.GMJava, "GM");
	}
	
	protected MethodEx _GetMethodEx(Object p_RegObject, Method m, MethodParam[] mps, int classid, int methodid)
	{
		GMRFC rfc = m.getAnnotation(GMRFC.class);
		return new MethodExGMImpl(p_RegObject, m, mps, classid, methodid, rfc.RunDirect(), rfc.Level());
	}

	protected int GetMethodID(Method m)
	{
		GMRFC rfc = m.getAnnotation(GMRFC.class);
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
