/**
 * TestBit.java 2012-9-18下午2:10:54
 */
package test.utility;

import static org.junit.Assert.*;

import org.junit.Test;

import utility.Bit;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestBit
{

	@Test
	public void Test()
	{
		long v = 0x1234567887654321L;
		assertTrue( Bit.GetBig(v) == 0x12345678 );
		assertTrue( Bit.GetSmall(v) == 0x87654321 );
		int v1 = 0x12345678;
		assertTrue( Bit.GetBig(v1) == 0x1234 );
		assertTrue( Bit.GetSmall(v1) == 0x5678 );
		
		assertTrue( Bit.GetIntByIndex(v1, 0) == 0x12 );
		assertTrue( Bit.GetIntByIndex(v1, 1) == 0x34 );
		assertTrue( Bit.GetIntByIndex(v1, 2) == 0x56 );
		assertTrue( Bit.GetIntByIndex(v1, 3) == 0x78 );
		
		v1 = 0x98765432;
		assertTrue( Bit.GetIntByIndex(v1, 0) == (byte)0x98 );
		assertTrue( Bit.GetIntByIndex(v1, 1) == (byte)0x76 );
		assertTrue( Bit.GetIntByIndex(v1, 2) == (byte)0x54 );
		assertTrue( Bit.GetIntByIndex(v1, 3) == (byte)0x32 );
	}
}
