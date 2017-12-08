package utility;

public class LegalityCheck
{
	// 检测名字的长度合法性(maxsz个字符以内,一个汉字按两个字符算)
	static public int CheckStrLengthLegal(String p_str, int maxsz)
	{
		int bRes = 1; // 名称长度合法
		if(p_str.isEmpty())
		{
			bRes = -1; // 名称为空
		}
		else
		{
			int nEngcharCount =0;
			int chineseCount = 0;
			int chineseByte = 0;
		    for(char ch : p_str .toCharArray())
			{	      
				if(ch >= 'a' && ch<='z' || ch>='A' && ch<='Z')
				{
					nEngcharCount +=1;
				}
				else if(ch >= '0' && ch <= '9')
				{
					nEngcharCount +=1;
				}
				else if(ch >= 0x4e00 && ch<= 0x9fa5)
				{
					chineseCount += 1;
	                chineseByte = chineseCount*2;
				}

	            if (nEngcharCount + chineseByte > maxsz)
	            {
	            	bRes = -2; // 名称超过4个汉字或8个字符
	                break;
	            }
			}
		}
		return bRes;
	}
}
