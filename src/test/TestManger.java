package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import logic.eSuanFenType;
import logic.module.room.RoomUser;
import logic.module.room.TestUser;

public class TestManger {

	private static BufferedWriter BufferedWriter = null;
	static boolean is_zimo=false;
	static boolean is_hy=false;
	public static int is_bao=eSuanFenType.SANJIACH.ID();
	public	static long dianpao_id=1;
	public static ArrayList<TestUser> getList(RoomUser r) {
		ArrayList<TestUser> list = TestUser.getSingle().getlist();
		long l=Long.parseLong("10000000000000000");
		list.get(0).set(r);
		list.get(0).set(true,0,0);
		list.get(1).set(l+1,  true, 1, 0);//(id,是不是飘，明杠个数，暗杠个数)
		list.get(2).set(l+2,  false, 0, 0);
		list.get(3).set(l+3,  false, 0, 1);

		return list;


	}
	public static boolean iszimo() {
		return is_zimo;
		// TODO Auto-generated method stub

	}

	public static boolean ishy() {
		return is_hy;
		// TODO Auto-generated method stub

	}

	public static BufferedWriter writer() {
		if(BufferedWriter==null){
			try {
				FileWriter fw=new FileWriter(new File("d:\\score.txt"),true);
				BufferedWriter=new BufferedWriter(fw);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		return BufferedWriter;


	}

	public static void log(String s) throws IOException {
		BufferedWriter bw=TestManger.writer();
		bw.write(s);
		bw.newLine();
		bw.flush();

	}

}
