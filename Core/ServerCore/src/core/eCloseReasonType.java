/**
 * eCloseReasonType.java 2013-3-14下午2:02:53
 */
package core;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum eCloseReasonType
{
	LISTEN_CLOSE,
	CANCELLEDKEY_EXCEPTION,
	DDOS,
	READ_EXCEPTION,
	WRITE_EXCEPTION,
	LOGICRUN_EXCEPTION,
	LOGICSQLRUN_EXCEPTION,
	SQLRUN_EXCEPTION,
	GM_LEVEL,
	;
	
	public int ID()
	{
		switch (this)
		{
		case LISTEN_CLOSE:				return -1;
		case CANCELLEDKEY_EXCEPTION:	return -2;
		case DDOS:						return -3;
		case READ_EXCEPTION:			return -4;
		case WRITE_EXCEPTION:			return -5;
		case LOGICRUN_EXCEPTION:		return -6;
		case LOGICSQLRUN_EXCEPTION:		return -7;
		case SQLRUN_EXCEPTION:			return -8;
		case GM_LEVEL:					return -9;
		}
		return 0;
	}
}
