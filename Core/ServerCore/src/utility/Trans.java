/**
 * Trans.java 2012-7-5上午11:29:11
 */
package utility;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Trans
{
	public static void SetIntToByteArray(byte[] b, int start, int value)
	{
		Debug.Assert(b.length >= start + 4, "");
		b[start] = GetByte(value,3);
		b[start+1] = GetByte(value,2);
		b[start+2] = GetByte(value,1);
		b[start+3] = GetByte(value,0);
	}
	
	public static void SetShortToByteArray(byte[] b, int start, Short value)
	{
		Debug.Assert(b.length >= start + 2, "");
		b[start] = GetByteByShort(value,1);
		b[start+1] = GetByteByShort(value,0);
		
	}
	
	public static byte[] GetByteFromShort(short value)
	{
		byte[] b = new byte[2];
		SetShortToByteArray(b, 0 , value);
		return b;
	}
	
	/**
	 * 把一个int值转换为byte数组
	 * 
	 * <br>
	 * 测试代码:{@link TestTrans#ByteIntTrans}
	 * 
	 * @param value
	 *            要读取的int值
	 * @return 读取到数组,长度一定为4
	 */
	public static byte[] GetByteFromInt(int value)
	{
		byte[] b = new byte[4];
		SetIntToByteArray(b, 0 , value);
		return b;
	}
	
	/**
	 * 从一个byte数组的start位置那读取一个int值
	 * 
	 * <br>
	 * 测试代码:{@link TestTrans#ByteIntTrans}
	 * 
	 * @param b
	 *            读取的数组
	 * @param start
	 *            读取的起始位置
	 * @return 读取到的数据
	 */
	public static int GetIntFromByte(byte[] b, int start)
	{
		Debug.Assert(b.length >= start + 4, "");
		int v = b[start+3];
		v |= (int)b[start] << 24;
		v |= (int)b[start+1] << 16;
		v |= (int)b[start+2] << 8;
		return v;
	}
	
	/**
	 * 把一个int拆分成byte,如0x12 34 56 78,index为0返回0x78,为1返回0x56,为2返回0x34,为3返回0x12
	 * 
	 * <br>
	 * 测试代码:{@link TestTrans#TestGetByte}
	 * 
	 * @param v
	 *            要读取的数据
	 * @param index
	 *            读取的位置,只能为0~3
	 * @return 读取到byte值
	 */
	public static byte GetByte(int v,int index)
	{
		Debug.Assert(index >= 0 && index < 4, "");
		switch(index)
		{
		case 0:
			return (byte) (v & 0x000000ff);
		case 1:
			return (byte) ((v & 0x0000ff00) >> 8);
		case 2:
			return (byte) ((v & 0x00ff0000) >> 16);
		case 3:
			return (byte) ((v & 0xff000000) >> 24);
		default:
				return 0x00;
		}
	}
	//index 0:低位 1：高位
	public static byte GetByteByShort(short v,int index)
	{
		Debug.Assert(index >= 0 && index < 2, "");
		switch(index)
		{
		case 0:
			return (byte) (v & 0x000000ff);
		case 1:
			return (byte) ((v & 0x0000ff00) >> 8);
		
		default:
				return 0x00;
		}
	}
	
	public static short GetShort(byte v1, byte v2) {
		short v = (short) (((short)v1) << 8); 
		v |= (short)v2;
		
		return v;
	}
	
	/*
	 * v1, v2, v3都是1个字节的范围的值的int
	 */
	public static int GetInteger(int v1, int v2, int v3) {
		Debug.Assert(v1 < 256 && v2 < 256 && v3 < 256, "v1, v2, v3都是1个字节的范围的值的int");
		
		int v = (v1 << 16) | (v2 << 8) | v3 ;
		
		return v;
	}
	
	/**
	 * 由两个int组成一个long
	 */
	public static long GetLong(int v1,int v2)
	{
		long v = GetLong2(v2);
		v |= (long)v1 << 32;
		return v;
	}
	
	public static long GetLong(int v1, byte v2, byte v3) {
		int v = v2 << 8;
		v |= v3;
		return GetLong(v1, v);
	}
	
	/*
	 * long 的高32位置0
	 */
	
	private static long GetLong2(long v) {
		return v & 0x0ffffffffL;
	}
}
