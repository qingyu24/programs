/**
 * PU.java 2012-6-11下午10:38:12
 */
package core.remote;

import java.lang.annotation.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * param ref user
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface PU
{
	int Index() default -1;
}
