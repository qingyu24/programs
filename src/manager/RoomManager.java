package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import core.detail.impl.log.Log;
import logic.MyUser;
import logic.module.log.eLogicDebugLogType;
import logic.module.room.Room;

public class RoomManager {
	
	private static RoomManager _instance;
	private static Map<Long, Room> x_list = new HashMap<Long,Room>();
	private static Map<Integer, Room> m_list = new HashMap<Integer, Room>();
	private static int roomId = 1;
	private static ArrayList<Integer> ms_roomids = new ArrayList<Integer>();
	private static int robotRoomIndex = 0;
	public static RoomManager getInstance(){
		if(_instance != null){
			return _instance;
		}
		return _instance = new RoomManager();
	}
	
	public Room createRoom(MyUser user){
		//todo这里需要检查是否在房间内。
		int id = this.generalRoomId();
		if(user.getCenterData().getOpenId().indexOf("TestUser", 0) == 0){
			id = 100 + robotRoomIndex;
			robotRoomIndex ++;
		}
		
		Room r = new Room(id,user.GetRoleGID());
		String str = String.format("%d 创建房间:%d", user.GetRoleGID(),id);
		LogRecord(user, str);
		m_list.put(id, r);
	/*	x_list.put(user.GetRoleGID(), r);*/
		return r;
	}
	
	public Room getRoom(long roleId){
		Set<Entry<Long, Room>> set = x_list.entrySet();
		Iterator<Entry<Long, Room>> iterator = set.iterator();
		while (iterator.hasNext()) {
		  Entry<Long, Room> next = iterator.next();
		  LogRecord(null, next.getKey()+":"+next.getValue());
			
		}
		if(x_list.get(roleId)==null){
			LogRecord(null, "取到的房间为空");
		}
		return x_list.get(roleId);
	}
	
	public boolean removeRoom(int roomId){
		Room r = this.getRoom(roomId);
		r.clearUser();
		r.destroy();
		m_list.remove(roomId);
		String str = String.format("房间被删除:%d\n", roomId);
		LogRecord(null, str);
		return true;
	}
	
	public void joinRoom(MyUser user, int roomId){
		Room room = this.getRoom(roomId);
		if(null != room){
			LogRecord(user, user.GetUserName()+"加入房间");
			x_list.put(user.GetRoleGID(), room);
		}
	}
	
	public Room getRoom(int roomId){
		return m_list.get(roomId);
	}
	
	public void removeRoomUser(long roleId){
		x_list.remove(roleId);
		System.err.printf("删除房间内的用户:%d\n", roleId);
		
	}
	
	private int generalRoomId(){
		if(this.roomId >= ms_roomids.size()){
			this.roomId = 0;
		}
		return ms_roomids.get(this.roomId++);
		
	}
	
	public static void initRoomId(){
		int index = 0;
		int r = (int) (Math.random() * 900000) + 100000;
		while(index++ < 1000){
			while(hasRoomId(r)){
				r = (int) (Math.random() * 900000) + 100000;
			}
			//System.out.printf("roomId:%d\n", r);
			ms_roomids.add(r);
		}
	}
	
	private static boolean hasRoomId(int id){
		for(int i = 0; i < ms_roomids.size(); ++i){
			if(ms_roomids.get(i) == id){
				return true;
			}
		}
		return false;
	}
	
	private void LogRecord(MyUser user, String record)
	{
		if(null != user){
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, user.GetRoleGID(), record);
		}else{
			Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, 0l, record);
		}
		
	}
	
}
