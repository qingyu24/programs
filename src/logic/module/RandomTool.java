package logic.module;

import java.util.Random;

public class RandomTool 
{
	static Random random =new Random(100000);
	
	public static int GetRandom(int min,int max)
	{				
		if(min == max)
			return min;
		
		int fvalue = (int)(random.nextFloat() * max) + min;					
		
		return fvalue;
	}
	
	public static float GetRandom(float min,float max)
	{				
		if(min == max)
			return min;
		
		float fvalue = (random.nextFloat() * max) + min;					
		
		return fvalue;
	}
}
