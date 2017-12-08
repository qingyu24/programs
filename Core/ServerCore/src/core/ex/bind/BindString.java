/**
 * BindString.java 2013-4-20下午7:33:13
 */
package core.ex.bind;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class BindString extends Bind
{

	public BindString(String bindname, Object bindobj)
	{
		super(bindname, bindobj);
	}
	
	public void Set(String s)
	{
		super.SetString(s);
	}

}
