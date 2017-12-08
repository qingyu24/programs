/**
 * RFC.java 2012-6-11下午10:37:19
 */
package core.remote;

import java.lang.annotation.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * Remote Function Call
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface RFC
{
	int ID() default -1;
	boolean RunDirect() default false;
}
