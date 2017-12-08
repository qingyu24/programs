package logic.config;
import utility.ExcelData;
/**
 * @author niuhao
 * @version 1.0.0
 *
 */
@ExcelData(File = "CommonConfig.xls", Table = "setting")
public class CommonConfig implements IConfig {
	public int RoleDefaultTemplete; //
	public int AttackSpeedSlow;
	public float AttackSpeedGeneral ;
	public int AttackSpeedFast;
	public int GeneralGold;
	public int GeneralDiamonds;
	public int GeneralFriendship;
	public int PhysicalStrength;
	public int GeneralShoes;
	

}
