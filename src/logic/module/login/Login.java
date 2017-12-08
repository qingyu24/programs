package logic.module.login;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import core.Root;
import core.RootConfig;
import core.Tick;
import core.User;
import core.detail.Mgr;
import core.detail.impl.log.Log;
import core.remote.PI;
import core.remote.PS;
import core.remote.PU;
import core.remote.RFC;
import logic.MyUser;
import logic.PackBuffer;
import logic.Reg;
import logic.eLogicCloseUserReason;
import logic.config.IConfig;
import logic.config.ServerCommonConfig;
import logic.methodex.EmptyMethod;
import logic.methodex.LoginMethod;
import logic.module.log.eLogicInfoLogType;
import logic.module.log.eLogicSQLLogType;
import logic.module.log.sql.eLoginDebugLogType;
import logic.module.login.LoginRecord.LoginState;
import logic.sqlrun.LoginSQLRun;
import manager.ConfigManager;
import utility.Debug;
import utility.LegalityCheck;
import utility.Pair;
import utility.UniqueID;
/**
 * @author niuhao
 * @version 1.0.0
 *
 */
public class Login implements LoginInterface, Tick
{
	private static final int m_WaitLoginTime = 60 * 1000;
	private static Login m_Instance = new Login();
	private static Map<String,LoginRecord> m_All = new HashMap<String,LoginRecord>();
	private static Set<String>	m_Forbids = new HashSet<String>();
	private static byte[] m_EchoSendBytes = new byte[10240];
	
	public static Login GetInstance()
	{
		return m_Instance;
	}
	private int	m_CurrUserNum = 0;
	private int m_MaxUserNum = 1000;
	
	private int m_CurrLinkNum = 0;
	private int m_MaxLinkNum = 1000;
	
	private static UniqueID tokenGenerator = new UniqueID();

	private Queue<String> m_LoginWait = new LinkedList<String>();
	private Map<String,LoginMethod> m_WaitSQLRuns = new HashMap<String, LoginMethod>();
	private Queue<String> m_Logining = new LinkedList<String>();
	
	// 登录accesstoken的缓存表，由验证服务器调用login模块的生成accesstoken，然后客户端以accesstoken发送登录请求
	// 生成accesstoken时token写入此缓存表
	// TODO 用户断线一段时间之后，要从此表中删除token，之所以要保留一段时间，是为了客户端的断线重连
	private Map<Integer, Map<String, Pair<String, Integer>>> loginTokenCacheTable = new HashMap<Integer, Map<String, Pair<String, Integer>>>();
	
	private long m_NoticeWaitTime = 0;
	
	Login()
	{
		Root.GetInstance().AddLoopMilliTimer(this, 200, null);
	}

	
	/* 
	 * 主要登录逻辑是在LoginMethod和LoginSQLRun，这里只是做有效性检测以及登录队列的处理
	 */
	@Override
	public void Enter(MyUser p_user, String p_username, String p_password, int p_nServerID, String p_deviceIdentifier, String p_deviceModel)
	{
		InternalLogin(p_user, p_username, p_password, p_nServerID, p_deviceIdentifier, p_deviceModel, 0, false, "", "", "");
	}

	
	
