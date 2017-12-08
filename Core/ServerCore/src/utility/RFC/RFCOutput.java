/**
 * RFCOutput.java 2012-7-11上午11:33:01
 */
package utility.RFC;

//import test.robot.Robot;
import utility.*;
import utility.RFC.RFCGather.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public enum RFCOutput
{
	CShape,
	Java,
	GMJava,
	;

	public String GetFileName(String mark)
	{
		switch(this)
		{
		case CShape:
			return "_ConnectionManager" + mark + ".cs";
		case Java:
			return "test/test/robot/RFCFn" + mark + ".java";
		case GMJava:
			return "GMRFCFn" + mark + ".java";
		default:
			Debug.Assert(false, "");
		}
		return null;
	}

	
	public String GetClassHead(String mark)
	{
		switch(this)
		{
		case CShape:
			return "using Networks;\r\npublic static class _ConnectionManager" + mark + "\r\n";
		case Java:
			return "package test.robot;\r\n\r\npublic class RFCFn" + mark + "\r\n";
		case GMJava:
			return "package test.robot;\r\n\r\npublic class RFCFn" + mark + "\r\n";
		default:
			Debug.Assert(false, "");
		}
		return null;
		
	}

	public String GetMethod(RFCClass c, RFCMethod m)
	{
		switch(this)
		{
		case CShape:
			return _GetCShapMethod(c, m);
		case Java:
			return _GetJavaMethod(c, m);
		case GMJava:
			return _GetJavaMethod(c, m);
		default:
			Debug.Assert(false, "");	
		}
		return null;
	}
	
	private String _GetCShapMethod(RFCClass c, RFCMethod m)
	{
		String mstr = "\tstatic public void " + c.Name + "_" + m.Name + "(this ConnectionManager mgr";
		for ( RFCParam p : m.Params )
		{
			mstr += ", " + p.GetOutput(this);
		}
		mstr += ")\r\n";
		mstr += "\t{\r\n";
		mstr += "\t\tPacketBuffer.GetInstance().Clear().SetObjMethod(";
		mstr += c.ID;
		mstr += ",";
		mstr += m.ID;
		mstr += ")";
		for ( RFCParam p : m.Params )
		{
			mstr += ".Add(" + p.Name + ")";
		}
		mstr += ".Send(mgr);\r\n";
		mstr += "\t}\r\n\r\n";
		return mstr;
	}
	
	private String _GetJavaMethod(RFCClass c, RFCMethod m)
	{
		String mstr = "\tpublic static void " + c.Name + "_" + m.Name + "(Robot r";
		
		for ( RFCParam p : m.Params )
		{
			mstr += ", " + p.GetOutput(this);
		}
		mstr += ")\r\n";
		mstr += "\t{\r\n";
		mstr += "\t\tr.GetSendBuffer().Clear().AddID(";
		mstr += c.ID;
		mstr += ",";
		mstr += m.ID;
		mstr += ")";
		for ( RFCParam p : m.Params )
		{
			mstr += ".Add(" + p.Name + ")";
		}
		mstr += ".Send(r.GetLink());\r\n";
		mstr += "\t}\r\n\r\n";
		
		return mstr;
	}
}
