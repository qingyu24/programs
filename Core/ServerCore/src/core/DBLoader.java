/**
 * DBLoader.java 2012-8-24下午2:25:58
 */
package core;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface DBLoader
{
	/**
	 * 当服务器开启时进行加载数据
	 */
	void OnLoad() throws Exception;
	
	/**
	 * 当服务器异常时调用的保存数据,逻辑不应该依赖这个来保存数据
	 * 意思是你最好手动保存数据,比较合适的地方应该在MyUser#SaveAllToDB
	 */
	void OnSave();
}
