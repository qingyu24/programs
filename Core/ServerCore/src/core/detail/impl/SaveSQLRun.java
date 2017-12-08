/**
 * SaveSqlRun.java 2012-7-6下午1:45:41
 */
package core.detail.impl;

import core.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SaveSQLRun implements SQLRun
{
	/* (non-Javadoc)
	 * @see core.SQLRun#Execute(core.User)
	 */
	@Override
	public void Execute(User p_User)
	{
		p_User.SaveAllToDB();
	}
}
