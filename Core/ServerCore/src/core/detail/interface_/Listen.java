/**
 * Listen.java 2012-11-6上午10:51:56
 */
package core.detail.interface_;

import java.io.IOException;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface Listen
{
	 /**
     * 初始化端口
     */
	public void Init(int port) throws IOException;
	
	/**
     * 开始监听,这是个无限循环
     */
    public void StartListen() throws IOException;
    
    /**
     * 出错后的对象清理,以保证下次重新初始化没有问题
     */
    public void Reset();
	
	/**
	 * 移除link
	 */
	public void AddRemoveLinkList(Link l);
	
	/**
	 * 获取当前所有的连接数量
	 */
	public int GetLinkNum();
	
	/**
	 * 停止监听
	 */
	public void Stop();
}
