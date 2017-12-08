/**
 * TestGlobalData.java 2012-9-3上午10:35:51
 */
package db;

import core.DBLoaderEx;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestReadGlobalData extends DBLoaderEx<TestGlobalData>
{
	protected TestReadGlobalData()
	{
		super(new TestGlobalData(), false);
	}
}
