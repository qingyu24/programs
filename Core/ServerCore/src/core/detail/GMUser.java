/**
 * GMUser.java 2013-1-24下午2:25:26
 */
package core.detail;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class GMUser extends UserBase
{
	private int m_GMLevel = -1;
	
	/* (non-Javadoc)
	 * @see core.detail.UserBase#ExecuteKeyDataSQLRun()
	 */
	@Override
	public void ExecuteKeyDataSQLRun() throws Exception
	{
	}

	public int GetGMLevel()
	{
		return m_GMLevel;
	}

	/* (non-Javadoc)
	 * @see core.detail.UserBase#OnAllDataLoadFinish()
	 */
	@Override
	public void OnAllDataLoadFinish() throws Exception
	{
	}
}
