/**
 * ExcelData.java 2012-6-10下午12:15:53
 */
package utility;

import java.lang.annotation.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 *{@link ExcelReader#Read(Object)}
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface ExcelData
{
	/**
	 * @return 文件路径
	 */
	String Path() default "data/config/";
	
	/**
	 * @return 文件全名,如abc.xls
	 */
	String File() default "";
	
	/**
	 * @return 读取的表名
	 */
	String Table() default "";
}
