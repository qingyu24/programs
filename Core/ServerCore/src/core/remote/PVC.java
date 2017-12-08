/**
 * PVC.java 2012-7-12下午3:31:39
 */
package core.remote;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ddoq
 * @version 1.0.0
 * 
 * param ArrayList<T>类型, T为从SerialData派生的类型
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PVC
{
	Class<?> Type(); 	///<这个需要设置具体的类型
}
