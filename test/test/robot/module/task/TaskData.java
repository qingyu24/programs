/**
 * TaskData.java 2012-7-12上午11:37:44
 */
package test.robot.module.task;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import logic.module.task.*;
import logic.userdata.task.*;
import test.robot.*;
import test.robot.value.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TaskData implements ResData
{
	private Robot m_Robot;
	
	private boolean m_RefreshRes = false;
	private int m_MarkValue = 0;
	
	public Value<Integer> m_Markvalue = new Value<Integer>();
	public boolean m_RefreshTaskData = false;
	public boolean m_ModifyTaskData = false;
	public ConcurrentLinkedQueue<MemoryTaskData> m_AllTaskData = new ConcurrentLinkedQueue<MemoryTaskData>();
	public eTaskOperResultRes m_Oper;
	
	public TaskData(Robot r)
	{
		m_Robot = r;
		m_Oper = new eTaskOperResultRes(r);
	}
	
	public boolean GetRefreshRes() throws InterruptedException
	{
		if ( !m_RefreshRes ) m_Robot.Wait();
		return m_MarkValue != 0;
	}

	public void Reset()
	{
		m_AllTaskData.clear();
		m_Oper.Init();
		m_Markvalue.Init(-1);
	}

	public void Refresh(int markvalue, eTaskSynType type,
			ArrayList<MemoryTaskData> mtds)
	{
//		System.out.println("Refresh TaskData:" + markvalue + " type:" + type + " sz:" + mtds.size());
//		for ( MemoryTaskData mtd : mtds )
//		{
//			System.out.println("Data:" + mtd.GetTaskID() + "," + mtd.GetEvent(0) + "," + mtd.GetEvent(1) + "," + mtd.GetEvent(2) + "," + mtd.GetTaskState());
//		}
		m_Markvalue.Set(markvalue);
		switch(type)
		{
		case EMPTY:
			break;
		case ALL: 
			SetAllTaskData(mtds); 
			m_RefreshTaskData = true;
			break;
		case MODIFY:
			ModifyTaskData(mtds);
			break;
		}
	}

	public void SetOper(eTaskOperResult res, short taskid)
	{
//		System.out.println("SetOper TaskData:" + res + " taskid:" + taskid);
		m_Oper.Set(res, taskid);
	}
	
	private void ModifyTaskData(ArrayList<MemoryTaskData> mtds)
	{
		if ( m_AllTaskData == null || m_AllTaskData.isEmpty() )
		{
			SetAllTaskData(mtds);
			m_RefreshTaskData = true;
			return;
		}
		for ( MemoryTaskData mtd : mtds )
		{
			boolean modify = false;
			for ( MemoryTaskData m : m_AllTaskData )
			{
				if ( m.GetTaskID() != mtd.GetTaskID() )
				{
					continue;
				}
				m.InitMemoryTaskData(mtd);
				modify = true;
			}
			if ( !modify )
			{
				m_AllTaskData.add(mtd);
			}
			m_ModifyTaskData = true;
		}
	}
	
	private void SetAllTaskData(ArrayList<MemoryTaskData> mtds)
	{
		m_AllTaskData.clear();
		for ( MemoryTaskData mtd : mtds )
		{
			m_AllTaskData.add(mtd);
		}
	}
}
