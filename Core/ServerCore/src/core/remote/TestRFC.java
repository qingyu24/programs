/**
 * TestRFC.java 2013-2-26上午11:39:41
 */
package core.remote;

import java.lang.annotation.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface TestRFC
{
	int ID() default -1;
	boolean RunDirect() default false;
}
