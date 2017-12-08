/**
 * EmptyPrintStream.java 2012-12-22上午11:18:56
 */
package core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class EmptyPrintStream extends PrintStream
{
	public static class EmptyOutputStream extends OutputStream
	{

		/* (non-Javadoc)
		 * @see java.io.OutputStream#write(int)
		 */
		@Override
		public void write(int b) throws IOException
		{
		}
	}
	
	public static EmptyPrintStream m_Instance = new EmptyPrintStream();
	
	private PrintStream m_Stream = System.out;
	
	public EmptyPrintStream()
	{
		super(new EmptyOutputStream());
	}

	public void Reset()
	{
		System.setOut(m_Stream);
	}
}
