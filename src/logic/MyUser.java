/**
 * MyUser.java 2012-6-11下午10:06:52
 */
package logic;
import core.Root;
import core.RootConfig;
import core.Tick;
import core.detail.UserBase;
import core.detail.impl.socket.SendMsgBuffer;
import logic.module.character.CharacterImpl;
import logic.module.login.Login;
import logic.module.login.eLoginErrorCode;
import logic.module.room.Room;
import logic.userdata.logindata;
import logic.userdata.handler.PlayerCenterData;
import manager.ConfigManager;
import manager.RoomManager;
/**
 * @author ddoq
 * @version 1.0.0
 * 逻辑层定义的User对象
 */
public class MyUser extends UserBase implements Tick
{
	public boolean client_DataReady = false;
	public boolean server_DataReady = false;
	private eLoginErrorCode m_errorCode;
	private logindata m_loginData;
	private PlayerCenterData m_center;
	private int m_roomid = -1; // 房间标示;离线以后这个roomid要记录下来;
	
    
	public boolean CheckPayMark(String p_Mark)
	{
		return true;
	}
	
	public MyUser()
	{					
		//在此添加所有的UserData注册
		this.m_center = new PlayerCenterData(this);
		AddToUserData(this.m_center);
		
		
	}
	
	public PlayerCenterData getCenterData(){
		return this.m_center;
	}
	
	public void setRoomId(int rid){
		this.m_roomid = rid;
	}
	
	//获得房间的id;
	public int getRoomId(){
		return this.m_roomid;
	}
	
	//----------------------玩家需要计算的属性;

	public eLoginErrorCode getLoginRes(){
		return m_errorCode;
	}

	//获得角色id位移之前的数据;
	public int getBaseRoleID(int origLen){
		long serverId = RootConfig.GetInstance().ServerUniqueID;
		long uid = serverId << (64 - 12);
		long roleId = this.GetRoleGID();
		int baseId = (int)((roleId - uid) / 10);
		return (int) (baseId + origLen + 100000);// 0 - 100000是留给机器人的;
		
	}
		
	
	public void setLoginData(logindata ld, eLoginErrorCode code){
		m_loginData = ld;
		m_errorCode = code;
	}

	public boolean packBaseData(SendMsgBuffer buffer){
		m_center.packData(buffer);
		return true;
	}
	
	//打包用户的数据发送给前端;
	public void packUserData(SendMsgBuffer buffer){
		this.m_center.packSimpleData(buffer);
	}
	
	
	/* (non-Javadoc)
	 * @see core.UserBase#ExecuteKeyDataSQLRun()
	 */
	@Override
	public void ExecuteKeyDataSQLRun() throws Exception
	{
		//TODO;
	}
	
	/* (non-Javadoc)
	 * @see core.UserBase#OnDisconnect()
	 */
	@Override
	public void OnDisconnect() throws Exception
	{
		//当玩家断开的时候要将所有的数据释放掉。
		super.OnDisconnect();
		m_errorCode = eLoginErrorCode.UNKNOW;
		Login.GetInstance().OnDisconnect(this);
		ConfigManager.getInstance().OnDisconnect(this);
		Room room = RoomManager.getInstance().getRoom(this.GetRoleGID());
		if(null != room){
			room.tagLeaveRoom(this);
			
		}
		
		//Echo.GetInstance().OnDisconnect(this);
		if ( this.IsKeyDataReady() ) //如果这个玩家的标示还存在就释放数据；
		{
			//释放数据；
		}
		
		long uid = 0;
		client_DataReady = false;
	}
	
	@Override
	public void OnRelease() throws Exception
	{
		super.OnRelease();
	}

	/* (non-Javadoc)
	 * @see core.detail.UserBase#OnAllDataLoadFinish()
	 */
	@Override
	public void OnAllDataLoadFinish() throws Exception
	{
		System.out.println("*User的数据加载完成:" + this);
		Root.GetInstance().AddLoopTimer(this, 60 * 5, this); // 开启主循环;
		//设置防沉迷，是不是要踢掉这个玩家；
		//设置屏蔽，是不是要踢掉这个玩家；
		//向客户端发一下基础的角色数据,
		CharacterImpl.GetInstance().ServerDataReady(this);
		
	}
	
	/* (non-Javadoc)
	 * @see core.Tick#OnTick(long)
	 */
	@Override
	public void OnTick(long p_lTimerID) throws Exception
	{
		if ( IsDisabled() )
		{
			return;
		}
		AddSaveTask(false);
	}
	
	

}
	