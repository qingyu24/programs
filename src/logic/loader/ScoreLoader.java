package logic.loader;

import java.util.ArrayList;
import java.util.Iterator;
import core.DBLoaderEx;
import core.DBMgr;
import core.detail.impl.socket.SendMsgBuffer;
import logic.MyUser;
import logic.module.room.RoomUser;
import logic.userdata.account;
import logic.userdata.calScore;

public class ScoreLoader extends DBLoaderEx<calScore> {

	public ScoreLoader(calScore p_Seed) {
		super(p_Seed);
		// TODO Auto-generated constructor stub
	}

	public ScoreLoader(calScore p_Seed, boolean p_bSave) {
		super(p_Seed, p_bSave);
		// TODO Auto-generated constructor stub
	}

	private static ArrayList<String> m_codes = new ArrayList<String>();

	private static String sql_rank = "select * from calScore order by begintime desc limit %d";

	public void packData(SendMsgBuffer buffer) {
		Iterator<calScore> it = this.m_Datas.iterator();
		buffer.Add((short) this.m_Datas.size());
		while (it.hasNext()) {
			calScore g = it.next();
			g.packData(buffer);
		}
	}

	public void Add(RoomUser user) {
		// TODO Auto-generated method stub
		calScore score = new calScore();
		score.GID.Set(m_Datas.size());
		score.RoleID.Set(user.getRoleId());
		score.beginScore.Set(user.getUser().getCenterData().changeScore(0));
		score.beginTime.Set(System.currentTimeMillis());
		score.endtime.Set(System.currentTimeMillis());
		user.setScoregid(score.beginTime.GetMillis());
		super.Add(score);
	}

	public void end(RoomUser user) {
		Iterator<calScore> it = m_Datas.iterator();
		while (it.hasNext()) {
			calScore next = it.next();
			if (next.beginTime.GetMillis() == user.getScoregid() && next.RoleID.Get() == user.getRoleId()) {

				next.endScore.Set(user.getUser().getCenterData().changeScore(0));
				next.changeScore.Set(next.endScore.Get() - next.beginScore.Get());

			}

		}
	}

	public calScore getUser(long uid) {
		Iterator<calScore> it = this.m_Datas.iterator();
		while (it.hasNext()) {
			calScore user = it.next();
			if (user.RoleID.Get() == uid) {
				return user;
			}
		}
		return null;
	}

	public void packRanking(SendMsgBuffer buffer, MyUser user, int count) {

		String sql = String.format(sql_rank, count);
		account[] accs = DBMgr.ReadSQL(new account(), sql);

		buffer.Add((short) (accs.length + 1));
		for (int i = 0; i < accs.length; ++i) {
			buffer.Add(accs[i].RoleID.Get());
			buffer.Add(accs[i].nickName.Get());
			buffer.Add(accs[i].headIcon.Get());
			buffer.Add(accs[i].winCount.Get());
		}
		calScore a = this.getUser(user.GetRoleGID());
		buffer.Add(a.GID.Get());
		buffer.Add(a.RoleID.Get());
		buffer.Add(a.beginScore.Get());
		buffer.Add(a.beginTime.GetMillis());
		buffer.Add(a.endScore.Get());
		buffer.Add(a.endtime.GetMillis());
		buffer.Add(a.changeScore.Get());
		buffer.Add(a.winnerId.Get());
		buffer.Add(a.roomUser1.Get());
		buffer.Add(a.roomUser2.Get());
		buffer.Add(a.roomUser3.Get());

	}

	public void packUserList(SendMsgBuffer buffer, int page) {
		ArrayList<account> list = new ArrayList<account>();
		Iterator<calScore> it = this.m_Datas.iterator();
		float pageCount = 10f;
		int count = 10;
		int maxPage = (int) (list.size() / pageCount);
		float temp = list.size() / pageCount;

		if (temp > maxPage) {
			maxPage += 1;
		}
		if (page >= maxPage - 1) {
			count = (int) (list.size() % pageCount);
			page = maxPage - 1;
		}
		count = Math.min(count, list.size());
		// buffer.Add((short)count);
		buffer.Add((short) count);
		// if(page < maxPage)
		{
			for (int i = page * 10; i < page * 10 + count; ++i) {
				list.get(i).packData(buffer);
			}
		}
		buffer.Add(this.m_Datas.size());
	}

}
