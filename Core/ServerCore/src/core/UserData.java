/**
 * UserData.java 2012-6-24上午9:33:44
 */
package core;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface UserData
{
	/**
	 * 数据是否准备好了
	 *
	 * @return 如果数据准备好了,返回true
	 */
	boolean DataReady() throws Exception;
	
	/**
	 * 如果数据没有准备好,那么要提供可以进行数据加载的对象
	 *
	 * @return 用来进行数据加载的对象
	 */
	SQLRun GetSQLRun() throws Exception;

	/**
	 * 把数据保存到DB中
	 */
	void SaveToDB() throws Exception;
}
