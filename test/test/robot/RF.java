/**
 * RF.java 2012-10-11上午11:13:54
 */
package test.robot;

import java.lang.annotation.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * robot function
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface RF
{
	String Name();
}
