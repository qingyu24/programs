/**
 * WrapException.java 2012-9-3下午3:22:20
 */
package core.exception;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class WrapException extends Exception
{
	private static final long	serialVersionUID	= -4113614874143223264L;
	private Exception m_Exception;

	public WrapException(Exception e)
	{
		super(e);
		m_Exception = e;
	}
	
	public WrapException(String s)
	{
		super(s);
	}
	
	public WrapException(Exception e, String s)
	{
		super(s, e);
		m_Exception = e;
	}
	
	public Exception GetException()
	{
		return m_Exception;
	}
}
