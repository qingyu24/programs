package logic.config.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.config.IConfig;
import logic.config.MaskWordConfig;

public class MaskWordHandler implements IHandler
{
	private static List<String> m_MaskWords = new ArrayList<String>();
	
	@Override
	public void Init(IConfig[] val)
	{
		
		MaskWordConfig[] list = (MaskWordConfig[])(val);
		for(MaskWordConfig data : list)
		{
			if(data == null)
				continue;
			if(data.word != null && !data.word.isEmpty())
				m_MaskWords.add(data.word);
			if(data.word1 != null && !data.word1.isEmpty())
				m_MaskWords.add(data.word1);
			if(data.word2 != null && !data.word2.isEmpty())
				m_MaskWords.add(data.word2);
			if(data.word3 != null && !data.word3.isEmpty())
				m_MaskWords.add(data.word3);
		}
	}
	
	public static boolean CheckMaskWords(String p_sWords)
	{
		if (p_sWords.isEmpty())
			return true;
		
		for(String word : m_MaskWords)
		{
			if (null != word)
			{
				int nK = p_sWords.indexOf(word);
				if (-1 != nK)
				{
					return false;
				}
			}
			else
			{
				System.out.println("检查屏蔽字：数据为空！");
			}
		}
		return true;
	}
	
	public static boolean CheckIllegalWord(String p_str)
	{
		Pattern p = Pattern.compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_.\u4e00-\u9fa5]+$");
		Matcher m = p.matcher(p_str);
		return m.find();
	}
}
