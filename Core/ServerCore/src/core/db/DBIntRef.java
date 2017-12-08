/**
 * DBIntRef.java 2013-3-16上午10:36:30
 */
package core.db;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DBIntRef extends DBInt
{
	private DBInt m_Value;
	
	public DBIntRef(DBInt v)
	{
		m_Value = v;
	}
	
	public void Set(int v)
	{
		m_Value.Set(v);
	}
	
	public int Get()
	{
		return m_Value.Get();
	}
	
	@Override
	public boolean IsChanged()
	{
		return false;
	}
	
	@Override
	public void Changed(boolean c)
	{
	}
}
