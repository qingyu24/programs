/**
 * ThreadExceptionProcess.java 2012-10-12上午11:45:57
 */
package core.detail.impl.mgr;

import java.lang.Thread.UncaughtExceptionHandler;

import core.detail.impl.log.Log;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ThreadExceptionProcess implements UncaughtExceptionHandler
{
	private static ThreadExceptionProcess m_Instance = new ThreadExceptionProcess();
	
	public static ThreadExceptionProcess GetInstance()
	{
		return m_Instance;
	}
	/* (non-Javadoc)
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		Log.out.LogException(e);
	}

}
