/**
 * LoginStep.java 2013-2-5下午2:12:33
 */
package test.robot.module.login;

import test.robot.RobotConfig;
import test.robot.TimeBatchRun;
import test.robot.step.AutoCreateRoleStep;
import test.robot.step.AutoMoveStep;
import test.robot.step.CheckReceiveEnterWorldStep;
import test.robot.step.LoginNormalStep;
import test.robot.step.OrderStep;
import test.robot.step.Step;
import test.robot.utility.IncreaseRule;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LoginStep
{

	public static void main(String[] args)
	{
		if (args.length != 4)
		{
			System.out.println("参数错误,需要[ip] [port] [机器人数量] [移动次数]");
			return;
		}
		
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		int num = Integer.parseInt(args[2]);
		int move = Integer.parseInt(args[3]);
		
		RobotConfig.Host = ip;
		RobotConfig.Port = port;
		
		RobotConfig.Init();
		
		System.out.println("准备开始");
		
		TimeBatchRun r = new TimeBatchRun(3*60*60*1000, num, 30*1000, new IncreaseRule(), new IncreaseRule());
		Step os = RFNormalStep(move);
		r.SetStep(os);
		r.run();
	}
	
	public static Step RFNormalStep(int move)
	{
		OrderStep os = new OrderStep();
		os.Add(new LoginNormalStep());
		os.Add(new AutoCreateRoleStep());
		os.Add(new CheckReceiveEnterWorldStep());
		os.Add(new AutoMoveStep(move));
		return os;
	}

}
