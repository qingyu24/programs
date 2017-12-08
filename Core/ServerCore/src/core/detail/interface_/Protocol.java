/**
 * Protocol.java 2012-7-9上午11:24:58
 */
package core.detail.interface_;

import java.nio.ByteBuffer;

import core.detail.impl.socket.TransBuffer;


/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface Protocol
{
	void Create(Link p_Link);

	boolean Init();
	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param buffer
	 * @return
	 * @throws Exception 
	 */
	boolean Resolve(TransBuffer buffer, Link p_Link) throws Exception;

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param buffer
	 * @return
	 */
	ByteBuffer Pack(ByteBuffer buffer);
}
