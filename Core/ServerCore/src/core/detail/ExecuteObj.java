/**
 * ExecuteObj.java 2012-10-17上午11:03:29
 */
package core.detail;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ExecuteObj<T>
{
	private T 	m_T;
	private int	m_ExecuteError;	///<执行期间发生的错误
	private int m_MaxError;		///<运行的最大错误数量
	
	public ExecuteObj(T t, int maxAllowError)
	{
		m_T = t;
		m_MaxError = maxAllowError;
	}
	
	public T Get()
	{
		return m_T;
	}
	
	public boolean CanRun()
	{
		if ( m_ExecuteError < m_MaxError )
		{
			return true;
		}
		return false;
	}
	
	public void AddError()
	{
		m_ExecuteError++;
	}
	
	public int GetErrorNum()
	{
		return m_ExecuteError;
	}
}
