/**
 * InsertTest.java 2012-8-14下午3:02:08
 */
package mysql;

import java.sql.*;  

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class InsertTest
{
	static int  count = 100000;//总次数  
    
    //一定要写rewriteBatchedStatements参数，Mysql批量插入才性能才理想  
    static String mySqlUrl="jdbc:mysql://192.168.3.200:3306/db?rewriteBatchedStatements=true";  
    static String mySqlUserName="root";    
    static String mySqlPassword="123";    
      
//    static String sql = "insert into vipdiamonddata(GID,RoleID,Diamond,Time) values(?,?,?,0)";   
//    static String sql = "INSERT INTO AttributeData(RoleID,Stamina,Money,Diamond,GiftTicket,Badge,Reputation,Sophisticate,Achievement,CurScene,CurNorBattleID,CurEcsBattleID,MainCityWeather,CurBattleArrayID,SpiritRemain,PhaseProgress,VertexProgress,LastGetStamina,HL_CurrentLevel,HL_LevelProgress,HL_NpcProgress,HL_ChallengeTable)  VALUES(?,100,6510,0,0,0,300,0,0,1001,10006,0,'1001,6;1002,7;1003,3;1004,3;1005,2;',1,0,0,0,0,1,1,1,0)";
//    static String sql =   "INSERT INTO ItemData(GID,RoleID,CID,Num,nWhere,Slot,Strength,TreasurePos,IsDelete)  VALUES(?,?,101001,1,2,0,0,0,0)";
    static String sql = "INSERT INTO Test(ID,RoleID) VALUES(?,?)";
    
    //每执行几次提交一次  
//    static int[] commitPoint={count,10000,1000,100,10,1}; 
    static int[] commitPoint={100,50,10}; 
    
	public static void main(String[] args)
	{
//		for(int point:commitPoint)
//		{  
//            test_mysql(point);    
//        }  
//        for(int point:commitPoint)
//        {  
//            test_mysql_batch(point);    
//        }  
//        
//        
//        test_mysql_batch(10);
//        test_mysql_batch(50);
//        test_mysql_batch(100);
        
        test_mysql();
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
                conn.setAutoCommit(false);    
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
     * 删除旧数据 
     * @return 
     */  
    public static void clear(Connection conn)
    {  
        try
        {  
            Statement st=conn.createStatement();  
            boolean bl=st.execute("delete FROM vipdiamonddata");  
//            conn.commit();  
            st.close();  
            System.out.println("执行清理操作："+(bl==false?"成功":"失败"));  
        }
        catch(Exception e)
        {
            e.printStackTrace();  
        }  
    }  
    /** 
     * 打印信息 
     * @return 
     */  
    public static void print(String key,long startTime,long endTime)
    {  
//        System.out.println("每执行"+point+"次sql提交一次事务");  
        System.out.println(key+"，用时"+ (endTime-startTime)+" ms,平均每秒执行"+(count*1000/(endTime-startTime))+"条");  
        System.out.println("----------------------------------");  
    }  
    /**  
     * mysql非批量插入10万条记录  
     */    
    public static void test_mysql()
    {    
        Connection conn=getConn("mysql");    
        clear(conn);  
        try 
        {          
              PreparedStatement prest = conn.prepareStatement(sql);          
              long a=System.currentTimeMillis();    
              for(int x = 1; x <= count; x++)
              {
//            	  prest.setLong(1, Rand.Get());
//            	  prest.setLong(2, 9999999+x);
            	  prest.setLong(1, x);
            	  prest.setLong(2, x % 1000 + 2000);
                 try
                 {
                	 prest.execute();
                 }
                 catch(SQLException e)
                 {
                	 e.printStackTrace();
                 }
                 if ( x % 1000 == 0 )
                 {
                	 conn.commit();
                	 System.out.println("提交了" + x + "条记录");
                 }
              }  
              conn.commit();
              long b=System.currentTimeMillis();    
              print("MySql非批量插入" + count + "万条记录",a,b);  
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
      
    /**  
     * mysql批量插入10万条记录  
     */    
//    public static void test_mysql_batch(int point)
//    {    
//        Connection conn=getConn("mysql");    
//        clear(conn);  
//        try 
//        {          
//            PreparedStatement prest = conn.prepareStatement(sql);          
//            long a=System.currentTimeMillis();    
//            for(int x = 1; x <= count; x++)
//            {          
//            	try
//            	{
//            		prest.setLong(1, x);
//            		prest.setLong(2, x+1);
//            		prest.setLong(3, x+2);
//	                prest.addBatch();      
//	                if(x%point==0)
//	                {  
//	                    prest.executeBatch();        
//	                }
//            	}
//            	catch(SQLException e)
//            	{
//            		e.printStackTrace();
//            	}
//            }          
//            long b=System.currentTimeMillis();    
//            print("MySql批量插入"+ count + "万条记录",a,b,point);  
//        } 
//        catch (Exception ex) 
//        {    
//            ex.printStackTrace();    
//        }
//        finally
//        {    
//            close(conn);      
//        }    
//    }    
}
