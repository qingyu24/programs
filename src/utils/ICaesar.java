package utils;

public interface ICaesar {  
    String encrypt(String text, int shiftNum);  
  
    String decrypt(String text, int shiftNum);  
}  