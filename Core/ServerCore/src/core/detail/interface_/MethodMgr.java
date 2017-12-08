/**
 * RunMethodMgr.java 2012-6-11下午11:00:10
 */
package core.detail.interface_;

import core.exception.*;


/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface MethodMgr
{
	/**
	 * 注册一个对象上的远程函数
	 */
	void Reg(Object p_regObj) throws RegException;

	/**
	 * 根据类id和方法id获取远程调用的函数
	 * 
	 * @return 无法找到返回null
	 */
	MethodEx GetMethod(byte classid, byte methodid);

	/**
	 * 创建远程调用函数
	 */
	void RFCBuild();
	
	/**
	 * 获取类的ID
	 */
	int GetClassID(Class<?> c);
}
