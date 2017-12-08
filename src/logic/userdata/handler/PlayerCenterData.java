package logic.userdata.handler;

import java.util.ArrayList;
import java.util.Iterator;

import core.DBMgr;
import core.SQLRun;
import core.UserData;
import core.detail.impl.socket.SendMsgBuffer;
import logic.MyUser;
import logic.PackBuffer;
import logic.Reg;
import logic.eProperty;
import logic.loader.UserLoader;
import logic.module.room.RoomInterface;
import logic.sqlrun.MySQLRun;
import logic.userdata.account;
import logic.userdata.charge_record;
import manager.LoaderManager;;
public class PlayerCenterData implements UserData {

	private boolean m_dataReady;
	private MyUser m_user;
	private account m_account;
	private ArrayList<charge_record> m_list;
	
	public PlayerCenterData(MyUser user){
		this.m_user = user;
		this.m_list = new ArrayList<charge_record>();
	}
	
	private int m_gid;
	public int getGid(){
		int baseId = m_user.getBaseRoleID(m_gid);
		m_gid ++;
		return baseId;
	}
	
	public String getOpenId(){
		return this.m_account.openid.Get();
	}
	
	public void packData(SendMsgBuffer buffer){
		m_account.packData(buffer);
	}
	
	public void packSimpleData(SendMsgBuffer buffer){
		buffer.Add(m_account.RoleID.Get());
		buffer.Add(m_account.nickName.Get());
		buffer.Add(m_account.headIcon.Get());
		buffer.Add(m_account.sex.Get());
		buffer.Add(m_account.prizecount.Get());
		buffer.Add(m_account.TID.Get());	
	}
	
	public int changeScore(int val){
		if(val == 0){
			return m_account.prizecount.Get();
		}
		m_account.prizecount.Set(m_account.prizecount.Get() + val);
		return m_account.prizecount.Get();
	}
	
	public void resetScore(){
		m_account.prizecount.Set(0);
	}
	
	//记录赢的局数;
	public void updateWinCount(){
		this.m_account.winCount.Set(m_account.winCount.Get() + 1);
	}
	
	public void addRecord(long targetRoleId, int score, String nick){
		charge_record cr = DBMgr.CreateRoleData(this.m_user.GetRoleGID(), new charge_record());
		cr.TargetRoleID.Set(targetRoleId);
		cr.card.Set(score);
		cr.Name.Set(nick);
		cr.DateTime.Set(System.currentTimeMillis());
		this.m_list.add(cr);
		
	}
	
	public void packRecordList(SendMsgBuffer buffer, int page){
		float pageCount = 10f;
		float count = 10f;
		int size = m_list.size();
		int maxPage = (int)(size / pageCount);
		float temp = (float)size / pageCount;
	
		if(temp > maxPage){
			maxPage += 1;
		}
		if(page >= maxPage - 1){
			count = (int) (size % pageCount);
			page = maxPage - 1;
		}
		count = (int) Math.min(count, size);
		
		buffer.Add((short)count);
		//if(page < maxPage)
		{
			for(int i =  page * 10 ; i < page * 10 + count; ++ i){
				m_list.get(i).packData(buffer);
			}
		}
		buffer.Add(this.m_list.size());
	}
	
	public int getRoomCard(){
		return m_account.roomCard.Get();
	}
	
	public void changeRoomCard(int val){
		int old = m_account.roomCard.Get();
		int ret = old + val;
		if(ret < 0){
			ret = 0;
		}
		m_account.roomCard.Set(ret);
		SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_CHANGE_ROOMCARD);
		p.Add(1);
		p.Add(ret);
		p.Send(m_user);
	}
	
	@Override
	public boolean DataReady() throws Exception {
		// TODO Auto-generated method stub
		return this.m_dataReady;
	}

	@Override
	public SQLRun GetSQLRun() throws Exception {
		// TODO Auto-generated method stub
		return new PlayerSqlRun();
	}

	@Override
	public void SaveToDB() throws Exception {
		// TODO Auto-generated method stub
		if(this.m_account != null){
			DBMgr.UpdateRoleData(this.m_account);
		}
		Iterator<charge_record> it = this.m_list.iterator();
		while(it.hasNext()){
			DBMgr.UpdateRoleData(it.next());
		}
	}
	
	
	public class PlayerSqlRun extends MySQLRun
	{
		@Override
		public void Execute(MyUser p_User) throws Exception {
			// TODO Auto-generated method stub
			m_dataReady = false;
			long gid = p_User.GetRoleGID();
			account[] data = DBMgr.ReadRoleIDData(gid, new account());
			if(data.length > 0){
				m_account = data[0];
				if(m_account.createTime.GetMillis() == 0){
					m_account.roomCard.Set(5);
					m_account.prizecount.Set(0);
					m_account.createTime.Set(System.currentTimeMillis());
					UserLoader loader = (UserLoader) LoaderManager.getInstance().getLoader(LoaderManager.Users);
					loader.addUser(m_account);
				}
				
			}else{
				//todo 写入错误日志：
			}
			//读取充值的日志;
			charge_record[] records = DBMgr.ReadRoleIDData(gid, new charge_record());
			for(int i = 0; i < records.length; ++ i){
				m_list.add(records[i]);
			}
			m_dataReady = true;
			
		}
	}

}
