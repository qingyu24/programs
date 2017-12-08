/**
 * MyRoot.java 2012-6-11涓嬪崍9:48:17
 */
package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

import core.DBMgr;
import core.Root;
import core.RootConfig;
import core.Tick;
import logic.config.MahJongConfig;
import logic.config.handler.MahJongHandler;
import logic.module.room.CardInfo;
import logic.module.room.RoomUser;
import manager.CardManager;
import manager.ConfigManager;
import manager.LoaderManager;
import manager.RoomManager;
import utility.TimeMethod;
import utility.dyjar.DynamicLoadJarFile;

/**
 * @author ddoq
 * @version 1.0.0
 * <p>
 * 姝ｅ父鏈嶅姟鍣ㄧ殑鍚姩妯″潡
 */
public class MyRoot extends Root implements Tick {
    public MyRoot() {
        super();
        m_Factory = new MyFactory();

    }

    public void RegAll() {
        super.RegAll();
    }

    public void StartAllThread() {
        super.StartAllThread();
    }

    public void StartShow() {
        super.StartShow();
    }

    public void RFCBuild() {
        super.RFCBuild();
    }

    /**
     * 鍏ュ彛
     *
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        //		System.setOut(EmptyPrintStream.m_Instance);

        System.err.println("Server go~~~");

        RootConfig c = RootConfig.GetInstance();
        c.Init();

        TimeMethod.Init();

        if (c.OpenSecurityService) {
            //鑷姩寮?瀹夊叏鍗忚鏈嶅姟鍣?
            DynamicLoadJarFile.ThreadRunJarClassMain("SecurityPolicyServer.jar", "sec.SecurityPolicyServer");
        }

        //sql杩炴帴
        DBMgr.Init();

        //閫昏緫灞傜殑娉ㄥ唽閰嶇疆
        ConfigManager.getInstance().initAll();

        MyRoot r = new MyRoot();
        r.RegAll();

        if (c.Debug) {
            r.RFCBuild();
        }

        if (c.Show) {
            r.StartShow();
        }

        LoaderManager.getInstance().loadAll();
        RoomManager.initRoomId();

        DBMgr.LoadAll();

        //鎶婃満鍣ㄤ汉鎻掑叆鍒板簱閲?;
        r.StartAllThread();


        String time = readFileByLines("Server.ini");
        if (null == time) {
            FileOutputStream output = new FileOutputStream("Server.ini");
            OutputStreamWriter osw = null;
            try {
                osw = new OutputStreamWriter(output, "UTF-8");
                long now = System.currentTimeMillis();
                osw.write(String.valueOf(now));
                osw.flush();
                osw.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (osw != null) {
                    try {
                        osw.close();
                    } catch (IOException e1) {
                    }
                }
            }

        } else {
            System.out.println("服务器启动时间:" + time);
            ConfigManager.serverStartRunTime = Long.parseLong(time);
        }
        //璁板綍涓?涓嬫父鎴忕殑寮?鏈嶆椂闂?;

        System.err.println("Server Start Finish!!!");

		/*
        for(int i = 0; i < 100; ++ i){
			System.out.print(RandomId.randomId(i) + "\n");
		}
		 */
        new CardManager().createCardGroup(12);


        synchronized (Root.GetInstance()) {
            try {
                Root.GetInstance().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Root.GetInstance().StopMainThread();
    }

    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 涓?娆¤鍏ヤ竴琛岋紝鐩村埌璇诲叆null涓烘枃浠剁粨鏉?
            while ((tempString = reader.readLine()) != null) {
                // 鏄剧ず琛屽彿
                //System.out.println("line " + line + ": " + tempString);
                //line++;
                return tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }


    /* (non-Javadoc)
     * @see core.Tick#OnTick(long)
     */
    @Override
    public void OnTick(long p_lTimerID) throws Exception {
        System.out.println("OnTick : " + p_lTimerID + " time:" + System.currentTimeMillis());
    }
}
