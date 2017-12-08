package logic.config.handler;

import java.util.ArrayList;
import java.util.Iterator;

import logic.config.IConfig;
import logic.config.MahJongConfig;
import logic.module.room.CardInfo;
import manager.CardManager;
import manager.RoomManager;

public class MahJongHandler implements IHandler {

	private ArrayList<MahJongConfig> m_list = new ArrayList<MahJongConfig>();
	@Override
	public void Init(IConfig[] val) {
		// TODO Auto-generated method stub
		for(int i = 0; i < val.length; ++ i){
			MahJongConfig conf = (MahJongConfig)val[i];
			this.m_list.add(conf);
		}
	}
	
	public int getSize(){
		return this.m_list.size();
	}
	
	public ArrayList<CardInfo> Random(int hui){
		ArrayList<CardInfo> list = new ArrayList<CardInfo>();
		ArrayList<MahJongConfig> temp = new ArrayList<MahJongConfig>();
		Iterator<MahJongConfig> it2 = m_list.iterator();
		while(it2.hasNext()){
			MahJongConfig conf = it2.next();
			if(CardManager.qufengpai){
				if(conf.mahJongType == 4 ||
						conf.mahJongType == 5 ||
						conf.mahJongType == 6 ||
						conf.mahJongType == 7 ||
						conf.mahJongType == 9 ||
						conf.mahJongType == 10){
					continue;
					
				}
				if(CardManager.isshanxi && conf.mahJongType == 8){
					continue;
				}
				
			}
			temp.add(conf);
		}
		for(int i = 0; i < temp.size(); ++ i){
			int index  = (int)((Math.random() * temp.size()));
			MahJongConfig conf = temp.get(i);
			temp.set(i, temp.get(index));
			temp.set(index, conf);
		}
		
		int pre = this.getPreHuiCard(hui);
		int nex = this.getNextHuiCard(hui);
		
		MahJongConfig conf1 = this.getConfigById(pre);
		MahJongConfig conf2 = this.getConfigById(nex);
		
		Iterator<MahJongConfig> it = temp.iterator();
		while(it.hasNext()){
			MahJongConfig c = it.next();
			CardInfo card = new CardInfo(null);
			card.init(c);
			if(CardManager.ishuludao){
				if(hui != -1){
					if(conf2.sortType == c.sortType){
						card.setHui(true);
					}
				}
			}
			if(CardManager.isyizhou){
				if(CardManager.canhui){
					if(conf1.sortType == c.sortType){
						card.setHui(true);
					}
					if(conf2.sortType == c.sortType){
						card.setHui(true);
					}
				}
			}
			if(CardManager.isjinzhou){
				if(CardManager.canhui){
					if(conf2.sortType == c.sortType){
						card.setHui(true);
					}
				}
			}
			list.add(card);
		}
		return list;
	}
	
	/**
	 * 返回首牌的id;
	 * @param id
	 * @return
	 */
	public int getPreHuiCard(int id){
		if(id == -1){
			return -1;
		}
		MahJongConfig conf = this.getConfigById(id);
		if(null == conf){
			return -1;
		}
		int rid = -1;
		int type = conf.sortType - 1;
		if(type == 0){
			rid = 33;
		}else if(type == 9){
			rid = 69;
		}else if(type == 18){
			rid = 105;
		}else if(type == 27){
			rid = 121;
		}else if(type == 31){
			rid = 133;
		}else{
			rid = id - 4;
		}
		
		if(CardManager.qufengpai){
			if(rid >= 109){
				rid = 125;
			}
		}
		return rid;
	}
	
	public int getNextHuiCard(int id){
		if(id == -1){
			return -1;
		}
		MahJongConfig conf = this.getConfigById(id);
		int rid = -1;
		int type = conf.sortType + 1;
		if(type == 10){
			rid = 1;
		}else if(type == 19){
			rid = 37;
		}else if(type == 28){
			rid = 73;
		}else if(type == 32){
			rid = 109;
		}else if(type == 35){
			rid = 125;
		}else{
			rid = id + 4;
		}
		if(CardManager.qufengpai){
			if(rid >= 109){
				rid = 125;
			}
		}
		return rid;
	}
	
	public MahJongConfig getConfigById(int id){
		Iterator<MahJongConfig> it = m_list.iterator();
		while(it.hasNext()){
			MahJongConfig conf = it.next();
			if(conf.id == id){
				return conf;
			}
		}
		return null;
	}
	

}
