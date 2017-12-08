/**
 * RFCType.java 2012-7-11上午10:52:52
 */
package utility.RFC;

import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum RFCType
{
	Bool,
	Byte,
	Double,
	Float,
	Int,
	Long,
	String,
	Short,
	
	ArrayBool,
	ArrayByte,
	ArrayDouble,
	ArrayFloat,
	ArrayInt,
	ArrayLong,
	ArrayString,
	ArrayShort,
	;

	
	public String GetName(RFCOutput type)
	{
		switch ( type )
		{
		case CShape:
			return _GetCShapeName();
		case Java:
			return _GetJavaName();
		case GMJava:
			return _GetJavaName();
		}
		Debug.Assert(false, "");
		return null;
	}
	
	private String _GetCShapeName()
	{
		switch(this)
		{
		case Bool: return "bool";
		case Byte: return "byte";
		case Double: return "double";
		case Float: return "float";
		case Int: return "int";
		case Long: return "long";
		case String: return "string";
		case Short: return "short";
		
		case ArrayBool: return "bool[]";
		case ArrayByte: return "byte[]";
		case ArrayDouble: return "double[]";
		case ArrayFloat: return "float[]";
		case ArrayInt: return "int[]";
		case ArrayLong: return "long[]";
		case ArrayString: return "string[]";
		case ArrayShort: return "short[]";
		}
		Debug.Assert(false, "");
		return null;
	}
	
	private String _GetJavaName()
	{
		switch(this)
		{
		case Bool: return "boolean";
		case Byte: return "byte";
		case Double: return "double";
		case Float: return "float";
		case Int: return "int";
		case Long: return "long";
		case String: return "String";
		case Short: return "short";
		
		case ArrayBool: return "boolean[]";
		case ArrayByte: return "byte[]";
		case ArrayDouble: return "double[]";
		case ArrayFloat: return "float[]";
		case ArrayInt: return "int[]";
		case ArrayLong: return "long[]";
		case ArrayString: return "String[]";
		case ArrayShort: return "short";
		}
		Debug.Assert(false, "");
		return null;
	}
}
