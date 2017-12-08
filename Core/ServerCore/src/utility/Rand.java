/**
 * Rand.java 2012-7-5下午3:38:35
 */
package utility;

import java.util.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Rand
{
	private static Random m_Random = new Random();
	
	/**
	 * 得到一个随机数,不确定正负
	 */
	public static int Get()
	{
		return m_Random.nextInt();
	}
	
	public static float GetRatio() {
		int curNum = GetIn100();
		return (float)curNum / 100;
	}
	
	/**
	 * 得到一个0~100以内的数
	 */
	public static int GetIn100()
	{
		return Math.abs(Get()) % 100;
	}
	
	/**
	 * 得到一个0~1000000以内的数
	 */
	public static int GetInMillion()
	{
		return Math.abs(Get()) % 1000000;
	}
	
	// 获取 0 - N 之间的随机数(包含0，不包含N)
	public static int Get(int n)
	{
		Debug.Assert(n > 0, "n必须是正数");
		return Math.abs(Get()) % n;
	}
	
	/**
	 * 随机boolean
	 */
	public static boolean Execute()
	{
		return m_Random.nextBoolean();
	}
	
	public static byte[] CreateBytes(int length)
	{
		byte[] bs = new byte[length];
		m_Random.nextBytes(bs);
		return bs;
	}
}
