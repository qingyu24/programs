/**
 * SystemLevel.java 2012-10-23下午3:19:11
 */
package core.detail.impl.log;

import org.apache.log4j.Level;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SystemLevel extends Level
{
	private static final long	serialVersionUID	= -970727952436595480L;

	public static final int SYSTEM_INT = 45000;
	
	public static final int SYSTEM_DEBUG_INT 	= SYSTEM_INT + 1;
	public static final int SYSTEM_INFO_INT 	= SYSTEM_INT + 2;
	public static final int SYSTEM_WARNING_INT 	= SYSTEM_INT + 3;
	public static final int SYSTEM_ERROR_INT 	= SYSTEM_INT + 4;
	
	public static final int SQL_INT = 50000;
	
	public static final Level SYSTEM_INFO 		= new SystemLevel(SYSTEM_INFO_INT, "SYSTEM_INFO");
	public static final Level SYSTEM_WARNING 	= new SystemLevel(SYSTEM_WARNING_INT, "SYSTEM_WARNING");
	public static final Level SYSTEM_ERROR	 	= new SystemLevel(SYSTEM_ERROR_INT, "SYSTEM_ERROR");
	public static final Level SQL				= new SystemLevel(SQL_INT, "SQL");
	
	protected SystemLevel(int level, String levelStr)
	{
		super(level, levelStr, 2);
	}

	public static Level toLevel(int val)
	{
		switch(val)
		{
		case SYSTEM_INFO_INT: 	return SYSTEM_INFO;
		case SYSTEM_WARNING_INT:return SYSTEM_WARNING;
		case SYSTEM_ERROR_INT:	return SYSTEM_ERROR;
		}
		return (Level) toLevel(val, Level.DEBUG);
	}
	
	public static Level tolevel(int val, Level defaultLevel)
	{
		switch(val)
		{
		case SYSTEM_INFO_INT: 	return SYSTEM_INFO;
		case SYSTEM_WARNING_INT:return SYSTEM_WARNING;
		case SYSTEM_ERROR_INT:	return SYSTEM_ERROR;
		}
		return (Level) toLevel(val, defaultLevel);
	}
	
	public static Level toLevel(String sArg, Level defaultLevel)
	{
		if ( sArg != null )
		{
			if ( sArg.toUpperCase().equals("SYSTEM_INFO") )
			{
				return SYSTEM_INFO;
			}
			else if ( sArg.toUpperCase().equals("SYSTEM_WARNING") )
			{
				return SYSTEM_WARNING;
			}
			else if ( sArg.toUpperCase().equals("SYSTEM_ERROR") )
			{
				return SYSTEM_ERROR;
			}
		}
		return (Level) toLevel(sArg, Level.DEBUG);
	}
}
