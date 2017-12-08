/**
 * test.java 2012-6-28下午7:23:52
 */
package test.c;

import java.util.ArrayList;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class test
{
	public static class Template<T>
	{
		private ArrayList<T> m_Value = new ArrayList<T>();
		
		void Add(T v)
		{
			m_Value.add(v);
		}
		
		@SuppressWarnings("unchecked")
		void AddInt()
		{
			Object o = 5;
			m_Value.add((T) o);
		}
		
		@SuppressWarnings("unchecked")
		void AddFloat()
		{
			Object o = 5.6f;
			m_Value.add((T) o);
		}
	}
	
	
	public static void main(String[] args)
	{
		test.Template<Integer> t = new test.Template<Integer>();
		t.Add(5);
		t.AddInt();
		t.AddFloat();
	}

}
