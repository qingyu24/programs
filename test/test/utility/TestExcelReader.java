/**
 * TestExcelReader.java 2012-6-10下午4:51:27
 */
package test.utility;

import static org.junit.Assert.*;

import org.junit.Test;

import utility.*;


/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestExcelReader
{
	public static class Data
	{
		public int	ID;
		public int	Value;
	}
	@ExcelData( File = "example.xls", Table = "Sheet1" )
	public static class ExampleExcelData
	{
		public int		ID;
		public String	Name;
		public Data[]	Data;
		public String	Other;
	}
	
	@ExcelData( File = "example.xls", Table = "Sheet1" )
	public static class ExampleExcelData1
	{
		public int		ID;
		public String	Name;
		public Data		Data;
		public Data		Data1;
		public String	Other;
	}
	
	@Test
	public void NormalReadXlsFile()
	{
		ExampleExcelData[] d = ExcelReader.Read(new ExampleExcelData());
		assertEquals(d.length, 3);
		assertEquals(d[0].ID, 1);
		assertEquals(d[0].Name, "嗷嗷嗷");
		assertEquals(d[1].ID, 2);
		assertEquals(d[1].Name, "abcd");
		assertEquals(d[0].Data.length, 2);
		assertEquals(d[0].Data[0].ID, 444);
		assertEquals(d[0].Data[0].Value, 88);
		assertEquals(d[0].Data[1].ID, 0);
		assertEquals(d[0].Data[1].Value, 0);
		
		assertEquals(d[2].Other, "");
		assertEquals(d[2].Data[0].ID, 0);
	}
	
	@Test
	public void NormalReadXlsFile1()
	{
		ExampleExcelData1[] d = ExcelReader.Read(new ExampleExcelData1());
		assertEquals(d.length, 3);
		assertEquals(d[0].ID, 1);
		assertEquals(d[0].Name, "嗷嗷嗷");
		assertEquals(d[1].ID, 2);
		assertEquals(d[1].Name, "abcd");
		
		assertEquals(d[1].Data1.ID, 447);
		assertEquals(d[1].Data1.Value, 99);
	}
}
