package core;
/**
 * RootConfig.java 2012-6-28下午3:25:05
 */


import java.nio.channels.*;

import core.detail.*;

import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
@ExcelData( File = "RootConfig.xls", Table = "Sheet1" )
public class RootConfig
{
	private static RootConfig m_RootConfig = new RootConfig();
	private boolean m_Init = false;
	
	public static class ProtocolClassName
	{
		public String Name;
	}
	
	public boolean	Debug;//如果为false，那么以Debug开头的配置都将关闭		add by xiaoS	2012/15/5
	public boolean	Show;//是否显示界面								add by xiaoS	2012/15/5
	
	public boolean OpenSecurityService;
	
	public int		CheckDeadLink;
	
	public int		ServerPort;	//服务器监听的端口					add by xiaoS	2012/15/5
	public int		GMPort;//游戏管理员系统监听端口						add by xiaoS	2012/15/5
	
	public int		ServerUniqueID;//服务器唯一ID					add by xiaoS	2012/15/5
	public String 	DBIp;//数据库管理员的ip地址						add by xiaoS	2012/15/5
	public int		DBPort;//数据库管理员的端口						add by xiaoS	2012/15/5
	public String	DBName;//数据库管理员的名字						add by xiaoS	2012/15/5
	public String	DBUser;//数据库管理员的用户名						add by xiaoS	2012/15/5
	public String	DBPassword;//数据库管理员的密码					add by xiaoS	2012/15/5
	
	public int		UserMax;//用户的最多人数							add by xiaoS	2012/15/5
	public int		UserStartClear;//当用户人数达到一定数量就执行清理		add by xiaoS	2012/15/5
	public int		UserClearNum;//每次要清理的数量					add by xiaoS	2012/15/5
	public int		UserClearTime;//当数据达到指定时间就执行清理			add by xiaoS	2012/15/5
	
	public int		ProcessTempUserTm;
	public int		ProcessSaveDBStartTm;
	public int		ExecuteSaveDBSQLBusyRatio;
	public int		ExecuteSaveDBSpaceTm;
	public int		ExecuteSaveDBUserNum;
	public int		ExecuteSaveLogBusyRatio;
	public int		ExecuteSaveLogNum;
	
	public int		LinkLimit;
	public int		LinkMsgLimit;
	public int		LinkMaxWaitFirstMsgTime;
	public int		LinkEchoSpaceTime;
	
	public ProtocolClassName[] Protocol;
	
	public boolean	RobotService;
	
	public boolean	ShutdownHook;
	
	public int		SD_CloseServerCountdown;
	public int		SD_CommonThreadWaitTime;
	public int		SD_SQLThreadWaitTime;
	public int		SD_LogThreadWaitTime;
	
	public int		LimitSendMsgBuffer;
	public int		LimitSendMsgNum;
	
	public String	GMIP;
	public String	PayURL;
	public String	FirstPayAwardURL;
	public float	SpeedSpace;
	
	public boolean	UseTokenVerify;
	public boolean	UsePasswordVerify;
	
	private long	m_StartTime = System.currentTimeMillis();
	private String[] m_GMAllowsIP = null;

	private RootConfig()
	{
		
	}
	
	public static RootConfig GetInstance()
	{
		return m_RootConfig;
	}
	
	public void Init()
	{
		if ( !m_Init )
		{
			ExcelIniReader.Read(m_RootConfig);
			m_Init = true;
			
			m_GMAllowsIP = GMIP.split(";");
		}
	}
	
	/**
	 * 是否达到连接上限
	 *
	 * @param p_nCurrLinkNum 当前的连接数量
	 * @return true表示达到上限了
	 */
	public boolean IsLinkLimit(int p_nCurrLinkNum)
	{
		if ( LinkLimit <= 0 )
		{
			return false;
		}
		return p_nCurrLinkNum > LinkLimit;
	}
	
	public boolean CanStartSaveDBTask()
	{
		return System.currentTimeMillis() > m_StartTime + ProcessSaveDBStartTm * 1000;
	}

	public boolean CheckGMIPLimit(SocketChannel c)
	{
		if ( m_GMAllowsIP == null )
		{
			return true;
		}
		String cip = SystemFn.GetIP(c);
		for ( String ip : m_GMAllowsIP )
		{
			if (ip.equals(cip))
			{
				return true;
			}
		}
		return false;
	}
}
