/**
 * PKRecordData.java 2013-7-4下午6:40:19
 */
package mergedb.db;

import core.db.DBInt;
import core.db.DBLong;
import core.db.DBShort;
import core.db.RoleDataBase;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 因为logic中的PKRecordData包含引用数据,不能用,所以用这个结构来读写
 */
public class PKRecordData extends RoleDataBase
{
	public DBLong			RoleID;		///<属于的角色id
	public DBInt			RankNum;		///<排名名次
	public DBInt			LastRankNum;	///<上次排名
	public DBShort			HasAward;		///<是否有奖励未领取
}
