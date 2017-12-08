/**
 * RunStep.java 2012-11-5下午2:36:57
 */
package test.robot.step;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import test.robot.*;
import test.robot.utility.*;
import utility.Debug;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RunStep
{
	public static void RunBatch(Step s, int maxtm, int num, int waittm)
	{
		TimeBatchRun r = new TimeBatchRun(maxtm, num, waittm);
		r.SetStep(s);
		r.run();
	}
	
	public static void RunFree(Step s, int maxtm, int num )
	{
		FreeRun r = new FreeRun(maxtm, num, NameRule.randomRule, NameRule.randomRule);
		r.SetStep(s);
		r.run();
	}
	
	public static void Run(String ip, int port, String stepname, int type, int maxtm, int num, int waittm)
	{
		Debug.Assert(type == 1 || type == 2, "目前可用的机器人类型只有1和2");
		RobotConfig.Host = ip;
		RobotConfig.Port = port;
		RobotConfig.Init();
		
		Step s = GetStep(stepname);
		switch(type)
		{
		case 1: RunBatch(s, maxtm, num, waittm);break;
		case 2: RunFree(s, maxtm, num);break;
		}
	}
	
	public static Step GetStep(String stepname)
	{
		String[] sps = stepname.split(":");
		Debug.Assert(sps.length == 2, "格式必定以:分隔");
		try
		{
			Class<?> c = Class.forName(sps[0]);
			Method[] ms = c.getMethods();
			for ( Method m : ms )
			{
				if ( m.getName().equals(sps[1]) )
				{
					Object o = m.invoke(null, (Object[])null);
					Step s = (Step)o;
					return s;
				}
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
