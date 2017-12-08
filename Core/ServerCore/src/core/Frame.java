/**
 * Frame.java 2012-7-30下午6:01:30
 */
package core;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 为每帧运行的接口
 */
public interface Frame
{
	/**
	 * 每一帧都调用,所以命名为OnUpdate
	 */
	void OnUpdate() throws Exception;
}
