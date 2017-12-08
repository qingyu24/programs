package utils;

public class WeightRand {
	
	public static int getIndexByWeight(int[] list, int max){
		double r = Math.random() * max;
		for(int i = 0; i < list.length; ++ i){
			if(r < list[i]){
				return i;
			}
		}
		return 0;
	}
}
