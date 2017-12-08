/**
 * PUNID.java 2012-6-27上午11:28:29
 */
package core.remote;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * param user by netid
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface PUNID
{
	int Index() default -1;
}
