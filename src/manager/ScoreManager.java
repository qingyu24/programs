package manager;

import java.util.HashMap;
import java.util.Map;

import core.DBLoader;
import logic.loader.ScoreLoader;
import logic.loader.UserLoader;
import logic.userdata.account;
import logic.userdata.calScore;

public class ScoreManager {
	
	private static ScoreManager _instance;
	private static Map<String,DBLoader> m_list = new HashMap<String, DBLoader>();
	public static String Scores = "Scores";
	
	public static ScoreManager getInstance(){
		if(_instance != null){
			return _instance;
		}
		return _instance = new ScoreManager();
	}
	
	public void loadAll(){
		ScoreLoader score = new ScoreLoader(new calScore());
		m_list.put(Scores, score);
	}
	
	public DBLoader getLoader(String name){
		return m_list.get(name);
	}
	
}
