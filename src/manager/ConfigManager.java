package manager;

import java.util.HashMap;
import java.util.Map;

import logic.MyUser;
import logic.config.CommonConfig;
import logic.config.IConfig;
import logic.config.MahJongConfig;
import logic.config.MaskWordConfig;
import logic.config.PlayerInitIni;
import logic.config.ServerCommonConfig;
import logic.config.TestCardConfig;
import logic.config.handler.IHandler;
import logic.config.handler.MahJongHandler;
import logic.config.handler.MaskWordHandler;
import utility.ExcelIniReader;
import utility.ExcelReader;


/**
 * 本地配置文件管理,获取配置的具体数据，并读入到内存当中。
 * 
 * @author niuhao
 *
 */

public class ConfigManager {

	private static ConfigManager _instance;
	private static Map<String, IConfig[]> xlsList = new HashMap<String,IConfig[]>();
	private static Map<String, IConfig> iniList = new HashMap<String,IConfig>();
	private static Map<String, IHandler> handlers = new HashMap<String, IHandler>();
	public static long serverStartRunTime; // 开服时间;
	public static String CommonConfig = "CommonConfig";
	public static String ServerCommonConfig = "ServerCommonConfig";
	public static String PlayerInitIni = "PlayerInitIni";
	public static String MaskWordData = "MaskWordData";
	public static String MahJongConfig = "MahJongConfig";
	public static String TestCardConfig = "TestCardConfig";
	public static ConfigManager getInstance(){
		if(_instance != null){
			return _instance;
		}
		return _instance = new ConfigManager();
	}
	
	/**
	 * 初始化所有的服务器配置文件；
	 */
	public void initAll(){

		this.parseXls(CommonConfig, new CommonConfig(), true, null);
		this.parseXls(ServerCommonConfig, new ServerCommonConfig(), true, null);
		this.parseXls(PlayerInitIni, new PlayerInitIni(), true, null);
		this.parseXls(MaskWordData, new MaskWordConfig(),false, new MaskWordHandler());
		this.parseXls(MahJongConfig, new MahJongConfig(),false, new MahJongHandler());
		if(CardManager.isyizhou||CardManager.ishuludao||CardManager.isjinzhou||CardManager.ischaoyang||CardManager.isshanxi){
		this.parseXls(TestCardConfig, new TestCardConfig(), true, null);
		TestCardConfig iConfig = (logic.config.TestCardConfig) iniList.get(TestCardConfig);
		CardManager.isTest=iConfig.isTset;
	}
	}
	
	/**
	 * 解析xls文件
	 * @param filename
	 * @param mapClass
	 */
	public <T> void parseXls(String filename,IConfig mapInstance, Boolean iniFile, IHandler handler){
		
		if(iniFile){
			ExcelIniReader.Read(mapInstance);
			iniList.put(filename, mapInstance);
		}else{
			IConfig[] val = ExcelReader.Read(mapInstance);
			if(val != null){
				xlsList.put(filename, val);
			}
			//System.out.print("data length:" + val.length + "\n");
			if(handler != null){
				handler.Init(val);
			}
			
			handlers.put(filename, handler);
		}
	}
	
	/**
	 * 通过name获取MyConfig[]的实例；
	 * @param name
	 * @return
	 */
	public IConfig[] getConfigList(String name){
		return xlsList.get(name);
	}
	
	/**
	 * 通过id获取MyConfig[]的实例；
	 * @param name
	 * @return
	 */
	public IConfig[] getConfigList(int name){
		return null;
	}
	
	/**
	 * 通过name获取Config的实例；
	 * @param name
	 * @return
	 */
	public IConfig getConfig(String name){
		return iniList.get(name);
	}
	
	/**
	 * 通过id获取Config的实例；
	 * @param name
	 * @return
	 */
	public IConfig getConfig(int name){
		return null;
	}
	
	public IHandler getHandler(String name){
		return handlers.get(name);
	}
	
	
	public void OnDisconnect(MyUser user){
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
