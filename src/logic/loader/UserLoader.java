package logic.loader;

import java.util.ArrayList;
import java.util.Iterator;

import core.DBLoaderEx;
import core.DBMgr;
import core.detail.impl.socket.SendMsgBuffer;
import logic.MyUser;
import logic.userdata.account;

public class UserLoader extends DBLoaderEx<account> {

	private static ArrayList<String> m_codes = new ArrayList<String>();
	private static String sql_add = "insert into charge_record(RoleID, TargetRoleID, card)values(%d, %d, %d)";
	private static String sql_rank = "select * from account order by winCount desc limit %d";
	private static String sql_query_rank = "SELECT * FROM (SELECT (@rownum:=@rownum+1) AS rownum, a.RoleID FROM `account` a, (SELECT @rownum:= 0 ) r  ORDER BY a.`winCount` DESC) AS b  WHERE RoleID = %d";
	public UserLoader(account p_Seed) {
		super(p_Seed);
	}
	
	public void packData(SendMsgBuffer buffer){
		Iterator<account> it = this.m_Datas.iterator();
		buffer.Add((short)this.m_Datas.size());
		while(it.hasNext()){
			account g = it.next();
			g.packData(buffer);
		}
	}
	
	public int addScore(long adminRoleId, long roleId, int score){
		account acc = this.getUser(roleId);
		if(null != acc){
			acc.roomCard.Set(acc.roomCard.Get() + score);
			//String sql = String.format(sql_add, adminRoleId, roleId, score);
			//DBMgr.ExecuteSQL(sql);
			return acc.roomCard.Get();
			//insert record;
			
		}
		return 0;
	}
	
	public account getUser(String account){
		Iterator<account> it = this.m_Datas.iterator();
		while(it.hasNext()){
			account user = it.next();
			if(user.nickName.Get().equals(account)){
				return user;
			}
		}
		return null;
	}
	
	public account getUser(long uid){
		Iterator<account> it = this.m_Datas.iterator();
		while(it.hasNext()){
			account user = it.next();
			if(user.RoleID.Get() == uid){
				return user;
			}
		}
		return null;
	}
	
	public account getUser(int uid){
		Iterator<account> it = this.m_Datas.iterator();
		while(it.hasNext()){
			account user = it.next();
			if(user.TID.Get() == uid){
				return user;
			}
		}
		return null;
	}
	
	public void addUser(account user){
		this.m_Datas.add(user);
	}
	
	public void packRanking(SendMsgBuffer buffer, MyUser user,int count){
		
		String sql = String.format(sql_rank, count);
		account[] accs = DBMgr.ReadSQL(new account(), sql);
		
		buffer.Add((short)(accs.length + 1));
		for(int i = 0; i < accs.length; ++ i){
			buffer.Add(accs[i].RoleID.Get());
			buffer.Add(accs[i].nickName.Get());
			buffer.Add(accs[i].headIcon.Get());
			buffer.Add(accs[i].winCount.Get());
		}
		account a = this.getUser(user.GetRoleGID());
		buffer.Add(a.RoleID.Get());
		buffer.Add(a.nickName.Get());
		buffer.Add(a.headIcon.Get());
		buffer.Add(a.winCount.Get());
	}
	
	public void packUserList(SendMsgBuffer buffer, int page){
		ArrayList<account> list = new ArrayList<account>();
		Iterator<account> it = this.m_Datas.iterator();
		float pageCount = 10f;
		int count = 10;
		int maxPage = (int)(list.size() / pageCount);
		float temp = list.size() / pageCount;
	
		if(temp > maxPage){
			maxPage += 1;
		}
		if(page >= maxPage - 1){
			count = (int) (list.size() % pageCount);
			page = maxPage - 1;
		}
		count = Math.min(count, list.size());
		//buffer.Add((short)count);
		buffer.Add((short)count);
		//if(page < maxPage)
		{
			for(int i =  page * 10 ; i < page * 10 + count; ++ i){
				list.get(i).packData(buffer);
			}
		}
		buffer.Add(this.m_Datas.size());
	}
	

}
