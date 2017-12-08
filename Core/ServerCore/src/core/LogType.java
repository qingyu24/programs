/**
 * eLogType.java 2012-10-19下午2:22:48
 */
package core;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface LogType
{
	String Serialize();
	
	boolean SystemLog();
	
	boolean LogicLog();
	
	boolean SQLLog();
	
	boolean DebugLog();
	
	boolean InfoLog();
	
	boolean WarningLog();
	
	boolean ErrorLog();
}
