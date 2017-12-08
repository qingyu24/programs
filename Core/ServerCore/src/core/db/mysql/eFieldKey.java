/**
 * FieldKey.java 2012-8-28下午4:21:13
 */
package core.db.mysql;

import core.exception.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 能作为key的字段名字
 */
public enum eFieldKey
{
	UserID,UserName,GID,RoleID,
	Unknow,
	;

	/**
	 * 获取名字
	 */
	public String GetName()
	{
		switch(this)
		{
		case UserID: return "UserID";
		case UserName: return "UserName";
		case GID: return "GID";
		case RoleID: return "RoleID";
		case Unknow: return "*";
		}
		throw new LostCodeRuntimeException("eFieldKey#GetName,有未解析的类型");
	}
}
