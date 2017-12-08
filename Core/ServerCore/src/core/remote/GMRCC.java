/**
 * GMRCC.java 2013-1-18下午5:41:14
 */
package core.remote;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * GM使用的RCC
 */

@Retention(RetentionPolicy.RUNTIME) 
public @interface GMRCC
{
	int ID() default -1;
}
