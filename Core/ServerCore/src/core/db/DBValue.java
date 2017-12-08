/**
 * DBValue.java 2012-6-29下午12:17:23
 */
package core.db;

import java.sql.*;


/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface DBValue
{
	/**
	 * 把值转化为数据库需要的形式
	 */
	public void SetStatement(int index,PreparedStatement s) throws SQLException;
	
	/**
	 * 值是否改变
	 */
	public boolean IsChanged();
	
	/**
	 * 设置值是否改变
	 */
	public void Changed(boolean c);
}
