/**
 * TestExcelIniReader.java 2012-6-19下午12:03:41
 */
package test.utility;

import static org.junit.Assert.*;

import org.junit.Test;

import utility.ExcelData;
import utility.ExcelIniReader;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestExcelIniReader
{
	@ExcelData( File = "exampleini.xls", Table = "Sheet1" )
	public static class ExampleIniExcelData
	{
		public boolean	Init;
		public int		Money;
		public String	Msg;
		
		public long		UnKnow;
		public String	UnKnowMsg;
	}
	
	/**
	 * 不支持内建类型,所以要用这个类型来包装一下
	 */
	public static class MsgType
	{
		public String Msg;
	}
	
	@ExcelData( File = "exampleini.xls", Table = "Sheet1" )
	public static class ExampleIniExcelData1
	{
		public boolean	Init;
		public int		Money;
		public MsgType[]Msg;
	}
	
//	@Test
//	public void ReadXlsIniFile()
//	{
//		ExampleIniExcelData e = new ExampleIniExcelData();
//		ExcelIniReader.Read(e);
//		
//		assertEquals(e.Init, true);
//		assertEquals(e.Money, 100000);
//		assertEquals(e.Msg, "hello~哈哈");
//		assertEquals(e.UnKnow, 0);
//		assertEquals(e.UnKnowMsg, null);
//	}
	
	@Test
	public void ReadXlsIniFile1()
	{
		ExampleIniExcelData1 e = new ExampleIniExcelData1();
		ExcelIniReader.Read(e);
		
		assertEquals(e.Init, true);
		assertEquals(e.Money, 100000);
		assertEquals(e.Msg.length, 4);
		assertEquals(e.Msg[0].Msg, "hello~哈哈");
	}

}
