/**
 * AutoFinishTaskStep.java 2012-8-2下午1:34:57
 */
package test.robot.step;

import java.util.concurrent.*;

import logic.config.*;
import logic.config.task.TaskConfig;
import logic.config.task.TaskTemplateData;
import logic.config.task.TaskTemplateData.EventData;
import logic.module.battle.enums.EBattleSubType;
import logic.module.event.eEventType;
import logic.userdata.task.*;
import test.robot.*;
import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class AutoFinishAllTaskStep extends Step
{
	public class ExecuteTask
	{
		private MemoryTaskData m_TaskData = null;
		private boolean m_WaitMsg = false;
		private long m_WaitEventTime = -1;
		
		//是否空闲
		public boolean IsSpace()
		{
			if ( m_TaskData == null )
			{
				return true;
			}
			return m_TaskData.IsFinish();
		}
		
		public void ReadyExecute(MemoryTaskData mtd)
		{
			if ( m_TaskData == null )
			{
				m_TaskData = new MemoryTaskData();
			}
//			System.out.println("设置执行任务:" + mtd.GetTaskID());
			m_TaskData.InitMemoryTaskData(mtd);
		}

		public void Execute(Robot r) throws InterruptedException
		{
//			System.out.println("执行任务:" + m_TaskData.GetTaskID());
			
			if ( m_WaitMsg && m_WaitEventTime != -1 && System.currentTimeMillis() > m_WaitEventTime + 2000 )
			{
				//发送了事件,可是服务器2秒了还让事件变化,那重试下
				m_WaitMsg = false;
				m_WaitEventTime = -1;
			}
			
			if ( !m_WaitMsg )
			{
//				System.out.println("需发送消息:" + m_TaskData.GetTaskID());
				eTaskState state = m_TaskData.GetTaskState();
				switch(state)
				{
				case UNRECEIVE:
//					System.out.println("发送接任务:" + m_TaskData.GetTaskID());
					RFCFn.Task_Receive(r, (short) m_TaskData.GetTaskID());
					m_WaitMsg = true;
					break;
				case RECEIVE:
					m_WaitMsg = ExecuteEvent(r);
					break;
				case CANFINISH:
//					System.out.println("发送完成任务:" + m_TaskData.GetTaskID());
					RFCFn.Task_Finish(r, (short) m_TaskData.GetTaskID());
					m_WaitMsg = true;
					break;
				case FINISH:
					break;
				}
				
			}
			else
			{
				MemoryTaskData mtd = GetMemoryTaskData(r.m_TaskData.m_AllTaskData, (short) m_TaskData.GetTaskID());
				if ( mtd == null )
				{
					return;
				}
				
				if ( mtd.GetTaskState() == m_TaskData.GetTaskState() )
				{
//					System.out.println("检测任务数据变化:" + m_TaskData.GetTaskID());
					if ( r.m_TaskData.m_ModifyTaskData )
					{
						if ( mtd.GetEvent(0) != m_TaskData.GetEvent(0) ||
							 mtd.GetEvent(1) != m_TaskData.GetEvent(1) ||
							 mtd.GetEvent(2) != m_TaskData.GetEvent(2))
						{
//							System.out.println("事件变化了:" + m_TaskData.GetTaskID());
							m_WaitMsg = false;
							m_WaitEventTime = -1;
							m_TaskData.InitMemoryTaskData(mtd);
						}
						r.m_TaskData.m_ModifyTaskData = false;
					}
					else
					{
					}
				}
				else
				{
//					System.out.println("刷新数据:" + m_TaskData.GetTaskID());
					//这个地方突然想起一个问题,现在的机制做单元测试有麻烦,比如发送一个事件可能导致多个锁触发,如任务的监听锁和物品的监听锁(比如任务事件是要求使用一个东西),以后再想想,
					//不过机器人应该不影响,因为机器人使用不是通知机制而是查询机制,每个机器人在某时刻会查询自己关心的东西,所以同时根本不管同时触发的事情
					//任务状态变化,那么接着发下一个消息来处理
					m_TaskData.InitMemoryTaskData(mtd);
					m_WaitMsg = false;
//					System.out.println("任务状态:" + m_TaskData.GetTaskID() + " state:" + m_TaskData.GetTaskState());
					
					if ( !m_TaskData.IsFinish() )
					{
						return;
					}
					
//					System.out.println("=====任务完成了:" + m_TaskData.GetTaskID() + ":" + r);
					m_ExecuteTaskNum++;
					for ( int i = 0; i < m_AllConfig.length; ++i )
					{
						if ( m_AllConfig[i].ID != m_TaskData.GetTaskID() )
						{
							continue;
						}
						m_FinishState[i] = true;
						break;
					}
				}
			}
		}

		private boolean ExecuteEvent(Robot r)
		{
			if ( System.currentTimeMillis() - m_WaitEventTime < 300 )
			{
				//最快也是1秒执行一个事件
				return false;
			}
			
			TaskTemplateData ttd =  m_TaskData.GetConfig();
			int e1 = 0, e2 = 0, e3 = 0;
			Debug.Assert(ttd.Events.length == 3 , "任务的事件有且只有3个");
			EventData[] eds = ttd.Events;
			e1 = eds[0].Num;
			e2 = eds[1].Num;
			e3 = eds[2].Num;

			int ce1 = 0, ce2 = 0, ce3 = 0;
			MemoryTaskData mtd = GetMemoryTaskData(r.m_TaskData.m_AllTaskData, (short) m_TaskData.GetTaskID());
			if ( mtd != null )
			{
				ce1 = mtd.GetEvent(0);
				ce2 = mtd.GetEvent(1);
				ce3 = mtd.GetEvent(2);
			}
			
			if ( e1 != 0 && e1 != ce1 )
			{
				SendEvent(r, eds, 0);
			}
			else if ( e2 != 0 && e2 != ce2 )
			{
				SendEvent(r, eds, 1);
			}
			else if ( e3 != 0 && e3 != ce3 )
			{
				SendEvent(r, eds, 2);
			}
			else
			{
//				Debug.Assert(false); ///<如果任务为未完成,为啥事件都完成了...
			}
			m_WaitEventTime = System.currentTimeMillis();
			return true;
		}

		private void SendEvent(Robot r, EventData[] eds, int index)
		{
			EventData ed = eds[index];
			eEventType type = eEventType.Create(ed.EventID);
			switch(type)
			{
			case KILL_MONSTER:
				Debug.Assert(false, "不支持直接杀怪物事件");///<还没有这类的配置
				break;
			case GET_ITEM:
				int battleid = TempBattleMapConfig.GetBattleIDByDropItem(ed.AimID);
				Debug.Assert(battleid > 0, "必定有物品掉落场景" );
				RFCFn.Battle_RequestPVE(r, (short) battleid, EBattleSubType.EBATTLESUBTYPE_NORMAL_COPY.value());
				RFCFn.Battle_RequestBattleStream(r);
				break;
			case SUCCESS_BATTLE:
			{
				byte v = ed.AimID < 20000 ? EBattleSubType.EBATTLESUBTYPE_NORMAL_COPY.value() : EBattleSubType.EBATTLESUBTYPE_ELITE_COPY.value();
				RFCFn.Battle_RequestPVE(r, (short) ed.AimID, v);
				RFCFn.Battle_RequestBattleStream(r);
				break;
			}
			case USE_ITEM:
				//还需要物品系统支持,太麻烦,直接用临时函数来增加事件
				RFCFnTest.Task_TempAddEvent(r, (short) m_TaskData.GetTaskID(), index);
				break;
			case ITEM_NUM_CHANGE:
				//还需要物品系统支持,太麻烦,直接用临时函数来增加事件
				RFCFnTest.Task_TempAddEvent(r, (short) m_TaskData.GetTaskID(), index);
				break;
			case ENTER_BATTLE:
			{
				byte v = ed.AimID < 20000 ? EBattleSubType.EBATTLESUBTYPE_NORMAL_COPY.value() : EBattleSubType.EBATTLESUBTYPE_ELITE_COPY.value();
				RFCFn.Battle_RequestPVE(r, (short) ed.AimID, v);
				RFCFn.Battle_RequestBattleStream(r);
				break;
			}
			case ENTER_ANY_BATTLE:
			{
				byte v = ed.AimID < 20000 ? EBattleSubType.EBATTLESUBTYPE_NORMAL_COPY.value() : EBattleSubType.EBATTLESUBTYPE_ELITE_COPY.value();
				RFCFn.Battle_RequestPVE(r, (short) TempBattleMapConfig.GetRandomBattleID(), v);
				RFCFn.Battle_RequestBattleStream(r);
				break;
			}
			case SUCCESS_ANY_BATTLE:
			{
				byte v = ed.AimID < 20000 ? EBattleSubType.EBATTLESUBTYPE_NORMAL_COPY.value() : EBattleSubType.EBATTLESUBTYPE_ELITE_COPY.value();
				RFCFn.Battle_RequestPVE(r, (short) TempBattleMapConfig.GetRandomBattleID(), v);
				RFCFn.Battle_RequestBattleStream(r);
				break;
			}
			case ADD_MERCENARY:
				RFCFn.MercenaryImple_RequireAddMercenary(r, ed.AimID);
				break;
			default:
				System.out.println("未加的事件代码:" + type);
			}
		}
	}
	
	private static TaskTemplateData[] m_AllConfig = null;
	private boolean[] m_FinishState;
	private boolean m_InitTaskData;
	private short m_RunTaskIndex = -1;
	private ExecuteTask m_ExecuteTask = new ExecuteTask();
	private int m_ExecuteTaskNum = 0;
	
	public AutoFinishAllTaskStep()
	{
		if ( m_AllConfig == null )
		{
			m_AllConfig = TaskConfig.GetAllConfig();
		}
		m_FinishState = new boolean[m_AllConfig.length];
		
	}
	/* (non-Javadoc)
	 * @see test.robot.step.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		return new AutoFinishAllTaskStep();
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Execute(test.robot.Robot)
	 */
	@Override
	public boolean Execute(Robot r) throws InterruptedException
	{
		if ( !m_InitTaskData )
		{
			m_InitTaskData = true;
			RFCFnTest.Task_ClearTask(r);
			RFCFn.Task_Refresh(r, -1);
		}
		if ( !r.m_TaskData.m_Markvalue.IsSet() )
		{
			return false;
		}
		if ( r.m_TaskData.m_RefreshTaskData )
		{
			r.m_TaskData.m_RefreshTaskData = false;
			RefreshFinishState(r.m_TaskData.m_AllTaskData);
		}
		
		if ( m_ExecuteTask.IsSpace() )
		{
			short taskid = GetNextNeedFinishTask();
			if ( taskid < 0 )
			{
				//所有的任务都完成了
				return true;
			}
			MemoryTaskData mtd = GetMemoryTaskData(r.m_TaskData.m_AllTaskData, taskid);
			if ( mtd == null )
			{
				//没有这个任务数据,说明没接
				mtd = new MemoryTaskData();
				mtd.InitCreateTaskData(taskid);
			}
			m_ExecuteTask.ReadyExecute(mtd);
		}
		
		if ( m_ExecuteTask.IsSpace() )
		{
			//这个时候还没有,那说明是没有可执行的任务
			return true;
		}
		else
		{
			m_ExecuteTask.Execute(r);
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see test.robot.step.Step#Executed(test.robot.Robot)
	 */
	@Override
	public boolean Executed(Robot r) throws Exception
	{
		return IsAllFinish();
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Result(test.robot.Robot)
	 */
	@Override
	public boolean Result(Robot r) throws InterruptedException
	{
		if ( IsAllFinish() )
		{
//			System.out.println("* 执行的任务数量:" + m_ExecuteTaskNum);
			return true;
		}
		return false;
	}
	
	public int GetExecuteTaskNum()
	{
		return m_ExecuteTaskNum;
	}
	
	private void RefreshFinishState(ConcurrentLinkedQueue<MemoryTaskData> p_AllTaskData)
	{
		for ( int i = 0; i < m_AllConfig.length; ++i )
		{
			m_FinishState[i] = false;
		}
		
		for ( MemoryTaskData mtd : p_AllTaskData )
		{
			if ( !mtd.IsFinish() )
			{
				continue;
			}
			int id = mtd.GetTaskID();
			for ( int i = 0; i < m_AllConfig.length; ++i )
			{
				if ( m_AllConfig[i].ID == id )
				{
					m_FinishState[i] = true;
					break;
				}
			}
		}
	}
	
	private boolean IsAllFinish()
	{
		return m_RunTaskIndex == -2;
	}
	
	private short GetNextNeedFinishTask()
	{
		if ( m_RunTaskIndex == -1 )
		{
			for (int i = 0; i < m_FinishState.length; ++i )
			{
				if ( !m_FinishState[i] )
				{
					m_RunTaskIndex = (short) i;
					break;
				}
			}
			if ( m_RunTaskIndex == -1 )
			{
				//没有找到要完成的任务,那么就是全部完成了
				m_RunTaskIndex = -2;
				return -1;
			}
		}
		else if ( m_RunTaskIndex == -2 )
		{
			return -1; //表示所有的任务都完成了
		}
		else
		{
			while ( m_RunTaskIndex < m_FinishState.length && m_FinishState[m_RunTaskIndex] )
			{
				m_RunTaskIndex++;
			}
			if ( m_RunTaskIndex >= m_FinishState.length )
			{
				m_RunTaskIndex = -2;
				return -1;
			}
		}
		
		if ( m_RunTaskIndex >= 0 && m_RunTaskIndex < m_FinishState.length )
		{
			return (short) m_AllConfig[m_RunTaskIndex].ID;
		}
		return -1;
	}

	private MemoryTaskData GetMemoryTaskData(ConcurrentLinkedQueue<MemoryTaskData> p_AllTaskData, short taskid )
	{
		for ( MemoryTaskData mtd : p_AllTaskData )
		{
			if ( mtd.GetTaskID() == taskid )
			{
				return mtd;
			}
		}
		return null;
	}
}
