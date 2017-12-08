/**
 * ThreadMgr.java 2012-10-17下午2:26:42
 */
package core.detail.interface_;

import core.detail.impl.eThreadType;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface ThreadMgr
{
	void Reg(Thread t, eThreadType type);
	
	void Remove(eThreadType type);
	
	eThreadType GetCurrThreadType();
	
	boolean IsEmpty();
}
