/**
 * TaskRtn.java 2012-7-12下午4:01:16
 */
package test.robot.module.task;

import java.util.*;

import logic.userdata.task.*;
import logic.module.task.*;
import test.robot.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TaskRtn implements TaskRtnInterface
{
	/* (non-Javadoc)
	 * @see test.robot.module.task.TaskRtnInterface#OnSynData(test.robot.Robot, int, byte, java.util.ArrayList)
	 */
	@Override
	public void OnSynData(Robot r, int markvalue, byte type,
			ArrayList<MemoryTaskData> mtds)
	{
		r.m_TaskData.Refresh(markvalue, eTaskSynType.Create(type), mtds);
	}

	/* (non-Javadoc)
	 * @see test.robot.module.task.TaskRtnInterface#OnTaskOper(test.robot.Robot, byte, short)
	 */
	@Override
	public void OnTaskOper(Robot r, byte type, short taskid)
	{
		r.m_TaskData.SetOper( eTaskOperResult.Create(type), taskid );
	}
}
