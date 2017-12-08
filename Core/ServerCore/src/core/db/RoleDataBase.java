/**
 * RoleDataBase.java 2012-6-30下午2:25:08
 */
package core.db;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RoleDataBase
{
	private boolean	Flag;	///<是否内存数据,这个字段的名字不允许修改,在SystemFn#SetRoleDataBaseFlag中有设置
	
	public boolean GetFlag()
	{
		return Flag;
	}
}
