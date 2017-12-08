/**
 * ExcelReader.java 2012-6-10下午12:22:05
 */
package utility;

import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;

import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;

import jxl.*;



/**
 * @author ddoq
 * @version 1.0.0
 * 
 * 用来读取Excel文件
 */
public class ExcelReader
{
	/**
	 * 读取数据
	 * 
	 * <br>测试代码:{@link TestExcelReader}
	 * 
	 * @param p_ExcelData
	 *            一个欲设定的excel数据结构,每行数据都会读入到这个结构,这个类型必须有{@link ExcelData}的注释
	 * @return 读取到的所有记录
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] Read(T p_ExcelData)
	{
		Class<?> c = p_ExcelData.getClass();
		ExcelData d = c.getAnnotation(ExcelData.class);
		if (d == null)
		{
			System.out.println("# 无法在["+c.getName()+"]中找到注释@ExcelData");
			return null;
		}
		String fname = d.Path() + d.File();
		String tbname = d.Table();
		String[] sv = tbname.split(",");
		Sheet[] ss = new Sheet[sv.length];
		int datanum = 0;
		try
		{
			for ( int i = 0; i < sv.length; ++i )
			{
//				System.out.println("# 开始读取["+fname+"]["+sv[i]+"]");
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_READ_CONFIG_FILE, fname, sv[i]);
				File f = new File(fname);
				InputStream is = new FileInputStream(f);
				Workbook wb = Workbook.getWorkbook(is);
				Sheet s = wb.getSheet(sv[i]);
				ss[i] = s;
				Debug.Assert(s.getRows() >= 1, "Excel中必定有数据");
				int rs = 0;
				for ( int j = 2; j < s.getRows(); ++j )
				{
					if ( s.getRow(j).length == 0 )
					{
						continue;
					}
					rs++;
				}
				datanum += rs;
			}
			
			Field[] fs = c.getDeclaredFields();
			fs = _GetFields(fs);
			T[] array = (T[]) Array.newInstance(c, datanum);
			int pos = 0;
			for ( Sheet s : ss )
			{
				System.out.print("数据长度:" + s.getRows());
				for (int i = 2; i < s.getRows(); ++i )
				{
					Cell[] cs = s.getRow(i);
					
					if ( cs.length == 0)
					{
						continue;
					}
					T t = (T) c.newInstance();
					_SerializeData(t, fs, cs, 0, s.getRow(2));
					array[pos++] = t;
				}
			}
//			System.out.println("# 读取完成["+fname+"]["+tbname+"]");
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_READ_CONFIG_FILE_FINISHED, fname, tbname);
			return array;
		}
		catch (Exception e)
		{
			Log.out.LogException(e);
		}
		
		return null;
	}
	
	private static <T> int _SerializeData(T t, Field[] fs, Cell[] cs, int index, Cell[] head) throws Exception
	{
		for ( Field f : fs )
		{
			if ( index >= cs.length )
			{
				break;
			}
			if ( f.getModifiers() == 4234 )
			{
				break;
			}
			boolean c = _SerializeField(t, f, cs[index]);
			if ( !c )
			{
				index = _SerializeCustomField(t, f, cs, index, head);
			}
			else
			{
				index++;
			}
		}
		return index;
	}
	
	/**
	 * 序列化字段的值
	 * 
	 * @param t
	 *            要序列化的对象
	 * @param f
	 *            要序列化的字段
	 * @param c
	 *            从excel中读取到的数据
	 *            
	 * @return	反馈是否序列化成功
	 */
	private static <T> boolean _SerializeField(T t, Field f, Cell c) throws Exception
	{
		Type type = f.getGenericType();
//		System.out.println("[" + f + "][" + c.getContents() + "]");
		if(type == boolean.class)
		{
			f.setBoolean(t, Str.GetBoolean(c.getContents()));
		}
		else if (type == byte.class)
		{
			f.setByte(t, Str.GetByte(c.getContents()));
		}
		else if (type == short.class)
		{
			f.setShort(t, Str.GetShort(c.getContents()));
		}
		else if (type == int.class)
		{
			f.setInt(t, Str.GetInt(c.getContents()));
		}
		else if (type == long.class)
		{
			f.setLong(t, Str.GetLong(c.getContents()));
		}
		else if (type == float.class)
		{
			f.setFloat(t, Str.GetFloat(c.getContents()));
		}
		else if (type == double.class)
		{
			f.setDouble(t, Str.GetDouble(c.getContents()));
		}
		else if (type == String.class)
		{
			f.set(t, Str.GetString(c.getContents()));
		}
		else
		{
			return false;
		}
		return true;
	}
	
	@SuppressWarnings({ "unchecked" })
	private static <T> int _SerializeCustomField(T t, Field f, Cell[] cs, int index, Cell[] head) throws Exception
	{
		if ( !f.getType().isArray() )
		{
			Object o = f.getType().newInstance();
			int startindex = index;
			Field[] fs = f.getType().getDeclaredFields();
			fs = _GetFields(fs);
			int flength = fs.length;
			index = _SerializeData(o, fs, cs, index, head);
			if ( index - startindex == flength )
			{
				f.set(t, o);
			}
			else
			{
//				f.set(t, null);
				throw new RuntimeException("读取Excel时无法序列化结构:" + f.getName() + " index:" + index + " startindex:" + startindex + " len:" + flength);
			}
		}
		else
		{
			Class<?> c = f.getType().getComponentType();
			Field[] fs = c.getDeclaredFields();
			fs = _GetFields(fs);
			int len = _GetArrayLength(fs.length, head, index);
			T[] ts = (T[]) Array.newInstance(c, len);
			int flength = fs.length;
			for ( int i = 0; i < len; ++i )
			{
				T o = (T) c.newInstance();
				int startindex = index;
				index = _SerializeData(o, fs, cs, index, head);
				if ( index - startindex == flength )
				{
					ts[i] = o;
				}
				else
				{
					ts[i] = null;
				}
			}

			f.set(t, ts);
		}
		return index;
	}
	
	/**
	 * 使用单元测试工具时,会在我们的数据结构中插入一些结构,会导致直接取会有问题
	 */
	private static Field[] _GetFields(Field[] fs)
	{
		ArrayList<Field> all = new ArrayList<Field>();
		for ( Field f : fs )
		{
			if ( f.getModifiers() == 1 )
			{
				all.add(f);
			}
		}
		return all.toArray(new Field[]{});
	}
	
	private static int _GetArrayLength(int num, Cell[] head,int index)
	{
		String[] ss = new String[num];
		for ( int i = 0; i < num; ++i )
		{
			ss[i] = head[index+i].getContents();
		}
		int sz = 1;
		for ( int i = index + num; i < head.length; )
		{
			boolean check = true;
			for ( int j = 0; j < ss.length; ++j )
			{
				if (!head[i+j].getContents().contains(ss[j]))
				{
					check = false;
					break;
				}
			}
			if ( check )
			{
				sz++;
				i += ss.length;
			}
			else
			{
				break;
			}
		}
		return sz;
	}
}
