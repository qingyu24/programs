package utils;

public abstract class Caesar implements ICaesar {  
    private String table = "";  
  
    public Caesar(String table) {  
        this.table = table;  
    }  
  
    protected void setTable(String table) {  
        this.table = table;  
    }  
  
    protected String encryptText(String text, int shiftNum) {  
        int len = table.length();  
  
        StringBuffer result = new StringBuffer();  
        for (int i = 0; i < text.length(); i++) {  
            char curChar = text.charAt(i);  
            int idx = table.indexOf(curChar);  
            if (idx == -1) {  
                result.append(curChar);  
                continue;  
            }  
            idx = ((idx + shiftNum) % len);  
            result.append(table.charAt(idx));  
        }  
  
        return result.toString();  
    }  
  
    @Override  
    public String encrypt(String text, int shiftNum) {  
        // TODO Auto-generated method stub  
        return encryptText(text, shiftNum);  
    }  
  
    @Override  
    public String decrypt(String text, int shiftNum) {  
        // TODO Auto-generated method stub  
        shiftNum = table.length() + (shiftNum % table.length());  
  
        return encryptText(text, shiftNum);  
    }  
} 
