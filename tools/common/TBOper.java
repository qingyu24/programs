/**
 * DBOper.java 2013-7-4下午4:04:58
 */
package common;

import java.util.ArrayList;



/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface TBOper
{
	void Execute(Class<?> c, DB aim, DB m, ArrayList<DB> merges) throws Exception;
}
