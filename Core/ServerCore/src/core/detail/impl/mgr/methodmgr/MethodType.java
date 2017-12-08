/**
 * MethodType.java 2013-2-26下午12:00:50
 */
package core.detail.impl.mgr.methodmgr;

import java.util.ArrayList;

import utility.Debug;
import core.detail.interface_.MethodMgr;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum MethodType
{
	NORMAL,
	TEST,
	GM,
	;
	
	public MethodMgr GetMethodMgr()
	{
		switch (this)
		{
		case NORMAL:	return MethodMgrNormalImpl.GetInstance();
		case TEST:	return MethodMgrTestImpl.GetInstance();
		case GM:	return MethodMgrGMImpl.GetInstance();
		default:	Debug.Assert(false, "未使用的MethodType:" + this);
		}
		return null;
	}
	
	public static ArrayList<MethodMgr> GetAllMethodMgr()
	{
		ArrayList<MethodMgr> all = new ArrayList<MethodMgr>();
		all.add(NORMAL.GetMethodMgr());
		all.add(TEST.GetMethodMgr());
		all.add(GM.GetMethodMgr());
		return all;
	}
}
