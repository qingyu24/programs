/**
 * RC.java 2012-10-11上午11:12:35
 */
package test.robot;

import java.lang.annotation.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * robot class
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface RC
{
	String Name();
}
