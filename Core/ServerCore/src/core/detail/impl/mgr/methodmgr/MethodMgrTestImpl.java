/**
 * MethodMgrTestImpl.java 2013-2-26上午11:44:26
 */
package core.detail.impl.mgr.methodmgr;

import java.lang.reflect.Method;

import utility.Debug;
import utility.RFC.*;
import core.detail.impl.*;
import core.detail.interface_.*;
import core.exception.RegException;
import core.remote.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 测试的网络函数,只有RootConfig中的机器人配置打开后才有效
 */
public class MethodMgrTestImpl extends MethodMgrBase
{
	private static MethodMgrTestImpl m_Instance = new MethodMgrTestImpl();
	
	public static MethodMgr GetInstance()
	{
		return m_Instance;
	}
	
	MethodMgrTestImpl()
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
	 * @see core.detail.interface_.MethodMgr#RFCBuild()
	 */
	@Override
	public void RFCBuild()
	{
		RFCBuild.Build(m_Gather, RFCOutput.Java, "Test");
	}

	protected MethodEx _GetMethodEx(Object p_RegObject, Method m, MethodParam[] mps, int classid, int methodid)
	{
		TestRFC rfc = m.getAnnotation(TestRFC.class);
		return new MethodExImpl(p_RegObject, m, mps, classid, methodid, rfc.RunDirect());
	}

	protected int GetMethodID(Method m)
	{
		TestRFC rfc = m.getAnnotation(TestRFC.class);
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
