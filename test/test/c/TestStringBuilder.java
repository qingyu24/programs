/**
 * TestStringBuilder.java 2013-3-13下午12:25:17
 */
package test.c;

import java.util.Formatter;
import java.util.Locale;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestStringBuilder
{
	public static void main(String[] args)
	{
		Formatter f = new Formatter(Locale.CHINESE);
		f.format("开始读取[%s][%s]", "1", "abc");
		StringBuilder s = (StringBuilder) f.out();
		s.insert(0, "###___@@@");
		s.insert(0, "*** ");
		System.out.println(f);
	}
}
