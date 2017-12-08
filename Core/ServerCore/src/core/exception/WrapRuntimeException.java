/**
 * WrapRuntimeException.java 2012-8-29上午10:31:52
 */
package core.exception;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class WrapRuntimeException extends RuntimeException
{
	private static final long	serialVersionUID	= -6559583236513314969L;
	private Throwable m_Exception;
	
	public WrapRuntimeException(Throwable e)
	{
		super(e);
		m_Exception = e;
	}
	
	public WrapRuntimeException(String s)
	{
		super(s);
	}
	
	public WrapRuntimeException(Exception e, String s)
	{
		super(s, e);
		m_Exception = e;
	}
	
	public Throwable GetException()
	{
		return m_Exception;
	}
}
