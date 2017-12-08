package test.robot;

public class RFCFn
{
	public static void Login_Enter(Robot r, String p_username, String p_password, int p_nServerID, String p_deviceIdentifier, String p_deviceModel)
	{
		r.GetSendBuffer().Clear().AddID(0,0).Add(p_username).Add(p_password).Add(p_nServerID).Add(p_deviceIdentifier).Add(p_deviceModel).Send(r.GetLink());
	}

	public static void Login_Register(Robot r, String p_username, String p_password, int p_nServerID, String p_deviceIdentifier, String p_deviceModel)
	{
		r.GetSendBuffer().Clear().AddID(0,2).Add(p_username).Add(p_password).Add(p_nServerID).Add(p_deviceIdentifier).Add(p_deviceModel).Send(r.GetLink());
	}

	public static void Login_ThirdLogin(Robot r, String p_username, String p_nickname, String p_password, int p_nServerID, String p_headurl, String p_sex, String p_deviceIdentifier, String p_deviceModel)
	{
		r.GetSendBuffer().Clear().AddID(0,4).Add(p_username).Add(p_nickname).Add(p_password).Add(p_nServerID).Add(p_headurl).Add(p_sex).Add(p_deviceIdentifier).Add(p_deviceModel).Send(r.GetLink());
	}

	public static void CharacterImpl_RequestBaseInfo(Robot r)
	{
		r.GetSendBuffer().Clear().AddID(5,0).Send(r.GetLink());
	}

	public static void RoomImpl_cenceldis(Robot r, int roomId)
	{
		r.GetSendBuffer().Clear().AddID(1,41).Add(roomId).Send(r.GetLink());
	}

	public static void RoomImpl_AutoCard(Robot r, int p_user)
	{
		r.GetSendBuffer().Clear().AddID(1,40).Add(p_user).Send(r.GetLink());
	}

	public static void RoomImpl_EnterRoom(Robot r, int rm, String ru)
	{
		r.GetSendBuffer().Clear().AddID(1,1).Add(rm).Add(ru).Send(r.GetLink());
	}

	public static void RoomImpl_OperCard(Robot r, int ru, int[] cid)
	{
		r.GetSendBuffer().Clear().AddID(1,5).Add(ru).Add(cid).Send(r.GetLink());
	}

	public static void RoomImpl_InitCard(Robot r, int roomId, String ip)
	{
		r.GetSendBuffer().Clear().AddID(1,4).Add(roomId).Add(ip).Send(r.GetLink());
	}

	public static void RoomImpl_GameReady(Robot r, int roomId)
	{
		r.GetSendBuffer().Clear().AddID(1,6).Add(roomId).Send(r.GetLink());
	}

	public static void RoomImpl_LeaveRoom(Robot r, int this)
	{
		r.GetSendBuffer().Clear().AddID(1,8).Add(this).Send(r.GetLink());
	}

	public static void RoomImpl_Reconnect(Robot r, int p)
	{
		r.GetSendBuffer().Clear().AddID(1,12).Add(p).Send(r.GetLink());
	}

	public static void RoomImpl_ApplyDismiss(Robot r, int roomId)
	{
		r.GetSendBuffer().Clear().AddID(1,13).Add(roomId).Send(r.GetLink());
	}

	public static void RoomImpl_SendPiao(Robot r, int roomId, int type)
	{
		r.GetSendBuffer().Clear().AddID(1,29).Add(roomId).Add(type).Send(r.GetLink());
	}

	public static void RoomImpl_KouCard(Robot r, int roomId, int[] list)
	{
		r.GetSendBuffer().Clear().AddID(1,23).Add(roomId).Add(list).Send(r.GetLink());
	}

	public static void RoomImpl_CreateRoom(Robot r, int count, int need, int this, int[] p_user, int round, int[] fan)
	{
		r.GetSendBuffer().Clear().AddID(1,0).Add(count).Add(need).Add(this).Add(p_user).Add(round).Add(fan).Send(r.GetLink());
	}

	public static void RoomImpl_GetList(Robot r, int page)
	{
		r.GetSendBuffer().Clear().AddID(1,31).Add(page).Send(r.GetLink());
	}

	public static void RoomImpl_AgreeDismiss(Robot r, int roomId, int result)
	{
		r.GetSendBuffer().Clear().AddID(1,14).Add(roomId).Add(result).Send(r.GetLink());
	}

	public static void RoomImpl_TestCard(Robot r, int[] conf, int[] ci)
	{
		r.GetSendBuffer().Clear().AddID(1,17).Add(conf).Add(ci).Send(r.GetLink());
	}

	public static void RoomImpl_TestJieSuan(Robot r, int this, int p_user, int round, int[] fan, int wanfa, int[] list, int jifen, int list2, int zimo, int dianpao, int ytl, int qys, int qysytl, int hy, int mg)
	{
		r.GetSendBuffer().Clear().AddID(1,38).Add(this).Add(p_user).Add(round).Add(fan).Add(wanfa).Add(list).Add(jifen).Add(list2).Add(zimo).Add(dianpao).Add(ytl).Add(qys).Add(qysytl).Add(hy).Add(mg).Send(r.GetLink());
	}

	public static void RoomImpl_SendChat(Robot r, int roomId, int type, String id)
	{
		r.GetSendBuffer().Clear().AddID(1,19).Add(roomId).Add(type).Add(id).Send(r.GetLink());
	}

	public static void RoomImpl_GetYou(Robot r, int this, int p_user)
	{
		r.GetSendBuffer().Clear().AddID(1,35).Add(this).Add(p_user).Send(r.GetLink());
	}

	public static void RoomImpl_DismissRoom(Robot r, int roomId)
	{
		r.GetSendBuffer().Clear().AddID(1,15).Add(roomId).Send(r.GetLink());
	}

	public static void ProxyImpl_GetUser(Robot r, int tid)
	{
		r.GetSendBuffer().Clear().AddID(2,3).Add(tid).Send(r.GetLink());
	}

	public static void ProxyImpl_AddScore(Robot r, long roleId, int score)
	{
		r.GetSendBuffer().Clear().AddID(2,2).Add(roleId).Add(score).Send(r.GetLink());
	}

	public static void ProxyImpl_GetAllUser(Robot r, int page)
	{
		r.GetSendBuffer().Clear().AddID(2,1).Add(page).Send(r.GetLink());
	}

	public static void ProxyImpl_GeChargeRecord(Robot r, int page)
	{
		r.GetSendBuffer().Clear().AddID(2,4).Add(page).Send(r.GetLink());
	}

}
