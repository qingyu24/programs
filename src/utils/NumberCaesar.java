package utils;

public class NumberCaesar extends Caesar {  
	  
    public NumberCaesar() {  
        super("0123456789");  
        // TODO Auto-generated constructor stub  
    }  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        ICaesar numberCaesar = new NumberCaesar();  
  
        
        String encodeStr = numberCaesar.encrypt("456789", 13);  
  
        System.out.println(encodeStr);  
  
        String decodeStr = numberCaesar.decrypt(encodeStr, -13);  
  
        System.out.println(decodeStr);  
  
    }  
  
}  