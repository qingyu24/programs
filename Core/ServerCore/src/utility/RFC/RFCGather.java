/**
 * RFCGather.java 2012-7-11上午10:09:29
 */
package utility.RFC;

import java.util.*;

import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RFCGather
{
	public class RFCClass
	{
		public String Name;
		public int ID;
		public ArrayList<RFCMethod> Methods = new ArrayList<RFCMethod>();
		public RFCMethod Curr;
	}
	
	public class RFCMethod
	{
		public String Name;
		public int ID;
		public ArrayList<RFCParam> Params = new ArrayList<RFCParam>();
	}
	public class RFCParam
	{
		public RFCParam(RFCType type, String paramName)
		{
			Type = type;
			Name = paramName;
		}
		public RFCType 	Type;
		public String	Name;
		
		public String GetOutput(RFCOutput type)
		{
			return Type.GetName(type) + " " + (Name == null ? "" : Name);
		}
	}
	
	private ArrayList<RFCClass> m_AllClass 	= new ArrayList<RFCClass>();
	private RFCClass			m_Curr;

	public void AddClass(String cname, int classid, boolean save)
	{
		if ( !save ) return;
		RFCClass c = new RFCClass();
		m_Curr = c;
		c.Name = cname;
		c.ID = classid;
		m_AllClass.add(c);
	}

	
	public void AddMethod(String name, int methodid, boolean save)
	{
		if ( !save ) return;
		Debug.Assert(m_Curr != null, "");
		RFCMethod m = new RFCMethod();
		m.Name = name;
		m.ID = methodid;
		m_Curr.Curr = m;
		m_Curr.Methods.add(m);
	}

	public void AddParam(RFCType type, String paramName, boolean save)
	{
		if ( !save ) return;
		Debug.Assert( m_Curr.Curr != null, "" );
		m_Curr.Curr.Params.add(new RFCParam(type, paramName));
	}


	public ArrayList<RFCClass> GetRFCClass()
	{
		return m_AllClass;
	}
}
