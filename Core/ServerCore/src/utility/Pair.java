/**
 * Pair.java 2012-8-29上午10:57:15
 */
package utility;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 一对值
 */
public class Pair<A,B>
{
	public final A first;
	public final B second;
	
	protected Pair(A first, B second)
	{
		this.first = first;
		this.second = second;
	}
	
	public static <A,B> Pair<A,B> makePair(A first, B second)
	{
		return new Pair<A,B>(first, second);
	}
	
	public String toString()
	{
		return super.toString() + "[" + first + "," + second + "]";
	}
}
