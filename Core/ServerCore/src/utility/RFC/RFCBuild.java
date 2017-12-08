/**
 * RFCBuild.java 2012-7-11上午10:09:12
 */
package utility.RFC;

import java.io.*;

import utility.RFC.RFCGather.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class RFCBuild
{
	public static void Build(RFCGather gather, RFCOutput type, String mark)
	{
		String filename = type.GetFileName(mark);
		
		try
		{
			FileOutputStream output = new FileOutputStream(filename);
			OutputStreamWriter osw = new OutputStreamWriter(output, "UTF-8");
			osw.write(type.GetClassHead(mark));
			osw.write("{\r\n");
			
			for ( RFCClass c : gather.GetRFCClass() )
			{
				for ( RFCMethod m : c.Methods )
				{
					osw.write(type.GetMethod(c, m));
				}
			}
			osw.write("}\r\n");
			
			osw.flush();
			osw.close();
			System.out.println("# 成功创建文件[" + filename + "]");	
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
}
