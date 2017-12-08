/**
 * MethodParam.java 2012-6-13下午10:03:01
 */
package core.detail.interface_;

import java.io.*;

import core.detail.eUserLockType;
import core.detail.impl.socket.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface MethodParam
{

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @return
	 */
	MethodParam Clone();

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param buffer
	 * @throws UnsupportedEncodingException 
	 */
	void ParseMsg(MsgBuffer buffer) throws Exception;

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @return
	 */
	boolean DataReady();

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @return
	 */
	boolean IsLock();

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param b
	 */
	void SetLock(eUserLockType type, boolean b);

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @return
	 */
	Object Get();

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 * @throws DBException 
	 *
	 */
	void SQLRun() throws Exception;

}
