package utils;
import java.util.ArrayList;
import java.util.Iterator;
import logic.MyUser;
import logic.module.room.RoomRule;
import logic.module.room.RoomUser;
import logic.module.room.TestUser;
import logic.userdata.IDeskBalance;
import logic.userdata.YZDeskBalance;

public class ScoreTest {

	
	public void test() {
		//	public void sumHu(boolean _hasHui, int _qd, int _zimo, int _ytl, int _qys, 
		//int _qysytl, int _hy, int _piao,
		//ArrayList<TestUser> users, RoomRule rr, long dianpa) {
		RoomUser roomUser = new RoomUser(new MyUser(), "100");
		IDeskBalance<YZDeskBalance> db = (YZDeskBalance) roomUser.getDeskBalance();
		RoomRule rr = new RoomRule(1, 3, 3, new ArrayList<Integer>(), 2,  new ArrayList<Integer>());
		
		boolean HasHui = false;
		int qd = 1;  //七对
		int _zimo=1;  //自摸
		int _ytl=1;    //一条龙
		int _qys=1;     // 清一色
		int _qysytl=0;  //
		int _hy=0;     //会悠
		long dianpaId=1;  //点炮ID
		
		ArrayList<TestUser> tlist = TestUser.getSingle().getlist();
		tlist.get(0).set(roomUser);
		long l = roomUser.getRoleId();
		tlist.get(0).set(l,  false,1,0);
		tlist.get(1).set(1,  true, 1, 0);//(id,是不是飘，明杠个数，暗杠个数)
		tlist.get(2).set(2,  false, 0, 0);
		tlist.get(3).set(3,  false, 0, 1);

		Iterator<TestUser> it = tlist.iterator();
		// 先算杠才能才能有正确的输出日志
		while (it.hasNext()) {
			TestUser obj = it.next();
			obj.sumGang(tlist);

		}
		Iterator<TestUser> its = tlist.iterator();
		while (its.hasNext()) {
			TestUser obj = its.next();

			if(obj.getM_roleId()==roomUser.getRoleId()){
				obj.changeGangTo();
			}
		}

		db.sumHu(HasHui, qd, _zimo, _ytl, _qys, _qysytl, _hy, 1, tlist, rr, dianpaId);
	}
}


