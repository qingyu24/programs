/**
 * BindFloat.java 2013-4-20下午7:32:52
 */
package core.ex.bind;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class BindFloat extends Bind
{
	public BindFloat(String bindname, Object bindobj)
	{
		super(bindname, bindobj);
	}
	
	public void Set(float f)
	{
		super.SetFloatFormat(f);
	}
}
