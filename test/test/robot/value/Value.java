/**
 * Value.java 2012-8-2下午2:03:41
 */
package test.robot.value;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class Value<T>
{
	private boolean m_Set;
	private T m_Res;
	
	public boolean IsSet()
	{
		return m_Set;
	}
	
	public T Result() throws InterruptedException
	{
		return m_Res;
	}
	
	public void Set(T v)
	{
		m_Set = true;
		m_Res = v;
	}
	
	public void Init(T v)
	{
		m_Set = false;
		m_Res = v;
	}
}
