/**
 * GatherInfo.java 2012-7-25上午10:08:42
 */
package core.ex;

//import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;

import core.Root;
import core.RootConfig;
import core.Tick;
import core.detail.impl.log.Log;
import core.ex.bind.*;

import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class GatherInfo implements Tick
{
	public static void OnPressAddSpeedCallBack()
	{
		speed *= RootConfig.GetInstance().SpeedSpace;
		speedValue.Set(speed);
		TimeMethod.SetSpeed(speed);
	}
	public static void OnPressDeSpeedCallBack()
	{
		speed /= RootConfig.GetInstance().SpeedSpace;
		speedValue.Set(speed);
		TimeMethod.SetSpeed(speed);
	}
	
	public static void OnPressAddTenMinuteCallBack()
	{
		TimeMethod.AddTime(600 * 1000);
	}
	
	public static void OnPressAddHourCallBack()
	{
		TimeMethod.AddTime(3600 * 1000);
	}
	
	public static void OnPressAddDayCallBack()
	{
		TimeMethod.AddTime(24 * 3600 * 1000);
	}
	
	private BindInt userNum;
	private BindLong processLogicNum;
	private BindLong receiveNum;
	private BindLong inputBuffer;
	private BindLong outputBuffer;
	private BindLong runSQLNum;
	private BindTime useTime;
	private BindLong outputBufferSecond;
	private BindInt storemsg;
	private BindString currTime;
	private BindString relateTime;
	private BindString realTime;
	
	private static BindFloat speedValue;
	private static float speed = 1.0f;
	private Object m_Show;
	private Object m_TotalPanel;
	private Method m_Method;
	
	private Method m_BtnBindMethod;
	
	private boolean m_Close = false;
	
	private Map<String, String> m_BtnCallBack = new HashMap<String, String>();	//按钮回调到的函数名,界面那边需要的对应函数名
	
	public void Close()
	{
		m_Close = true;
	}
	
	public GatherInfo()
	{
		m_BtnCallBack.put("OnPressAddSpeedCallBack", "OnPressAddSpeed");
		m_BtnCallBack.put("OnPressDeSpeedCallBack", "OnPressDeSpeed");
		m_BtnCallBack.put("OnPressAddTenMinuteCallBack", "OnPressAddTenMinute");
		m_BtnCallBack.put("OnPressAddHourCallBack", "OnPressAddHour");
		m_BtnCallBack.put("OnPressAddDayCallBack", "OnPressAddDay");
	}

	public void Init(Object show, Object panel)
	{
		try
		{
			m_Show = show;
			m_TotalPanel = panel;
			
			Method[] ms = m_TotalPanel.getClass().getMethods();
			for ( Method m : ms )
			{
				if ( m.getName().equals("SetValue") )
				{
					m_Method = m;
					break;
				}
			}
			
			ms = show.getClass().getMethods();
			for ( Method m : ms )
			{
				if ( m.getName().equals("BindBtn") )
				{
					m_BtnBindMethod = m;
					break;
				}
			}
			
			BindCallBackFn();
			
			Field[] fs = getClass().getDeclaredFields();
			for ( Field f : fs )
			{
				Class<?> type = f.getType();
				if ( type.getSuperclass() == Bind.class )
				{
					Constructor<?>[] cs = type.getConstructors();
					Debug.Assert(cs.length == 1, "必定只有一个构造函数");
					String bindname = f.getName();
					Object o = cs[0].newInstance(bindname, show);
					f.set(this, o);
				}
			}
		}
		catch (Exception e)
		{
			Log.out.LogException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see core.Tick#OnTick(long)
	 */
	@Override
	public void OnTick(long p_lTimerID) throws Exception
	{
		long tm = TimeMethod.currentTimeMillis();
		long realtm = TimeMethod.GetRealElapseTime();
		long relattm = TimeMethod.GetRelativeElapseTime();
		
		currTime.Set(Debug.GetShowTime(tm));
		relateTime.Set(Debug.GetUseTime1(relattm));
		realTime.Set(Debug.GetUseTime1(realtm));
	}
	
	public void RegTimer()
	{
		Root.GetInstance().AddLoopTimer(this, 1, null);
	}
	
	public void setInputBuffer(long inputBuffer)
	{
		_RunMethod("SetInputBuffer", ""+inputBuffer);
	}

	public void setInputBufferTotal(long inputBuffer)
	{
		if ( this.inputBuffer != null && m_Close == false)
		{
			this.inputBuffer.Set(inputBuffer);
		}
	}

	public void setLinkNum(int linkNum)
	{
		_RunMethod("SetLinkNum", ""+linkNum);
	}

	public void setLogicNetListNum(int logicNetListNum)
	{
		_RunMethod("SetLoginNetListNum", ""+logicNetListNum);
	}

	public void setLogicNum(long logicNum)
	{
		_RunMethod("SetLogicNum", ""+logicNum);
	}

	public void setLogicSQLListNum(int logicSQLListNum)
	{
		_RunMethod("SetLoginSQLListNum", ""+logicSQLListNum);
	}

	public void setLogicTime(long logicTime)
	{
		_RunMethod("SetLogicUseTime", ""+logicTime);
	}

	public void setNetNum(long netNum)
	{
		_RunMethod("SetNetNum", ""+netNum);
	}

	public void setOutputBuffer(long outputBuffer)
	{
		_RunMethod("SetOutputBuffer", ""+outputBuffer);
	}
	
	public void setOutputBufferSecond(long outputBufferSecond)
	{
		if ( this.outputBufferSecond != null && m_Close == false)
		{
			this.outputBufferSecond.Set(outputBufferSecond);
		}
	}

	public void setOutputBufferTotal(long outputBuffer)
	{
		if ( this.outputBuffer != null && m_Close == false)
		{
			this.outputBuffer.Set(outputBuffer);
		}
	}

	public void setProcessLogicNumTotal(long processLogicNum)
	{
		if ( this.processLogicNum != null && m_Close == false)
		{
			this.processLogicNum.Set(processLogicNum);
		}
		if ( this.useTime != null && m_Close == false)
		{
			this.useTime.Set();
		}
	}
	
	public void setReceiveMsgNumTotal(long receiveMsgNum)
	{
		if ( this.receiveNum != null && m_Close == false)
		{
			this.receiveNum.Set(receiveMsgNum);
		}
	}
	
	public void setRunSQLNumTotal(long runSQLNum)
	{
		if ( this.runSQLNum != null && m_Close == false)
		{
			this.runSQLNum.Set(runSQLNum);
		}
	}

	public void setSQLListNum(int SQLListNum)
	{
		_RunMethod("SetSQLListNum", ""+SQLListNum);
	}
	
	public void setSqlNum(long sqlNum)
	{
		_RunMethod("SetSQLNum", ""+sqlNum);
	}
	
	public void setSQLUseTime(long usetm)
	{
		_RunMethod("SetSQLUseTime", ""+usetm);
	}

	public void setStoremsg(int storemsg)
	{
		if ( this.storemsg != null && m_Close == false)
		{
			this.storemsg.Set(storemsg);
		}
	}
	
	public void setUserNum(int userNum)
	{
		if ( this.userNum != null && m_Close == false)
		{
			this.userNum.Set(userNum);
		}
		if ( m_TotalPanel != null )
		{
			_RunMethod("SetUserNum", ""+userNum);
		}
	}
	
	private void _AddBind(Method m, String s)
	{
		try
		{
			m_BtnBindMethod.invoke(m_Show, m, s);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void _RunMethod(String fnName, String param)
	{
		if ( m_Method == null || m_TotalPanel == null || m_Close)
		{
			return;
		}
		try
		{
			m_Method.invoke(m_TotalPanel, fnName, param);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	private void BindCallBackFn()
	{
		Method[] ms = this.getClass().getMethods();
		for (Method m : ms)
		{
			if (m_BtnCallBack.containsKey(m.getName()))
			{
				_AddBind(m, m_BtnCallBack.get(m.getName()));
			}
		}
	}
}
