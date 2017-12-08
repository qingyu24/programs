/**
 * SqlMgr.java 2012-6-13下午11:49:35
 */
package core.detail.interface_;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface SqlMgr extends MgrBase
{
	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param mex
	 */
	void AddTask(MethodEx mex);
	
	boolean IsBusy();
	
	int BusyRatio();
}
