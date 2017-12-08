/**
 * test1.java 2012-7-12下午4:25:34
 */
package test.c;

import java.lang.reflect.*;
import java.util.List;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class test1
{

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param args
	 */
	public static void main(String[] args)
	{
		Method[] methods = test1.class.getDeclaredMethods();
		for ( Method m : methods )
		{
			System.out.println("return type:");
			Type returnType = m.getGenericReturnType();
			if ( returnType instanceof ParameterizedType )
			{
				Type[] types = ((ParameterizedType)returnType).getActualTypeArguments();
				for ( Type t : types )
				{
					System.out.println(t);
				}
			}
		}
	}

	public static List<String> test3(List<Integer> list)
	{
		return null;
	}
}
