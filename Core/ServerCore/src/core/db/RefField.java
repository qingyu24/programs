/**
 * RefField.java 2012-8-24下午3:04:35
 */
package core.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface RefField
{
	String Bind() default "";
}
