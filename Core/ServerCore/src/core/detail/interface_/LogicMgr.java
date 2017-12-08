/**
 * LogicMgr.java 2012-6-13下午11:32:25
 */
package core.detail.interface_;

import core.Frame;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface LogicMgr extends MgrBase
{
	void AddNetTask(MethodEx mex);

	void AddSqlTask(MethodEx mex);

	void AddUpdater(Frame f);
}
