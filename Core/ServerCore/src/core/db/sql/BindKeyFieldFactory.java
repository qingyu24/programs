/**
 * BindKeyFieldFactory.java 2012-8-28下午4:00:58
 */
package core.db.sql;

import java.lang.reflect.*;

import core.db.mysql.*;
import core.exception.FindKeyFieldRuntimeException;

/**
 * @author ddoq
 * @version 1.0.0
 * 
 */
public class BindKeyFieldFactory
{
	/**
	 * 获取一个合适的BindKeyField对象
	 * 
	 * @param c
	 *            用来查询的类型
	 * @param keys
	 *            所有可用的key，哪个在前面就以哪个为key
	 */
	public static KeyField GetAnyBindKeyField(Class<?> c, eFieldKey... keys)
	{
		Field[] fs = c.getFields();
		for ( Field f : fs )
		{
			for ( eFieldKey key : keys )
			{
				if ( f.getName().equals(key.GetName()) )
				{
					return new KeyField(key);
				}
			}
		}
		throw new FindKeyFieldRuntimeException(c, keys);
	}
}
