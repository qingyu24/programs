package utils;

public class LetterCaesar extends Caesar {  
	  
    private final static String upperLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";  
    private final static String lowerLetter = "abcdefghijklmnopqrstuvwxyz";  
  
    public LetterCaesar() {  
        super(upperLetter);  
        // TODO Auto-generated constructor stub  
    }  
  
    private void checkTextTypeAndSetTable(String text) {  
        char cur = text.charAt(0);  
  
        if (Character.isUpperCase(cur)) {  
            setTable(upperLetter);  
        } else {  
            setTable(lowerLetter);  
        }  
    }  
  
    @Override  
    public String encrypt(String text, int shiftNum) {  
        // TODO Auto-generated method stub  
        checkTextTypeAndSetTable(text);  
  
        return super.encrypt(text, shiftNum);  
    }  
  
    @Override  
    public String decrypt(String text, int shiftNum) {  
        // TODO Auto-generated method stub  
  
        checkTextTypeAndSetTable(text);  
  
        return super.decrypt(text, shiftNum);  
    }  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        ICaesar numberCaesar = new LetterCaesar();  
  
        String encodeStr = numberCaesar  
                .encrypt("ABCDEFGHIGJLMNOPQRSTUVWXYZ", 3);  
        System.out.println(encodeStr);  
        String decodeStr = numberCaesar.decrypt(encodeStr, -3);  
        System.out.println(decodeStr);  
  
        encodeStr = numberCaesar.encrypt("abcdefghijklmnopqrstuvwxyq", 3);  
        System.out.println(encodeStr);  
  
        decodeStr = numberCaesar.decrypt(encodeStr, -3);  
        System.out.println(decodeStr);  
    }  
  
} 