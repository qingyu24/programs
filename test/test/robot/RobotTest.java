/**
 * RobotTest.java 2013-2-17下午4:49:30
 */
package test.robot;

import test.robot.step.*;
import test.robot.utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RobotTest
{
	public static void main(String[] args)
	{
		if (args.length < 9)
		{
			System.out.println("参数太少 [ip] [port] [robotnum] [runnum] [stepnum] [type] [robottype] [runloopwaittm] [maxwaittime]");
			System.out.println("type:1	上线并在城市移动\n" +
							   "type:2 	纯登陆" +
							   "type:3 世界聊天" +
							   "type:4 完成所有任务" +
							   "type:5 日志" +
							   "type:6 SQL日志" +
							   "\n");
			System.out.println("robotytype: 0:increaseRule 1:normalRule 2:randomRule 3:solidRule");
			return;
		}
		
		String ip = args.length < 1 ? "192.168.3.10" : args[0];
		int port = args.length < 2 ? 9527 : Integer.parseInt(args[1]);
		int robotnum = args.length < 3 ? 1 : Integer.parseInt(args[2]);
		int runnum = args.length < 4 ? 100 : Integer.parseInt(args[3]);
		int stepnum = args.length < 5 ? 10 : Integer.parseInt(args[4]);
		int type = args.length < 6 ? 3 : Integer.parseInt(args[5]);
		int robottype = args.length < 7 ? 1 : Integer.parseInt(args[6]);
		int runloopwaittm = args.length < 8 ? 5000 : Integer.parseInt(args[7]);
		int maxwaittime = args.length < 9 ? 120 * 1000 : Integer.parseInt(args[8]);
		
		RobotConfig.Init();
		
		RobotConfig.Host = ip;
		RobotConfig.Port = port;
		
		NameRule rule = _GetRobotNameRule(robottype);
		TimeBatchRun r = new TimeBatchRun(20*60*60*1000, robotnum, maxwaittime, rule, rule);
//		FreeRun r = new FreeRun(2*60*60*1000, robotnum, rule, rule);
		r.SetMaxRunNum(runnum);
		if (runloopwaittm > 0 )
		{
			r.SetRunWaitTime(runloopwaittm);
		}
		
		Step s = GetStep(type, stepnum);
		r.SetStep(s);
		r.run();
	}
	
	private static NameRule _GetRobotNameRule(int type)
	{
		switch (type)
		{
		case 0:	return NameRule.increaseRule;
		case 1: return NameRule.normalRule;
		case 2: return NameRule.randomRule;
		case 3: return NameRule.solidRule;
		default:return NameRule.randomRule;
		}
	}
	
	public static Step GetStep(int type, int stepnum)
	{
		switch (type)
		{
		case 1:return RFAutoMoveStep(stepnum);
		case 2:return RFLoginStep();
		case 3:return RFChatStep(stepnum);
		case 4:return RFAutoAllTask();
		case 5:return RFLog(stepnum);
		case 6:return RFSQLLog(stepnum);
		default:
			return null;
		}
	}
	
	public static Step RFAutoMoveStep(int num)
	{
		OrderStep os = new OrderStep();
		os.Add(new LoginNormalStep());
		os.Add(new AutoCreateRoleStep());
		os.Add(new CheckReceiveEnterWorldStep());
		os.Add(new AutoMoveStep(num));
		return os;
	}
	
	public static Step RFLoginStep()
	{
		return new LoginNormalStep();
	}
	
	public static Step RFChatStep(int num)
	{
		OrderStep os = new OrderStep();
		os.Add(new LoginNormalStep());
		os.Add(new AutoCreateRoleStep());
		os.Add(new CheckReceiveEnterWorldStep());
		os.Add(new NoticeAllStep(num));
		return os;
	}
	
	public static Step RFAutoAllTask()
	{
		OrderStep os = new OrderStep();
		os.Add(new LoginNormalStep());
		os.Add(new AutoCreateRoleStep());
		os.Add(new CheckReceiveEnterWorldStep());
		os.Add(new AutoFinishAllTaskStep());
		return os;
	}
	
	public static Step RFLog(int num)
	{
		return new LogStep(num);
	}
	
	public static Step RFSQLLog(int num)
	{
		return new SQLLogStep(num);
	}
}
