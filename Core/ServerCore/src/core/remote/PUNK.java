/**
 * PUNK.java 2012-6-27上午11:27:05
 */
package core.remote;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * param user by nick
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface PUNK
{
	int Index() default -1;
}
