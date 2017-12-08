/**
 * Bit.java 2012-9-18下午2:08:41
 */
package utility;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 二进制处理
 * <br>测试代码:{@link TestBit#Test}
 */
public class Bit
{
	/**
	 * 获取高32位的值,如0x12345678 87654321L,得到0x12345678
	 */
	public static int GetBig(long v)
	{
		v = v >> 32;
		return (int) v;
	}
	
	/**
	 * 获取低32位的值,如0x12345678 87654321L,得到0x87654321L
	 */
	public static int GetSmall(long v)
	{
		return (int) (v & 0x00000000ffffffffL);
	}
	
	/**
	 * 获取高16位的值,如0x12345678,得到0x1234
	 */
	public static short GetBig(int v)
	{
		return (short) (v >> 16);
	}
	
	/**
	 * 获取低16位的值,如0x12345678,得到0x5678
	 */
	public static short GetSmall(int v)
	{
		return (short) (v & 0x0000ffff);
	}
	
	/**
	 * 从一个整数上获取一个byte,如0x12345678,index为0时返回0x12,index为3时返回0x78
	 * 
	 * @param index 获取的索引
	 */
	public static byte GetIntByIndex(int v, int index)
	{
		Debug.Assert(index >= 0 && index <= 3, "index的范围是0~3,现在是:" + index);
		switch (index)
		{
		case 0:	return (byte)((v >> 24) & 0x000000ff);
		case 1:	return (byte)(((v & 0x00ff0000) >> 16) & 0x000000ff);
		case 2: return (byte)(((v & 0x0000ff00) >> 8) & 0x000000ff);
		case 3: return (byte) ((v & 0x000000ff) & 0x000000ff);
		default: return 0x00;
		}
	}
}
