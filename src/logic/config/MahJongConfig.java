package logic.config;

import utility.ExcelData;

/// <summary>
/// 麻将模板数据
/// </summary>
@ExcelData(File = "MahJongConfig.xls", Table = "Sheet1")
public class MahJongConfig implements IConfig{

	public int id;
	public int mahJongType;
	public int sortType;
	public String name;
	public String handMahIcon;
	public String mingMahIcon;
	public String manSound;
	public String womanSound;
	
	

}

