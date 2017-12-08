/**
 * RCC.java 2012-6-11下午10:33:42
 */
package core.remote;

import java.lang.annotation.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * Remote Class Call
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface RCC
{
	int ID() default -1;
}
