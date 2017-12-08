package test.robot;

public class RFCFnTest
{
	public static void Login_Log(Robot r, String p_sLog)
	{
		r.GetSendBuffer().Clear().AddID(0,61).Add(p_sLog).Send(r.GetLink());
	}

	public static void Login_SQLLog(Robot r)
	{
		r.GetSendBuffer().Clear().AddID(0,62).Send(r.GetLink());
	}

	public static void Login_Binary(Robot r, String p_binary)
	{
		r.GetSendBuffer().Clear().AddID(0,65).Add(p_binary).Send(r.GetLink());
	}

	public static void Login_CreateWebRole(Robot r, String p_username, String p_password, int p_nAdult, int p_nServerID, String p_RoleName, int p_TemplateID)
	{
		r.GetSendBuffer().Clear().AddID(0,64).Add(p_username).Add(p_password).Add(p_nAdult).Add(p_nServerID).Add(p_RoleName).Add(p_TemplateID).Send(r.GetLink());
	}

	public static void Login_Echo(Robot r, int p_nHashcode, String p_sInfo)
	{
		r.GetSendBuffer().Clear().AddID(0,57).Add(p_nHashcode).Add(p_sInfo).Send(r.GetLink());
	}

	public static void Login_EchoReceive(Robot r, int p_nHashcode, String p_sInfo)
	{
		r.GetSendBuffer().Clear().AddID(0,55).Add(p_nHashcode).Add(p_sInfo).Send(r.GetLink());
	}

	public static void Login_EchoSend(Robot r, int p_nHashcode, int p_nLength)
	{
		r.GetSendBuffer().Clear().AddID(0,56).Add(p_nHashcode).Add(p_nLength).Send(r.GetLink());
	}

	public static void Login_RunEmptySQL(Robot r, int p_nHashcode, int p_nNum)
	{
		r.GetSendBuffer().Clear().AddID(0,58).Add(p_nHashcode).Add(p_nNum).Send(r.GetLink());
	}

	public static void Login_LoadByUserName(Robot r, String p_user1, String psd)
	{
		r.GetSendBuffer().Clear().AddID(0,63).Add(p_user1).Add(psd).Send(r.GetLink());
	}

}
