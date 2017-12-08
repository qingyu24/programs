/**
 * TaskRtnInterface.java 2012-7-12上午11:48:08
 */
package test.robot.module.task;

import java.util.*;

import logic.*;
import logic.userdata.task.*;

import core.remote.*;
import test.robot.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
@RCC ( ID = Reg.TASK )
public interface TaskRtnInterface
{
	static final int MID_DATA = 0;
	static final int MID_OPER = 1;
	
	@RFC ( ID = MID_DATA )
	void OnSynData(@PU Robot r, 
					@PI int markvalue,
					@PBT byte type,
					@PVC (Type = MemoryTaskData.class) ArrayList<MemoryTaskData> mtds);
	
	@RFC ( ID = MID_OPER )
	void OnTaskOper(@PU Robot r, @PBT byte type,@PST short taskid);
}