	private boolean Validate(MyUser p_user, 
			String p_username,
			String p_password, 
			int p_nServerID){
		
		if ( _CheckServerFull() )
		{
			_SToC_LoginReturn(p_user,eLoginErrorCode.USER_FULL.ID(), p_username);
			return false;
		}
		// 检查当前网络连接数量是否已经到上限
		if ( _CheckLinkFull() )
		{
			_SToC_LoginReturn(p_user,eLoginErrorCode.LINK_FULL.ID(), p_username);
			return false;
		}
		// 检查登录队列是否已经满了
		if ( _CheckWaitLoginQueueFull() )
		{
			_SToC_LoginReturn(p_user,eLoginErrorCode.LOGIN_QUEUE_FULL.ID(), p_username);
			return false;
		}
		// 检查登录队列里是否有重名（同一个人多次申请登录）
		if ( _CheckQueueSameUserName(p_username) )
		{
			_SToC_LoginReturn(p_user,eLoginErrorCode.USERNAME_SAME.ID(), p_username);
			return false;
		}
		// 检查用户名是否为空
		int lengthLegal = _CheckUserNameLength(p_username, 16);
		if(-1 == lengthLegal)
		{
			_SToC_LoginReturn(p_user,eLoginErrorCode.USERNAMM_EMPTY.ID(), p_username);
			return false;
		}
		// 设置用户名，密码，服务器ID，为后续的查询和检测做准备
		p_user.SetUserName(p_username);
		p_user.SetPassword(p_password);
		p_user.SetServerID((short)p_nServerID);
		//AdultFlag.GetInstance().SetFlag(p_user, p_nAdult);		// 是否检测防沉迷
		
		eLoginDebugLogType.RECEIVE.Log(p_user);
		
		// 如果相同用户名的用户已经在服务器上有登录记录，那么要做一次检测，看看是用户重复登录，还是异地登录，还是登录失败的重新登录
		// 对于重复登录，异地顶替登录以及其他未记录的情况，就直接返回不做后续登录的处理了
		if ( m_All.containsKey(p_user.GetServerUserName()) )
		{
			if ( !CheckLoginRecord(m_All.get(p_user.GetServerUserName()), p_user, p_username, p_password) )
			{
				return false;
			}
		}
		
		eLoginDebugLogType.ADD_RECORD.Log(p_user);
		// 新增/更新 用户登录记录
		m_All.put(p_user.GetServerUserName(), new LoginRecord(p_user.hashCode())); 
		return true;
	}
	/* 
	 * 主要登录逻辑是在LoginMethod和LoginSQLRun，这里只是做有效性检测以及登录队列的处理
	 */
	private void InternalLogin(MyUser p_user, String p_username, String p_password, 
			int p_nServerID, String deviceIdentifier, String deviceModel, int code, boolean isRegister, 
			String token, String headurl, String sex)
	{
		Log.out.Log(eLogicInfoLogType.LOGIC_COMMON, "Login#Enter:" + p_username + "," + p_nServerID + "," + Debug.GetCurrTime());
		// 检查服务器是否已经满员
		
		if(!this.Validate(p_user, p_username, p_password, p_nServerID)){
			return;
		}
		// 判断用户是否已经被封禁
		boolean forbid = m_Forbids.contains(p_username);
		// 创建登录实例，加入队列，用户名加入登录等候队列，等待服务器登录处理完成
		String p_token = "";
		if(!token.equals("")){
			p_token = token;
		}
		if(RootConfig.GetInstance().UsePasswordVerify)
		{
			m_WaitSQLRuns.put(p_username, new LoginMethod(p_user,new LoginSQLRun(
					p_user,
					p_username,
					p_token,
					p_password,
					"servertoken",
					forbid,
					p_nServerID,
					deviceIdentifier,
					deviceModel,
					isRegister,
					headurl,
					sex
					)));
		}
		
		boolean c = m_LoginWait.add(p_username);
		Log.out.Log(eLogicInfoLogType.LOGIC_COMMON, "Login#Enter#AddLoginWait:" + p_username + "," + p_nServerID + "," + Debug.GetCurrTime() + ",result:" + c + ",size:" + m_LoginWait.size());
		// 通知客户端前头还有多少人在等待登录
		PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN, 10).Add(m_LoginWait.size()).Send(p_user);
	}
	
	private void InternalRegister(MyUser p_user, String p_username,
			String p_password,
			int p_nServerID, String p_deviceIdentifier,
			String p_deviceModel)
	{
		Log.out.Log(eLogicInfoLogType.LOGIC_COMMON, "Login#Register:" + p_username + "," + p_nServerID + "," + Debug.GetCurrTime());
		// 检查服务器是否已经满员
		
		if(!this.Validate(p_user, p_username, p_password, p_nServerID)){
			return;
		}
		// 判断用户是否已经被封禁
		boolean forbid = m_Forbids.contains(p_username);
		// 创建登录实例，加入队列，用户名加入登录等候队列，等待服务器登录处理完成
		String p_token = "";
		m_WaitSQLRuns.put(p_username, new LoginMethod(p_user,new LoginSQLRun(
				p_user,
				p_username,
				p_token,
				p_password,
				"servertoken",
				forbid,
				p_nServerID,
				p_deviceIdentifier,
				p_deviceModel,
				true,"","")));
		
		boolean c = m_LoginWait.add(p_username);
		Log.out.Log(eLogicInfoLogType.LOGIC_COMMON, "Login#Enter#AddLoginWait:" + p_username + "," + p_nServerID + "," + Debug.GetCurrTime() + ",result:" + c + ",size:" + m_LoginWait.size());
		// 通知客户端前头还有多少人在等待登录
		PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN, 10).Add(m_LoginWait.size()).Send(p_user);
	}

	/* (non-Javadoc)
	 * @see logic.module.login.LoginInterface#Binary(logic.MyUser, java.lang.String)
	 */
	@Override
	public void Binary(MyUser p_user, String p_binary)
	{
		System.err.println(Arrays.toString(p_binary.getBytes()));
	}
	
	/* (non-Javadoc)
	 * @see logic.module.login.LoginInterface#CreateWebRole(logic.MyUser, java.lang.String, java.lang.String, int, int, java.lang.String, int)
	 */
	@Override
	public void CreateWebRole(MyUser p_user, String p_username, String p_password, int p_nAdult, int p_nServerID, String p_RoleName, int p_TemplateID)
	{
		//todo by niuhao;
	} 
	
	/* (non-Javadoc)
	 * @see logic.module.login.LoginInterface#Echo(logic.MyUser, int, java.lang.String)
	 */
	@Override
	public void Echo(MyUser p_user, int p_nHashcode, String p_sInfo)
	{
		PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN, MID_ECHO).Add(p_nHashcode).Add(p_sInfo).Send(p_user);
	}
	
	/* (non-Javadoc)
	 * @see logic.module.login.LoginInterface#EchoReceive(logic.MyUser, int, java.lang.String)
	 */
	@Override
	public void EchoReceive(MyUser p_user, int p_nHashcode, String p_sInfo)
	{
		PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN, MID_ECHORECEIVE).Add(p_nHashcode).Send(p_user);
	}
	
	/* (non-Javadoc)
	 * @see logic.module.login.LoginInterface#EchoSend(logic.MyUser, int, int)
	 */
	@Override
	public void EchoSend(MyUser p_user, int p_nHashcode, int p_nLength)
	{
		PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN, MID_ECHOSEND).Add(p_nHashcode).Add(new String(m_EchoSendBytes, 0, p_nLength)).Send(p_user);
	}

	/* (non-Javadoc)
	 * @see logic.module.login.LoginInterface#LoadByUserName(logic.MyUser, logic.MyUser)
	 */
	@Override
	public void LoadByUserName(MyUser p_user, MyUser p_user1)
	{
		System.err.println("LoadByUserName#" + p_user1);
	}
	
	@Override
	public void Log(MyUser p_user, String p_sLog) 
	{
		p_user.Log(eLogicInfoLogType.LOGIC_COMMON, p_sLog);
	}
	
	public void OnDisconnect(MyUser p_User)
	{
		String username = p_User.GetServerUserName();
		if ( username == null )
		{
			eLoginDebugLogType.LOGIN_DIS_ERR_FIND_USERNAME.Log(p_User);
			return;
		}
		
		m_Logining.remove(username);
		
		if ( m_All.containsKey(username) )
		{
			LoginRecord r = m_All.get(username);
			System.out.println(r);
			if ( r.GetHashCode() == p_User.hashCode() )
			{
//				System.out.println("* Login#OnDisconnect 删除:" + p_User.GetUserName() + "登陆数据");
				eLoginDebugLogType.LOGIN_DIS_OK.Log(p_User);
				m_All.remove(username);
			}
			else
			{
				eLoginDebugLogType.LOGIN_DIS_ERR_CHANGE_RECORD.Log(p_User);
			}
		}
		else
		{
			eLoginDebugLogType.LOGIN_DIS_ERR_FIND_RECORD.Log(p_User);
		}
	}
	
	public void OnLinkNumChange(int currnum, int maxnum)
	{
		m_CurrLinkNum = currnum;
		m_MaxLinkNum = maxnum;
	}
	
	public void OnLinkProcessMsgTooMuch(User p_User)
	{
		if (p_User == null || p_User.GetUserName() == null)
		{
			return;
		}
		m_Forbids.add(p_User.GetUserName());
	}

	/* (non-Javadoc)
	 * @see core.Tick#OnTick(long)
	 */
	@Override
	public void OnTick(long p_lTimerID) throws Exception
	{
		// 更新通知客户端前头等候登录的人数变化
		_NoticeWaitTime();
		
		if ( Mgr.GetSqlMgr().BusyRatio() > 150 )
		{
			Log.out.Log(eLogicInfoLogType.LOGIC_COMMON, "sql的执行超时。。");
			return;
		}
		IConfig mc = ConfigManager.getInstance().getConfig("ServerCommonConfig");
		ServerCommonConfig scc = (ServerCommonConfig)mc;
		
		if ( m_Logining.size() > scc.LoginMaxWaitNum )
		{
			Log.out.Log(eLogicInfoLogType.LOGIC_COMMON, "Login#OnTick#MaxWaitNum.....");
			return;
		}
		
		int max = 5;
		// 把登录等待队列里的登录请求导入到登录队列里去（两个队列：登录等待队列和登录队列，登录队列里是服务器逻辑线程一次最多可以批量处理的登录请求
		// 而登录等待队列里是当前总共发起的登录请求，两个队列分别有自己的数量上限，在服务器配置里）
		while ( m_Logining.size() < scc.LoginMaxWaitNum && max > 0 )
		{
			if ( m_LoginWait.isEmpty() )
			{
				
				break;
			}
			// 从登录等待队列里获取到用户名
			String username = m_LoginWait.poll();
			Log.out.Log(eLogicInfoLogType.LOGIC_COMMON, "Login#OnTick#LoginWaitPoll:" + username + "," + Debug.GetCurrTime() + ",result:");
			// 依照用户名得到登录实例
			LoginMethod m = m_WaitSQLRuns.get(username);
			m_WaitSQLRuns.remove(username);
			if ( m == null )
			{
				continue;
			}
			User user = m.GetUser();
			if ( user == null )
			{
				continue;
			}
			if ( user.IsDisabled() || user.GetState().GetDisconnect() )
			{
				continue;
			}
			// 导入到登录队列里
			m_Logining.add(username);
			_SynLoginWaitNum(0, m.GetUser());
			// 登录实例交给服务器逻辑线程去执行登录流程
			Root.GetInstance().AddSQLRun(m);
			
//			Log.out.Log(eLogicInfoLogType.LOGIC_COMMON, "* Login#EnterLoginInfo:" + m.GetUser() + ", loginingSize:" + m_Logining.size() + ", loginWaitSize:" + m_LoginWait.size());
		}
	}
	
	public void OnUserNumChange(int currnum, int maxnum)
	{
		m_CurrUserNum = currnum;
		m_MaxUserNum = maxnum;
	}
	
	public void RemoveLoginInfo(MyUser p_user)
	{
		if ( p_user == null )
		{
			return;
		}
		String p_username = p_user.GetUserName();
		if ( p_username == null )
		{
			return;
		}
		m_Logining.remove(p_username);
		Log.out.Log(eLogicInfoLogType.LOGIC_COMMON, "* Login#RemoveLoginInfo:" + p_user);
	}

	/* (non-Javadoc)
	 * @see logic.module.login.LoginInterface#RunEmptySQL(logic.MyUser, int)
	 */
	@Override
	public void RunEmptySQL(MyUser p_user, int p_nHashcode, int p_nNum)
	{
		Root.GetInstance().AddSQLRun(new EmptyMethod(p_user, p_nHashcode, p_nNum));
	}

	/**
	 * 设置用户在创建角色,如果无法设置则会断开用户
	 */
	public void SetLoginCreateRole(MyUser p_user)
	{
		LoginRecord r = m_All.get(p_user.GetServerUserName());
		if ( r == null )
		{
			System.out.println("* SetLoginCreateRole 没有:"+p_user.GetUserName()+"的登陆数据...");
			p_user.Close(eLogicCloseUserReason.UPDATE_LOGINDATA_CREATEROLE_NOT_FIND.ID(), 0);
			return;
		}
		if ( r.GetHashCode() != p_user.hashCode() )
		{
			System.out.println("* SetLoginCreateRole 有:"+p_user.GetUserName()+"的登陆数据,但是不是自己:"+p_user.hashCode() + ",而是" + r.GetHashCode());
			p_user.Close(eLogicCloseUserReason.UPDATE_LOGINDATA_CREATEROLE_CHANGED.ID(), 0);
			return;
		}
		r.SetState(LoginState.LOGIN_CREATE);
	}

	/**
	 * 设置用户完成创建角色,如果无法设置则会断开用户
	 */
	public void SetLoginCreateRoleFinish(MyUser p_user)
	{
		LoginRecord r = m_All.get(p_user.GetServerUserName());
		if ( r == null )
		{
			System.out.println("* SetLoginCreateRoleFinish 没有:"+p_user.GetUserName()+"的登陆数据...");
			p_user.Close(eLogicCloseUserReason.UPDATE_LOGINDATA_CREATEROLE_FINISH_NOT_FIND.ID(), 0);
			return;
		}
		if ( r.GetHashCode() != p_user.hashCode() )
		{
			System.out.println("* SetLoginCreateRoleFinish 有:"+p_user.GetUserName()+"的登陆数据,但是不是自己:"+p_user.hashCode() + ",而是" + r.GetHashCode());
			p_user.Close(eLogicCloseUserReason.UPDATE_LOGINDATA_CREATEROLE_FINISH_CHANGED.ID(), 0);
			return;
		}
		r.SetState(LoginState.LOGIN_CREATEFINISH);
	}

	/**
	 * 设置某个用户登陆失败,会导致断开这个用户的连接
	 */
	public void SetLoginFail(MyUser p_user)
	{
		LoginRecord r = m_All.get(p_user.GetServerUserName());
		if ( r == null )
		{
			System.out.println("* SetLoginFail 没有:"+p_user.GetUserName()+"的登陆数据...");
			eLoginDebugLogType.LOGIN_FAIL_ERR_FIND_RECORD.Log(p_user);
		}
		else
		{
			if ( r.GetHashCode() != p_user.hashCode() )
			{
				System.out.println("* SetLoginFail 有:"+p_user.GetUserName()+"的登陆数据,但是不是自己:"+p_user.hashCode() + ",而是" + r.GetHashCode());
				eLoginDebugLogType.LOGIN_FAIL_ERR_CHANGE_RECORD.Log(p_user);
			}
			else
			{
				eLoginDebugLogType.LOGIN_FAIL_SET.Log(p_user);
				r.SetState(LoginState.LOGINED_FAIL);
			}
		}
		//p_user.Close(eLogicCloseUserReason.UPDATE_SET_LOGINDATA_FAIL.ID(), 0);
	}

	/**
	 * 设置某个用户登陆成功
	 * 
	 * @return 返回true表示登陆检查没问题
	 */
	public boolean SetLoginSuccess(MyUser p_user)
	{
		LoginRecord r = m_All.get(p_user.GetServerUserName());
		if ( r == null )
		{
			System.out.println("* SetLoginSuccess 没有:"+p_user.GetUserName()+"的登陆数据...");
			eLoginDebugLogType.LOGIN_OK_ERR_NO_RECORD.Log(p_user);
			return false;
		}
		if ( r.GetHashCode() != p_user.hashCode() )
		{
			System.out.println("* SetLoginSuccess 有:"+p_user.GetUserName()+"的登陆数据,但是不是自己:"+p_user.hashCode() + ",而是" + r.GetHashCode());
			eLoginDebugLogType.LOGIN_OK_ERR_CHANGE_RECORD.Log(p_user);
			return false;
		}
		if ( r.GetLoginUseTime() > m_WaitLoginTime )
		{
			System.out.println(" SetLoginSuccess 虽然登陆成功了,但是超时了,现在是:" + Debug.GetShowTime(System.currentTimeMillis()) + ",登陆是:" + Debug.GetShowTime(r.GetLoginTime()));
			eLoginDebugLogType.LOGIN_OK_ERR_TOOLONG.Log(p_user);
			return false;
		}
		eLoginDebugLogType.LOGIN_OK_OK.Log(p_user);
		r.SetState(LoginState.LOGINED_SUCCESS);
		return true;
	}	
	
	@Override
	public void SQLLog(MyUser p_user) 
	{
		p_user.Log(eLogicSQLLogType.LOGIC_SQL_LOGIN,
				RootConfig.GetInstance().ServerUniqueID,
				eLogicSQLLogType.GetCurrTime(),
				0,
				"",
				p_user.GetLink().GetIP(),
				"",
				"",
				9527);
	}
	
	public String GenerateAccessToken(String username, int serverID, int channelID)
	{
		Long tokenID = tokenGenerator.Get();
		String tokenIDString = tokenID.toString();
		Map<String, Pair<String, Integer>> tmpTable = null;
		if(loginTokenCacheTable.containsKey(serverID))
			tmpTable = loginTokenCacheTable.get(serverID);
		else
		{
			tmpTable = new HashMap<String, Pair<String, Integer>>();
			loginTokenCacheTable.put(serverID, tmpTable);
		}
		tmpTable.put(username, Pair.makePair(tokenIDString, channelID));
		return tokenIDString;
	}
	
	public void CacheAccessToken(String username, int serverID, int channelID, String token)
	{
		Map<String, Pair<String, Integer>> tmpTable = null;
		if(loginTokenCacheTable.containsKey(serverID))
			tmpTable = loginTokenCacheTable.get(serverID);
		else
		{
			tmpTable = new HashMap<String, Pair<String, Integer>>();
			loginTokenCacheTable.put(serverID, tmpTable);
		}
		tmpTable.put(username, Pair.makePair(token, channelID));
	}

	private boolean _CheckLinkFull()
	{
		return m_MaxLinkNum == 0 ? false : m_CurrLinkNum * 10 > m_MaxLinkNum * 9;
	}

	private boolean _CheckQueueSameUserName(String p_username)
	{
		return m_LoginWait.contains(p_username);
	}
	
	private boolean _CheckServerFull()
	{
		return m_CurrUserNum * 10 > m_MaxUserNum * 9;
	}

	// 检测名字的长度合法性(maxsz个字符以内,一个汉字按两个字符算)
	private int _CheckUserNameLength(String p_Rolename, int maxsz)
	{
		return LegalityCheck.CheckStrLengthLegal(p_Rolename, maxsz);
	}

	private boolean _CheckWaitLoginQueueFull()
	{
		IConfig mc = ConfigManager.getInstance().getConfig("ServerCommonConfig");
		ServerCommonConfig scc = (ServerCommonConfig)mc;
		return m_LoginWait.size() >= scc.LoginQueueMaxNum;
	}

	private boolean _KillOpposite(MyUser p_User)
	{
//		Debug.Assert(p_User != null, "");
		if ( p_User != null )
		{
			PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN,0).Add(eLoginErrorCode.OPPO_KILLED.ID()).Send(p_User);
			p_User.Close(eLogicCloseUserReason.LOGIN_KILL_OPPOSITE.ID(), 0);
		}
		return true;
	}

	private void _NoticeWaitTime()
	{
		if ( System.currentTimeMillis() < m_NoticeWaitTime )
		{
			return;
		}
		m_NoticeWaitTime = System.currentTimeMillis() + 5000;
		
		int num = 0;
		Iterator<String> it = m_LoginWait.iterator();
		while (it.hasNext())
		{
			String username = it.next();
			LoginMethod m = m_WaitSQLRuns.get(username);
			if ( m == null )
			{
				continue;
			}
			_SynLoginWaitNum(++num, m.GetUser());
		}
	}

	private void _SToC_CreateReturn(MyUser p_user, int i)
	{
		PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN,4).Add(i).Send(p_user);		
	}

	private void _SToC_LoginReturn(MyUser p_user, int errid, String username)
	{
		PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN,0).Add(errid).Send(p_user);	
		
		p_user.Log(eLogicSQLLogType.LOGIC_SQL_LOGIN, 
				RootConfig.GetInstance().ServerUniqueID,
				eLogicSQLLogType.GetCurrTime(),
				0,
				username,
				p_user.GetLink().GetIP(),
				"",
				"",
				errid);
		
		p_user.Close(eLogicCloseUserReason.LOGIN_RETURN_FAILID.ID(), errid);
	}

	private void _SynLoginWaitNum(int num, User p_user)
	{
		PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN, 10).Add(num).Send(p_user);
	}

	private boolean CheckCanCreateRole(MyUser p_user)
	{
		if ( !p_user.UserNameReady() )
		{
			return false;
		}
		if ( !m_All.containsKey(p_user.GetServerUserName()) )
		{
			return false;
		}
		LoginRecord r = m_All.get(p_user.GetServerUserName());
		if ( r == null )
		{
			return false;
		}
		if ( r.GetHashCode() != p_user.hashCode() )
		{
			return false;
		}
		return true;
	}

	private boolean CheckLoginRecord(LoginRecord r, MyUser p_user, String p_username, String p_password)
	{
		if ( r.GetState() == LoginState.LOGINING 		|| 
			 r.GetState() == LoginState.LOGIN_CREATE 	||
			 r.GetState() == LoginState.LOGIN_CREATEFINISH )
		{
			if ( r.GetLoginUseTime() > m_WaitLoginTime )
			{
				eLoginDebugLogType.REPLACE.Log(p_user);
				//TODO 实际情况是在某些情况下客户端断开,服务器并不清楚.需要在连接那添加一个不断用来ping的包来测试断开问题.这样就导致这个号重新上的时候还能发现登陆数据
				return _KillOpposite((MyUser) Root.GetInstance().GetUserByUserName(p_username));
//				eLoginDebugLogType.ERR_TOOLONG.Log(p_user);
//				System.out.println("* 前面的用户已经登陆超过:"+m_WaitLoginTime+"毫秒,用户" + r.GetHashCode() + ",登陆用时间为," + r.GetLoginUseTime() + ",不应该出现这种情况,如果出现了应该是系统出问题了,也先忽略这个用户登陆,高级别警告");
//				return _KillSelf(p_user);
			}
			else
			{
				if ( r.GetHashCode() == p_user.hashCode() )
				{
					eLoginDebugLogType.REPEAT.Log(p_user);
					System.out.println("* 用户重复发送登陆消息,忽略");
					return false;
				}
				else
				{
					eLoginDebugLogType.REPLACE.Log(p_user);
					System.out.println(" 另外一个用户:" + p_user.hashCode() + " 想挤掉用户:" + r.GetHashCode() + ",正在执行!!!");
					return _KillOpposite((MyUser) Root.GetInstance().GetUserByUserName(p_username));
				}
			}
		}
		
		if ( r.GetState() == LoginState.LOGINED_SUCCESS )
		{
			if ( r.GetHashCode() == p_user.hashCode() )
			{
				eLoginDebugLogType.REPEAT.Log(p_user);
				System.out.println("* 用户重复发送登陆消息,忽略");
				return false;
			}
			else
			{
				eLoginDebugLogType.REPLACE.Log(p_user);
				System.out.println(" 另外一个用户:" + p_user.hashCode() + " 想挤掉用户:" + r.GetHashCode() + ",正在执行!!!");
				return _KillOpposite((MyUser) Root.GetInstance().GetUserByUserName(p_username));
			}
		}
		
		if ( r.GetState() == LoginState.LOGINED_FAIL )
		{
			eLoginDebugLogType.LOGIN_SAME_FAILED.Log(p_user);
			r.Init(p_user.hashCode());
			return true;
		}
		
		Debug.Assert(false, ""); ///<没有写的其他状态?
		return false;
	}


	@Override
	@RFC(ID = 2, RunDirect = true)
	public void Register(@PU MyUser p_user, @PS String p_username,
			@PS String p_password, 
			@PI int p_nServerID, @PS String p_deviceIdentifier,
			@PS String p_deviceModel) {
		// TODO Auto-generated method stub
		InternalRegister(p_user, p_username,
				p_password,
				p_nServerID, p_deviceIdentifier,
				p_deviceModel);
	}


	@Override
	@RFC(ID = 4, RunDirect = true)
	public void ThirdLogin(@PU MyUser p_user, @PS String p_username,
			@PS String p_nickname, @PS String p_password, @PI int p_nServerID,
			@PS String p_headurl, @PS String p_sex,
			@PS String p_deviceIdentifier, @PS String p_deviceModel) {
		// TODO Auto-generated method stub
		InternalLogin(p_user, p_nickname, p_password, p_nServerID, p_deviceIdentifier, p_deviceModel, 0, false, p_username, p_headurl, p_sex);

	}


	


	

}
