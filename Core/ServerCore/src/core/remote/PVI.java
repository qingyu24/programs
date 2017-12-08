/**
 * PVI.java 2012-7-12下午3:30:05
 */
package core.remote;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * param ArrayList<T>类型, T为内部类型(Internal),内部类型包括我们普通的类型和String
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PVI
{
	
}
