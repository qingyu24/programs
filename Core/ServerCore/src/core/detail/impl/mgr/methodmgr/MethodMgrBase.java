/**
 * MethodMgrBase.java 2013-2-26上午11:51:28
 */
package core.detail.impl.mgr.methodmgr;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import javassist.*;
import javassist.bytecode.*;
import utility.Debug;
import utility.RFC.*;
import core.*;
import core.detail.impl.log.*;
import core.detail.impl.methodparam.*;
import core.detail.interface_.*;
import core.exception.*;
import core.remote.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public abstract class MethodMgrBase implements MethodMgr
{
	protected Map<Integer,MethodEx> 	m_AllReg = new HashMap<Integer,MethodEx>();
	protected RFCGather					m_Gather = new RFCGather();
	
	public abstract int GetClassID(Class<?> c);
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodMgr#GetClone(byte, byte)
	 */
	@Override
	public MethodEx GetMethod(byte classid, byte methodid)
	{
		int id = _GetID(classid, methodid);
		if ( !m_AllReg.containsKey(id) )
		{
			return null;
		}
		else
		{
			return m_AllReg.get(id).Clone();
		}
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodMgr#Reg(java.lang.Object)
	 */
	@Override
	public void Reg(Object p_regObj) throws RegException
	{
		Class<?> c = p_regObj.getClass();
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_METHOD_REG, c.getName());
		String cname = c.getName().substring( c.getName().lastIndexOf('.')+1 );
		Class<?> it = _GetInterface(c);
		
		boolean save = IsSaveToFile();
		int classid = GetClassID(it);
		Debug.Assert(classid >= 0 && classid < Short.MAX_VALUE, new RegException("# 错误的类id["+classid+"]"));
		m_Gather.AddClass(cname, classid, save);
		
		Method[] ms = it.getMethods();
		for ( Method m : ms )
		{
			int methodid = GetMethodID(m);
			if ( methodid < 0 )
			{
				continue;
			}
			
			int id = _GetID(classid ,methodid);
			Debug.Assert(m_AllReg.containsKey(id) == false, new RegException("# 以["+classid+","+methodid+"]已经注册了"));
			
			m_Gather.AddMethod(m.getName(), methodid, save);
			
			Class<?>[] ps = m.getParameterTypes();
			Type[] ts = m.getGenericParameterTypes();
			Annotation[][] ass = m.getParameterAnnotations();
			Debug.Assert(ps.length != 0 && ass.length != 0 && ass[0].length != 0 && ass[0][0].annotationType() == PU.class
					, new RegException("# 对外的接口第一个参数注释必须是@PU"));
			
			
			Debug.Assert(_FindUserInterface(ps[0]), new RegException("# 第一个参数类型必须是User的派生类"));
			
			MethodParam[] mps = new MethodParam[ps.length];
			mps[0] = new MethodParamRefUser();
			
			String[] paramNames = GetParamName(c, m);
			if ( paramNames != null )
			{
				Debug.Assert( ps.length == paramNames.length , "");
			}
			
			for ( int i = 1; i < ps.length; ++i )
			{
				Debug.Assert(ass[i].length != 0, new RegException("# 参数必须使用参数注释@P*"));
				mps[i] = GetMethodParam(ps[i], ts[i], ass[i][0], paramNames != null ? paramNames[i] : null, save);
			}
			
			MethodEx mex = _GetMethodEx(p_regObj, m, mps, classid, methodid);
			m_AllReg.put(id, mex);
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_METHOD_ADD_METHOD, classid, methodid, cname, m.getName());
		}
		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_METHOD_REG_FINISH, c.getName());
	}
	
	public abstract void RFCBuild();
	
	private boolean _FindUserInterface(Class<?> c)
	{
		Class<?> cs = c.getSuperclass();
		Class<?>[] is = cs.getInterfaces();
		for ( Class<?> iss : is )
		{
			if ( iss == User.class )
			{
				return true;
			}
		}
		return false;
	}

	private int _GetID(int classid,int methodid)
	{
		return classid << 16 | methodid;
	}

	private Class<?> _GetInterface(Class<?> c) throws RegException
	{
		Class<?>[] cs = c.getInterfaces();
		Debug.Assert(cs.length != 0, new RegException("# 无法在["+c.getName()+"]找到一个接口类!"));
		return cs[0];
	}

	private boolean _HaveInterface(Class<?> type, Class<?> face)
	{
		Class<?>[] cs = type.getInterfaces();
		for ( Class<?> c : cs )
		{
			if ( c == face )
			{
				return true;
			}
		}
		return false;
	}

	private MethodParam GetArrayListMethodParam(Class<?> s, Type st, Annotation an, String paramName, boolean save) throws RegException
	{
		Type type = an.annotationType();
		if ( type == PVI.class && st instanceof ParameterizedType )
		{
			ParameterizedType t = (ParameterizedType) st;
			Type[] ts = t.getActualTypeArguments();
			Type ic = ts[0];
			if ( ic == Boolean.class )
			{
				m_Gather.AddParam(RFCType.ArrayBool, paramName, save);
				return new MethodParamArrayList(Boolean.class);
			}
			if ( ic == Byte.class )
			{
				m_Gather.AddParam(RFCType.ArrayByte, paramName, save);
				return new MethodParamArrayList(Byte.class);
			}
			if ( ic == Double.class )
			{
				m_Gather.AddParam(RFCType.ArrayDouble, paramName, save);
				return new MethodParamArrayList(Double.class);
			}
			if ( ic == Float.class )
			{
				m_Gather.AddParam(RFCType.ArrayFloat, paramName, save);
				return new MethodParamArrayList(Float.class);
			}
			if ( ic == Integer.class )
			{
				m_Gather.AddParam(RFCType.ArrayInt, paramName, save);
				return new MethodParamArrayList(Integer.class);
			}
			if ( ic == Long.class )
			{
				m_Gather.AddParam(RFCType.ArrayLong, paramName, save);
				return new MethodParamArrayList(Long.class);
			}
			if ( ic == String.class )
			{
				m_Gather.AddParam(RFCType.ArrayString, paramName, save);
				return new MethodParamArrayList(String.class);
			}
			if ( ic == Short.class )
			{
				m_Gather.AddParam(RFCType.ArrayShort, paramName, save);
				return new MethodParamArrayList(Short.class);
			}
		}
		if ( type == PVC.class )
		{
			PVC pvc = (PVC) an;
			
			Class<?> pType = pvc.Type();
			Debug.Assert( _HaveInterface(pType, SerialData.class), "" );
//			m_Gather.AddParam(RFCType.ArrayCustom, paramName);
			return new MethodParamArrayListCustom(pType);
		}
		throw new RegException("# 参数使用了目前还没支持的类型[" + s + "]");
	}

	private MethodParam GetMethodParam(Class<?> s, Type st, Annotation an, String paramName, boolean save) throws RegException
	{
		Type type = an.annotationType();
		if ( s == boolean.class )
		{
			Debug.Assert(type == PB.class, new RegException("# boolean类型的参数必须使用注释@PB"));
			m_Gather.AddParam(RFCType.Bool, paramName, save);
			return new MethodParamBoolean();
		}
		if ( s == byte.class )
		{
			Debug.Assert(type == PBT.class, new RegException("# byte类型的参数必须使用注释@PBT"));
			m_Gather.AddParam(RFCType.Byte, paramName, save);
			return new MethodParamByte();
		}
		if ( s == double.class )
		{
			Debug.Assert(type == PD.class, new RegException("# double类型的参数必须使用注释@PD"));
			m_Gather.AddParam(RFCType.Double, paramName, save);
			return new MethodParamDouble();
		}
		if ( s == float.class )
		{
			Debug.Assert(type == PF.class, new RegException("# float类型的参数必须使用注释@PF"));
			m_Gather.AddParam(RFCType.Float, paramName, save);
			return new MethodParamFloat();
		}
		if ( s == int.class )
		{
			Debug.Assert(type == PI.class, new RegException("# int类型的参数必须使用注释@PI"));
			m_Gather.AddParam(RFCType.Int, paramName, save);
			return new MethodParamInt();
		}
		if ( s == long.class )
		{
			Debug.Assert(type == PL.class, new RegException("# long类型的参数必须使用注释@PL"));
			m_Gather.AddParam(RFCType.Long, paramName, save);
			return new MethodParamLong();
		}
		if ( s == String.class )
		{
			Debug.Assert(type == PS.class, new RegException("# String类型的参数必须使用注释@PS"));
			m_Gather.AddParam(RFCType.String, paramName, save);
			return new MethodParamString();
		}
		if ( s == short.class )
		{
			Debug.Assert(type == PST.class, new RegException("# short类型的参数必须使用注释@PST"));
			m_Gather.AddParam(RFCType.Short, paramName, save);
			return new MethodParamShort();
		}
		if ( _FindUserInterface(s) )
		{
			if ( type == PUGID.class )
			{
				m_Gather.AddParam(RFCType.Long, paramName, save);
				return new MethodParamRoleGid();
			}
			else if ( type == PUNID.class )
			{
				m_Gather.AddParam(RFCType.Int, paramName, save);
				return new MethodParamRoleNetID();
			}
			else if ( type == PUNK.class )
			{
				m_Gather.AddParam(RFCType.String, paramName, save);
				m_Gather.AddParam(RFCType.Short, "serverid", save);
				return new MethodParamRoleNick();
			}
			else if ( type == PUUN.class )
			{
				m_Gather.AddParam(RFCType.String, paramName, save);
				m_Gather.AddParam(RFCType.String, "psd", save);
				return new MethodParamUserName();
			}
		}
		if ( s == ArrayList.class )
		{
			return GetArrayListMethodParam(s, st, an ,paramName, save);
		}
		throw new RegException("# 参数使用了目前还没支持的类型[" + s + "]");
	}

	private String[] GetParamName(Class<?> c, Method m) throws RegException
	{
		try
		{
			ClassPool pool = ClassPool.getDefault();
			CtClass cc = pool.get(c.getName());
			CtMethod cm = cc.getDeclaredMethod(m.getName());
			MethodInfo methodInfo = cm.getMethodInfo();
			
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
			Debug.Assert( attr != null , "");
			String[] paramNames = new String[cm.getParameterTypes().length];
			int pos = javassist.Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			for ( int i = 0; i < paramNames.length; ++i)
			{
				paramNames[i] = attr.variableName(i + pos);
			}
			return paramNames;
		}
		catch (NotFoundException e)
		{
		}
		return null;
	}

	protected abstract MethodEx _GetMethodEx(Object p_RegObject, Method m, MethodParam[] mps, int classid, int methodid);

	protected abstract int GetMethodID(Method m);

	protected abstract boolean IsSaveToFile();

}
