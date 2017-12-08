/**
 * AutoMoveStep.java 2012-12-24上午11:22:44
 */
package test.robot.step;

import java.util.Random;

import test.robot.RFCFn;
import test.robot.Robot;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class AutoMoveStep extends Step
{
	private boolean m_EnterCity = false;
	private long m_SynTime = 0;
	private static final int m_SynSpace = 3000;
	private float m_PosX = 23.4f;
	private float m_PosZ = 17.0f;
	private short m_EnterID = 1001;
	
	private final float m_StaticPosX = 23.4f;
	private final float m_StaticPosZ = 17.0f;
	
	private int m_Num = 0;
	private int m_MaxNum = 0;
	
	public AutoMoveStep(int maxnum)
	{
		m_MaxNum = maxnum;
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Clone()
	 */
	@Override
	public Step Clone()
	{
		return new AutoMoveStep(m_MaxNum);
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Execute(test.robot.Robot)
	 */
	@Override
	public boolean Execute(Robot r) throws InterruptedException
	{
		if ( m_EnterCity == false )
		{
			m_EnterCity = true;
			RFCFn.City_EnterCity(r, m_EnterID, m_PosX, m_PosZ);
			m_SynTime = System.currentTimeMillis();
//			System.out.println("进入城市:" + m_EnterID);
			return false;
		}
		
		if ( System.currentTimeMillis() - m_SynTime > m_SynSpace )
		{
			m_SynTime = System.currentTimeMillis();
			RFCFn.City_SyncPos(r, m_PosX, m_PosZ, (byte)0);
			_RandomData();
			
			if ( m_Num++ > m_MaxNum )
			{
				return true;
			}
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Executed(test.robot.Robot)
	 */
	@Override
	public boolean Executed(Robot r) throws Exception
	{
		return m_Num >= m_MaxNum;
	}

	/* (non-Javadoc)
	 * @see test.robot.step.Step#Result(test.robot.Robot)
	 */
	@Override
	public boolean Result(Robot r) throws InterruptedException
	{
		return true;
	}

	private void _RandomData()
	{
		Random r = new Random();
		int ex = r.nextBoolean() ? 1 : -1;
		int ez = r.nextBoolean() ? 1 : -1;
		float x =  ex * r.nextInt(4000) / 1000.0f;
		float z = ez * r.nextInt(4000) / 1000.0f;
		
		if ( x < 0.5 )
		{
			x += 0.5;
		}
		
		if ( z < 0.5 )
		{
			z += 0.5;
		}
		
		m_PosX += x;
		m_PosZ += z;
		
		if ( m_PosX - m_StaticPosX > 7)
		{
			m_PosX = m_StaticPosX;
		}
		
		if ( m_PosZ - m_StaticPosZ > 7 )
		{
			m_PosZ = m_StaticPosZ;
		}
	}
}
