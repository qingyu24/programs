/**
 * SQLResultParse.java 2013-7-4下午2:35:42
 */
package core.db.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface SQLResultParse
{
	void Parse(ResultSet r) throws SQLException;
}
