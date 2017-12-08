/**
 * TraceSelf.java 2012-10-13上午11:21:43
 */
package test.c;

import java.util.*;

import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TraceSelf
{
	public static class StackTrace
	{
		private StackTraceElement m_StackTraceElement;
		
		@Override
		public String toString()
		{
			return super.toString() + " File:" + _Get().getFileName() + " Method:" + _Get().getMethodName() + " Line:" + _Get().getLineNumber();
		}
		
		public StackTrace Trace()
		{
			m_StackTraceElement = null;
			return this;
		}
		
		private StackTraceElement _Get()
		{
			if ( m_StackTraceElement == null )
			{
				m_StackTraceElement = new Throwable().getStackTrace()[4];
			}
			return m_StackTraceElement;
		}

		public String GetFileName()
		{
			return _Get().getFileName();
		}

		public String GetMethodName()
		{
			return _Get().getMethodName();
		}

		public int GetLine()
		{
			return _Get().getLineNumber();
		}
	}
	
	public static class GlobalStackTrace
	{
		@SuppressWarnings("unused")
		private static Map<Integer, StackTrace> m_All = new HashMap<Integer, StackTrace>();
		private static StackTrace m_StackTrace = new StackTrace();
		
		public static String GetFileName()
		{
			return _GetStackTrace(Rand.Get()).GetFileName();
		}
		
		public static String GetMethodName()
		{
			return _GetStackTrace(Rand.Get()).GetMethodName();
		}
		
		public static int GetLine()
		{
			return _GetStackTrace(Rand.Get()).GetLine();
		}
		
		public static String GetFullInfo(String format)
		{
			StackTrace s = _GetStackTrace(Rand.Get());
			return String.format(format, s.GetFileName(), s.GetMethodName(), s.GetLine());
		}
		
		public static String GetFullInfo()
		{
			return GetFullInfo("File:%1$s Method:%2$s Line:%3$d");
		}
		
		private static StackTrace _GetStackTrace(int v)
		{
//			if ( m_All.containsKey(v) )
//			{
//				return m_All.get(v);
//			}
//			StackTrace trace = new StackTrace();
//			m_All.put(v, trace);
//			return trace;
			m_StackTrace.Trace();
			return m_StackTrace;
		}
	}
	
	public static void main(String[] args)
	{
		long stm = System.currentTimeMillis();
		for ( int i = 0; i < 10000; ++i )
		{
//			System.out.println("this is:" + GlobalStackTrace.GetFullInfo());
			GlobalStackTrace.GetFullInfo();
		}
		System.out.println("usetm:" + (System.currentTimeMillis()-stm));
	}

	
}
