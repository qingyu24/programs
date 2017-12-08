package logic.module;

public class LongPairParam
{
	public long m_ValueFirst;
	public long m_ValueSecond;
	
	public LongPairParam(long p_First, long p_Second)
	{
		if (p_First > p_Second)
		{
			m_ValueFirst = p_First;
			m_ValueSecond = p_Second;
		}
		else
		{
			m_ValueFirst = p_Second;
			m_ValueSecond = p_First;
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (null == obj)
			return false;
		if (obj.getClass() != getClass())
			return false;
		
		LongPairParam param = (LongPairParam)obj;
		if (param.m_ValueFirst == m_ValueFirst && param.m_ValueSecond == m_ValueSecond)
			return true;
		return false;
	}
	@Override
	public int hashCode()
	{
		String str = ((Long)m_ValueFirst).toString() + "#" + ((Long)m_ValueSecond).toString();
		return str.hashCode();
	}
}
