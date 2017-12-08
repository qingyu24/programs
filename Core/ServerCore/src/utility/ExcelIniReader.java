/**
 * ExcelIniReader.java 2012-6-19上午11:10:05
 */
package utility;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;

import jxl.*;

/**
 * @author ddoq
 * @version 1.0.0
 * 
 */
public class ExcelIniReader
{
	/**
	 * 以INI的方式来读取一个excel文件
	 * 
	 * <br>
	 * 测试代码:{@link TestExcelIniReader}
	 * 
	 * @param p_ExcelData
	 *            一个预设定的结构,根据结构的字段获取设置的值
	 */
	public static <T> void Read(T p_ExcelData)
	{
		Class<?> c = p_ExcelData.getClass();
		ExcelData d = c.getAnnotation(ExcelData.class);
		if (d == null)
		{
			System.out.println("# 无法在[" + c.getName() + "]中找到注释@ExcelData");
			return;
		}
		String fname = d.Path() + d.File();
		String tbname = d.Table();
		//Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_READ_CONFIG_FILE, fname, tbname);

		try
		{
			File f = new File(fname);
			InputStream is = new FileInputStream(f);
			Workbook wb = Workbook.getWorkbook(is);
			Sheet s = wb.getSheet(tbname);
			int rownum = s.getRows();
			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			for (int i = 0; i < rownum; ++i)
			{
				Cell[] cs = s.getRow(i);
				if (cs.length >= 2)
				{
					String key = cs[0].getContents();
					String value = cs[1].getContents();
					keys.add(key);
					values.add(value);
					// System.out.println("# Add["+key+"]["+value+"]");
				}
				else if (cs.length == 1)
				{
					keys.add(cs[0].getContents());
					values.add("");
				}
			}

			_FillDatas(c, 0, keys, values, p_ExcelData);

			// System.out.println("# 读取完成[" + fname + "][" + tbname + "]");
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_READ_CONFIG_FILE, fname, tbname);

		}
		catch (Exception e)
		{
			Log.out.LogException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> int _FillArrayData(Field fd, int loc, ArrayList<String> keys, ArrayList<String> values, T p_ExcelData) throws Exception
	{
		Class<?> c = fd.getType().getComponentType();
		int len = _GetArrayLength(fd.getName(), keys, loc);
		T[] ts = (T[]) Array.newInstance(c, len);
		
		Field[] fs = c.getDeclaredFields();
		fs = _GetFields(fs);

		for (int i = 0; i < len; ++i)
		{
			T o = (T) c.newInstance();
			int startindex = loc;
			loc = _FillDatas(o.getClass(), loc, keys, values, o);
			if (loc - startindex == fs.length)
			{
				ts[i] = o;
			}
			else
			{
				ts[i] = null;
			}
		}

		fd.set(p_ExcelData, ts);
		return loc;
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

	private static <T> int _FillDatas(Class<?> c, int loc, ArrayList<String> keys, ArrayList<String> values, T p_ExcelData) throws Exception
	{
		Field[] fs = c.getFields();
		fs = _GetFields(fs);
		for (Field fd : fs)
		{
			if (loc >= values.size())
			{
				break;
			}
			boolean r = _SerializeData(p_ExcelData, fd, values.get(loc));
			if (!r)
			{
				loc = _SerializeCustom(p_ExcelData, fd, keys, values, loc);
			}
			else
			{
				loc++;
			}

		}
		return loc;
	}

	private static int _GetArrayLength(String name, ArrayList<String> keys, int loc)
	{
		int length = 0;
		length++;
		for (int i = loc + 1; i < keys.size(); ++i)
		{
			if (keys.get(i).contains(name))
			{
				length++;
			}
			else
			{
				break;
			}
		}
		return length;
	}

	private static <T> int _SerializeCustom(T p_ExcelData, Field fd, ArrayList<String> keys, ArrayList<String> values, int loc) throws Exception
	{
		if (!fd.getType().isArray())
		{

		}
		else
		{
			loc = _FillArrayData(fd, loc, keys, values, p_ExcelData);
		}
		return loc;
	}

	/**
	 * 序列化字段的值
	 * 
	 * @param t
	 *            要序列化的对象
	 * @param f
	 *            要序列化的字段
	 * @param value
	 *            从excel中读取到的值
	 */
	private static <T> boolean _SerializeData(T t, Field f, String value) throws Exception
	{
		Type type = f.getGenericType();
		if (type == boolean.class)
		{
			f.setBoolean(t, Str.GetBoolean(value));
		}
		else if (type == byte.class)
		{
			f.setByte(t, Str.GetByte(value));
		}
		else if (type == short.class)
		{
			f.setShort(t, Str.GetShort(value));
		}
		else if (type == int.class)
		{
			f.setInt(t, Str.GetInt(value));
		}
		else if (type == long.class)
		{
			f.setLong(t, Str.GetLong(value));
		}
		else if (type == float.class)
		{
			f.setFloat(t, Str.GetFloat(value));
		}
		else if (type == double.class)
		{
			f.setDouble(t, Str.GetDouble(value));
		}
		else if (type == String.class)
		{
			f.set(t, value);
		}
		else
		{
			return false;
		}
		return true;
	}
}
