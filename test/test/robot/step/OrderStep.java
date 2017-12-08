/**
 * OrderStep.java 2012-7-31下午5:19:58
 */
package test.robot.step;

import java.util.*;

import test.robot.Robot;
import utility.Debug;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 顺序执行并且要求所有的操作都成功,当其中有一个操作失败就中断执行
 */
public class OrderStep extends Step
{
	private ArrayList<Step> m_All = new ArrayList<Step>();
	private int m_ExecuteNum = 0;
	private int m_ResultNum = 0;
	private ArrayList<Boolean> m_Res = new ArrayList<Boolean>();
	private boolean m_Run = true;
	
	/**
	 * 添加一个步骤
	 */
	public void Add(Step s)
	{
		m_All.add(s);
	}
	
	public int GetStepNum()
	{
		return m_All.size();
	}
	
	public int GetExecuteNum()
	{
		return m_ExecuteNum;
	}
	
	public int GetResultNum()
	{
		return m_ResultNum;
	}
	
	public ArrayList<Boolean> GetRes()
	{
		return m_Res;
	}
	
	/* (non-Javadoc)
	 * @see test.robot.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		OrderStep os = new OrderStep();
		for ( Step s : m_All )
		{
			os.m_All.add(s.Clone());
		}
		return os;
	}

	/* (non-Javadoc)
	 * @see test.robot.Step#Execute(test.robot.Robot)
	 */
	@Override
	public boolean Execute(Robot r) throws InterruptedException
	{
		if ( !m_Run )
		{
			return true;
		}
		int asz = m_All.size();
		Debug.Assert(m_ExecuteNum <= asz, "");
		if ( m_ExecuteNum == asz )
		{
			return true;
		}
		//当一个操作返回的时候才进行下一个操作的执行
		if ( m_ResultNum == m_ExecuteNum )
		{
			Step s = m_All.get(m_ExecuteNum);
			if ( s.Execute(r) )
			{
				m_ExecuteNum++;
			}
		}
		
		return m_ExecuteNum == asz;
	}

	/* (non-Javadoc)
	 * @see test.robot.Step#Executed(test.robot.Robot)
	 */
	@Override
	public boolean Executed(Robot r) throws Exception
	{
		int asz = m_All.size();
		Debug.Assert(m_ResultNum <= asz, "");
		if ( m_ResultNum == asz )
		{
			return true;
		}
		Step s = m_All.get(m_ResultNum);
		if ( s.Executed(r) )
		{
			m_ResultNum++;
			boolean res = s.Result(r);
			m_Res.add(res);
			if ( !res )
			{
				m_Run = false;
				return true;
			}
		}
		return m_ResultNum == asz;
	}

	/* (non-Javadoc)
	 * @see test.robot.Step#Result(test.robot.Robot)
	 */
	@Override
	public boolean Result(Robot r)
	{
		for ( boolean c : m_Res )
		{
			if ( !c )
			{
				return false;
			}
		}
		return true;
	}

}
