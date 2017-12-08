/**
 * BindInt.java 2013-4-20下午7:31:18
 */
package core.ex.bind;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class BindInt extends Bind
{
	public BindInt(String bindname, Object bindobj)
	{
		super(bindname, bindobj);
	}

	public void Set(int v)
	{
		super.SetIntNumForamt(v);
	}
}
