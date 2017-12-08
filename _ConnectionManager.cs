using Networks;
public static class _ConnectionManager
{
	static public void Login_Enter(this ConnectionManager mgr, string p_username, string p_password, int p_nServerID, string p_deviceIdentifier, string p_deviceModel)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(0,0).Add(p_username).Add(p_password).Add(p_nServerID).Add(p_deviceIdentifier).Add(p_deviceModel).Send(mgr);
	}

	static public void Login_Register(this ConnectionManager mgr, string p_username, string p_password, int p_nServerID, string p_deviceIdentifier, string p_deviceModel)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(0,2).Add(p_username).Add(p_password).Add(p_nServerID).Add(p_deviceIdentifier).Add(p_deviceModel).Send(mgr);
	}

	static public void Login_ThirdLogin(this ConnectionManager mgr, string p_username, string p_nickname, string p_password, int p_nServerID, string p_headurl, string p_sex, string p_deviceIdentifier, string p_deviceModel)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(0,4).Add(p_username).Add(p_nickname).Add(p_password).Add(p_nServerID).Add(p_headurl).Add(p_sex).Add(p_deviceIdentifier).Add(p_deviceModel).Send(mgr);
	}

	static public void CharacterImpl_RequestBaseInfo(this ConnectionManager mgr)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(5,0).Send(mgr);
	}

	static public void RoomImpl_cenceldis(this ConnectionManager mgr, int roomId)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,41).Add(roomId).Send(mgr);
	}

	static public void RoomImpl_AutoCard(this ConnectionManager mgr, int p_user)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,40).Add(p_user).Send(mgr);
	}

	static public void RoomImpl_EnterRoom(this ConnectionManager mgr, int rm, string ru)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,1).Add(rm).Add(ru).Send(mgr);
	}

	static public void RoomImpl_OperCard(this ConnectionManager mgr, int ru, int[] cid)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,5).Add(ru).Add(cid).Send(mgr);
	}

	static public void RoomImpl_InitCard(this ConnectionManager mgr, int roomId, string ip)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,4).Add(roomId).Add(ip).Send(mgr);
	}

	static public void RoomImpl_GameReady(this ConnectionManager mgr, int roomId)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,6).Add(roomId).Send(mgr);
	}

	static public void RoomImpl_LeaveRoom(this ConnectionManager mgr, int this)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,8).Add(this).Send(mgr);
	}

	static public void RoomImpl_Reconnect(this ConnectionManager mgr, int p)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,12).Add(p).Send(mgr);
	}

	static public void RoomImpl_ApplyDismiss(this ConnectionManager mgr, int roomId)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,13).Add(roomId).Send(mgr);
	}

	static public void RoomImpl_SendPiao(this ConnectionManager mgr, int roomId, int type)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,29).Add(roomId).Add(type).Send(mgr);
	}

	static public void RoomImpl_KouCard(this ConnectionManager mgr, int roomId, int[] list)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,23).Add(roomId).Add(list).Send(mgr);
	}

	static public void RoomImpl_CreateRoom(this ConnectionManager mgr, int count, int need, int this, int[] p_user, int round, int[] fan)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,0).Add(count).Add(need).Add(this).Add(p_user).Add(round).Add(fan).Send(mgr);
	}

	static public void RoomImpl_GetList(this ConnectionManager mgr, int page)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,31).Add(page).Send(mgr);
	}

	static public void RoomImpl_AgreeDismiss(this ConnectionManager mgr, int roomId, int result)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,14).Add(roomId).Add(result).Send(mgr);
	}

	static public void RoomImpl_TestCard(this ConnectionManager mgr, int[] conf, int[] ci)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,17).Add(conf).Add(ci).Send(mgr);
	}

	static public void RoomImpl_TestJieSuan(this ConnectionManager mgr, int this, int p_user, int round, int[] fan, int wanfa, int[] list, int jifen, int list2, int zimo, int dianpao, int ytl, int qys, int qysytl, int hy, int mg)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,38).Add(this).Add(p_user).Add(round).Add(fan).Add(wanfa).Add(list).Add(jifen).Add(list2).Add(zimo).Add(dianpao).Add(ytl).Add(qys).Add(qysytl).Add(hy).Add(mg).Send(mgr);
	}

	static public void RoomImpl_SendChat(this ConnectionManager mgr, int roomId, int type, string id)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,19).Add(roomId).Add(type).Add(id).Send(mgr);
	}

	static public void RoomImpl_GetYou(this ConnectionManager mgr, int this, int p_user)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,35).Add(this).Add(p_user).Send(mgr);
	}

	static public void RoomImpl_DismissRoom(this ConnectionManager mgr, int roomId)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(1,15).Add(roomId).Send(mgr);
	}

	static public void ProxyImpl_GetUser(this ConnectionManager mgr, int tid)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(2,3).Add(tid).Send(mgr);
	}

	static public void ProxyImpl_AddScore(this ConnectionManager mgr, long roleId, int score)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(2,2).Add(roleId).Add(score).Send(mgr);
	}

	static public void ProxyImpl_GetAllUser(this ConnectionManager mgr, int page)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(2,1).Add(page).Send(mgr);
	}

	static public void ProxyImpl_GeChargeRecord(this ConnectionManager mgr, int page)
	{
		PacketBuffer.GetInstance().Clear().SetObjMethod(2,4).Add(page).Send(mgr);
	}

}
