/**
 * DebugAssertRuntimeException.java 2012-8-28下午4:53:25
 */
package core.exception;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DebugAssertRuntimeException extends WrapRuntimeException
{
	private static final long	serialVersionUID	= 5475079267522500519L;

	public DebugAssertRuntimeException(String str)
	{
		super(str);
	}
	
	public DebugAssertRuntimeException(Throwable e)
	{
		super(e);
	}
}
