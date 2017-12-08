/**
 * GMRFC.java 2013-1-24上午10:25:06
 */
package core.remote;

import java.lang.annotation.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * GM Remote Function Call
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface GMRFC
{
	int ID() default -1;
	int Level() default -1;
	boolean RunDirect() default false;
}
