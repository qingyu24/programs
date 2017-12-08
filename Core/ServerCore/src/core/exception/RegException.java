/**
 * RegException.java 2012-9-3下午3:21:48
 */
package core.exception;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RegException extends WrapException
{
	private static final long	serialVersionUID	= 1510426507954868906L;

	public RegException(Exception e)
	{
		super(e);
	}

	public RegException(String s)
	{
		super(s);
	}

}
