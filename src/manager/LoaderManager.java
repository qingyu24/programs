package manager;

import java.util.HashMap;
import java.util.Map;

import core.DBLoader;
import logic.loader.UserLoader;
import logic.userdata.account;

public class LoaderManager {
	
	private static LoaderManager _instance;
	private static Map<String,DBLoader> m_list = new HashMap<String, DBLoader>();
	public static String Users = "Users";
	
	public static LoaderManager getInstance(){
		if(_instance != null){
			return _instance;
		}
		return _instance = new LoaderManager();
	}
	
	public void loadAll(){
		UserLoader users = new UserLoader(new account());
		m_list.put(Users, users);
	}
	
	public DBLoader getLoader(String name){
		return m_list.get(name);
	}
	
}
