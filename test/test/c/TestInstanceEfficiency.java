/**
 * TestInstanceEfficiency.java 2012-10-17下午3:27:46
 */
package test.c;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestInstanceEfficiency
{
	public static interface A
	{
		void OnCall();
	}
	
	public static class A1 implements A
	{
		public void OnCall()
		{
			
		}
		
		public void OnCall1()
		{
			
		}
	}
	
	public static class A2 implements A
	{
		public void OnCall()
		{
			
		}
		
		public void OnCall2()
		{
			
		}
	}
	
	public static void main(String[] args)
	{
		final int num = 10000 * 10000;
		A a = new A1();
		
		long stm = System.currentTimeMillis();
		for ( int i = 0; i < num; ++i )
		{
			a.OnCall();
		}
		System.out.println("use tm:" + (System.currentTimeMillis() - stm));
		
		stm = System.currentTimeMillis();
		for ( int i = 0; i < num; ++i )
		{
			if ( a instanceof A1 )
			{
				((A1) a).OnCall1();
			}
		}
		System.out.println("use tm:" + (System.currentTimeMillis() - stm));
	}

}
