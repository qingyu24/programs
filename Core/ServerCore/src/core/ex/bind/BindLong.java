/**
 * BindLong.java 2013-4-20下午7:31:45
 */
package core.ex.bind;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class BindLong extends Bind
{
	private long m_Value;
	private long m_Base;
	public BindLong(String bindname, Object bindobj)
	{
		super(bindname, bindobj);
	}
	
	public void Set(long v)
	{
		m_Value = v;
		super.SetLongNumForamt(v - m_Base);
	}

	public long Get()
	{
		return m_Value;
	}

	public void SetBase(long get)
	{
		m_Base = get;
		Set(m_Value);
	}
}
