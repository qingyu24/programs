/**
 * BindTime.java 2013-4-20下午7:32:16
 */
package core.ex.bind;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class BindTime extends Bind
{
	private final long m_StartTime = System.currentTimeMillis();
	public BindTime(String bindname, Object bindobj)
	{
		super(bindname, bindobj);
	}
	
	public void Set()
	{
		SetUseTime(System.currentTimeMillis() - m_StartTime);
	}
	
}
