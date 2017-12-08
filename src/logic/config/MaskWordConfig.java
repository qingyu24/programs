package logic.config;
import utility.ExcelData;
@ExcelData( File = "MaskWordData.xls", Table = "关键字" )
public class MaskWordConfig implements IConfig
{
	public String word;
	public String word1;
	public String word2;
	public String word3;
}
