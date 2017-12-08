package logic.module.room;

import logic.eCardCatchType;
import logic.eGangType;
import logic.config.MahJongConfig;
import manager.CardManager;

public class CardInfo {
	
	private int templeteId;
	private int m_type;
	private int m_number;
	private MahJongConfig m_conf;
	private String m_name;
	private eGangType m_gangType;
	private eCardCatchType m_catchtype;
	private RoomUser m_ru;
	private boolean m_kou;
	private boolean m_hui; //是否是会儿牌：
	private boolean m_bao; //是否是宝牌;
	private boolean m_from; //是否来源于杠后摸到的牌
	private boolean m_fromAn; //是否来源于杠后摸到的牌
	private boolean m_fromcai; //是否彩杠；
	
	public CardInfo(RoomUser user){
		m_ru = user;
		
	}
	public void init(MahJongConfig conf){
		templeteId = conf.id;
		m_type = conf.mahJongType;
		m_number = conf.sortType;
		m_name = conf.name;
		m_conf = conf;
		m_bao = false;
		m_from = false;
		m_fromAn=false;
		m_fromcai = false;
	}
	
	public int getTid(){
		return templeteId;
	}
	
	public String getName(){
		return m_name + "[" + templeteId + "-" + m_type + "-" + m_number + "]";
	}
	
	
	
	public int getType(){
		return m_type;
	}
	
	public int getNumber(){
		return m_number;
	}
	
	public RoomUser getOwner(){
		return m_ru;
	}
	
	public void setOwner(RoomUser ru){
		m_ru = ru;
		if(ru == null){
			assert(false);
		}
	}

	
	//牌的获取方式;
	//暂时不用;
	public void setCatchType(eCardCatchType cc){
		m_catchtype = cc;
	}
	
	public eCardCatchType getCatchType(){
		return m_catchtype;
	}
	
	public void setGangType(eGangType type){
		this.m_gangType = type;
	}
	
	//获得杠的类型;(1:直杠，2：明杠，3:暗杠, 4:首圈杠(如果你是开的庄家的杠，庄家要给每家+1分))
	public eGangType getGangType(){
		return this.m_gangType;
	}
	
	public void setKou(boolean ret){
		m_kou = ret;
	}
	
	public boolean getKou(){
		return m_kou;
	}
	
	public  void setHui(boolean hui){
		this.m_hui = hui;
	}
	
	public boolean getHui(){
		return this.m_hui;
	}
	
	//设置为宝牌;
	public  void setBao(boolean bao){
		this.m_bao = bao;
	}
	
	public boolean getBao(){
		return this.m_bao;
	}
	
	public void setFromCaigang(boolean gang){
		m_fromcai = gang;
	}
	
	public void setFromGang(boolean gang){
		this.m_from = gang;
	}
	public void setFromGangAn(boolean gang){
		this.m_fromAn = gang;
	}
	
	//是否来源于杠后摸到的牌;
	public boolean getFromCaiGang(){
		return this.m_fromcai;
	}
	
	//是否来源于杠后摸到的牌;
	public boolean getFromGang(){
		return this.m_from;
	}
	//是否来源于杠后摸到的牌;
	public boolean getFromGangAn(){
		return this.m_fromAn;
	}
	
	
	//是不是1.9牌;
	public boolean isOneNine(){
		return this.m_number == 1 || this.m_number == 9 
				|| this.m_number == 10 || this.m_number == 18 
				||this.m_number == 19 || this.m_number == 27||(this.getType()>3&&CardManager.ishuludao)||(this.getType()==8&&CardManager.isjinzhou);
	}
	
	public boolean sameTo(CardInfo other){
		return this.m_number == other.getNumber() && this.m_type == other.getType();
	}
	
	public CardInfo clone(){
		CardInfo info = new CardInfo(m_ru);
		info.init(m_conf);
		info.m_catchtype = this.m_catchtype;
		info.m_gangType = this.m_gangType;
		info.m_hui = this.m_hui;
		info.m_kou = this.m_kou;
		info.m_ru = this.m_ru;
		return info;
	}
	
	
}
