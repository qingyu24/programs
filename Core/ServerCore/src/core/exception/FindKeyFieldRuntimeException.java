/**
 * FindKeyFieldRuntimeException.java 2012-8-28下午4:45:37
 */
package core.exception;

import core.db.mysql.eFieldKey;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class FindKeyFieldRuntimeException extends WrapRuntimeException
{
	private static final long	serialVersionUID	= 6263829882986927487L;

	public FindKeyFieldRuntimeException(Class<?> c, eFieldKey... keys)
	{
		super("无法在类型[" + c + "]的字段中找到其中的任意一个keys[" + keys + "]");
	}
}
