/**
 * LostCodeRuntimeException.java 2012-8-28下午6:29:51
 */
package core.exception;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 有未写完的代码...
 */
public class LostCodeRuntimeException extends WrapRuntimeException
{
	private static final long	serialVersionUID	= -229674973081675870L;
	
	public LostCodeRuntimeException(String str)
	{
		super(str);
	}
}
