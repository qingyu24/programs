/**
 * ReadTest.java 2012-8-15上午11:40:42
 */
package mysql;

import java.sql.*;

import utility.Rand;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ReadTest
{
static int  count=100000;//总次数  
    
    //一定要写rewriteBatchedStatements参数，Mysql批量插入才性能才理想  
    static String mySqlUrl="jdbc:mysql://192.168.3.9:3306/robotdb?";  
    static String mySqlUserName="root";    
    static String mySqlPassword="123";  
    
    static String initsql = "TRUNCATE TABLE TESTDATA";
      
    static String sql = "select * from logindata where UserName=?";   
    
	public static void main(String[] args)
	{
        test_mysql(1);
	}
	
	/** 
     * 创建连接 
     * @return 
     */  
    public static Connection getConn(String flag)
    {  
        long a=System.currentTimeMillis();  
        try 
        {          
            if("mysql".equals(flag))
            {  
                Class.forName("com.mysql.jdbc.Driver");          
                Connection conn =  DriverManager.getConnection(mySqlUrl, mySqlUserName, mySqlPassword);       
//                conn.setAutoCommit(false);    
                return conn;  
            }
            else
            {  
                System.out.println();  
                throw new RuntimeException("flag参数不正确,flag="+flag);  
            }  
        } 
        catch (Exception ex) 
        {    
            ex.printStackTrace();    
        }
        finally
        {    
            long b=System.currentTimeMillis();    
            System.out.println("创建连接用时"+ (b-a)+" ms");   
        }  
        return null;  
    }  
    /** 
     * 关闭连接 
     * @return 
     */  
    public static void close(Connection conn)
    {  
         try 
         {    
             if(conn!=null)
             {  
                 conn.close();    
             }  
         } 
         catch (SQLException e) 
         {    
             e.printStackTrace();    
         }  
    }
    
    /** 
     * 打印信息 
     * @return 
     */  
    public static void print(String key,long startTime,long endTime,int point)
    {  
        System.out.println("每执行"+point+"次sql提交一次事务");  
        System.out.println(key+"，用时"+ (endTime-startTime)+" ms,平均每秒执行"+(count*1000/(endTime-startTime))+"条");  
        System.out.println("----------------------------------");  
    }  
    /**  
     * mysql非批量插入10万条记录  
     */    
    public static void test_mysql(int point)
    {    
        Connection conn=getConn("mysql");    
        try 
        {          
              PreparedStatement prest = conn.prepareStatement(sql);          
              long a=System.currentTimeMillis();    
              for(int x = 1; x <= count; x++)
              {
//            	  prest.setString(1, "robot"+Rand.GetIn100()); 
            	  prest.setString(1, "robot"+Rand.Get()); 
            	  prest.executeQuery();
              }          
              long b=System.currentTimeMillis();    
              print("MySql读取" + count + "条记录",a,b,point);  
        } 
        catch (Exception ex) 
        {    
            ex.printStackTrace();    
        }
        finally
        {    
            close(conn);      
        }    
    }    
}
