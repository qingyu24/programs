/**
 * TestUniqueID.java 2012-7-18上午10:27:24
 */
package test.utility;

import static org.junit.Assert.*;

import org.junit.Test;

import utility.UniqueID;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestUniqueID
{

	/**
	 * 瞬间创建大量的唯一id测试
	 */
	@Test
	public void TestBuildMoment()
	{
		UniqueID u = new UniqueID();
		long[] vs = new long[1000];
		for (int i = 0; i < vs.length; ++i )
		{
			vs[i] = u.Get();
		}
		for ( int i = 0; i < vs.length; ++i )
		{
			assertEquals(findnum(vs[i],vs),1);
		}
	}

	private int findnum(long v,long[] vs)
	{
		int num = 0;
		for (int i = 0; i < vs.length; ++i )
		{
			if ( v == vs[i] )
			{
				num++;
			}
		}
		return num;
	}
}
