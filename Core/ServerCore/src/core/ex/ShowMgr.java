/**
 * ShowMgr.java 2012-7-23下午3:13:06
 */
package core.ex;

import core.RootConfig;
import core.detail.SystemFn;
import utility.dyjar.DynamicLoadJarFile;


/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ShowMgr
{
	private static GatherInfo m_Info = new GatherInfo();
	private static boolean m_Init = false;
	
	public static boolean CanShow()
	{
		return RootConfig.GetInstance().Show;
	}
	
	public static void Run()
	{
//		Debug.Assert(m_Show == null, "");
//		m_Show = new Show();
//		m_Show.getFrame().setVisible(true);
		
		if ( CanShow() && m_Init == false )
		{
			m_Info.RegTimer();
			m_Init = true;
			Object show = DynamicLoadJarFile.GetClassObj("exjar/show.jar", "ex.Show", "exjar/jcommon-1.0.17.jar", "exjar/jfreechart-1.0.14.jar");
			if ( show != null )
			{
				Object panel = SystemFn.GetFieldValue(show, "totalPanel");
				m_Info.Init(show, panel);
			}
		}
	}
	
	public static GatherInfo GetInfo()
	{
		return m_Info;
	}
}
