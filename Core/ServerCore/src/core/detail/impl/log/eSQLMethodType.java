/**
 * eSQLMethodType.java 2013-2-1下午8:26:29
 */
package core.detail.impl.log;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eSQLMethodType
{
	INSERT,
	EXECUTE,
	;

	
	public int ID()
	{
		switch (this)
		{
		case INSERT:	return 1;
		case EXECUTE:	return 2;
		}
		return 0;
	}

}
