package utils;
import java.util.Random;  
  
public class RandomId {  
    private static Random random = new Random();
    private static String table = "0123456789";  
    private static NumberCaesar m_nc = new NumberCaesar(); 
    public RandomId() {  
       
    }
    
    public static String randomId(int shift) {  
        int key = random.nextInt(10); 
        String num = m_nc.encrypt(table, shift);  
        return (num);  
    }  
}