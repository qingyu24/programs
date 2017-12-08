package utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtil
{
	public static <K1, K2, V> void AddIntoTable(K1 key1, K2 key2, V value, Map<K1, Map<K2, V>> table)
	{
		if(table == null)
			return;
		Map<K2, V> tmpTable1 = null;
		if(table.containsKey(key1))
			tmpTable1 = table.get(key1);
		else
		{
			tmpTable1 = new HashMap<K2, V>();
			table.put(key1, tmpTable1);
		}
		
		tmpTable1.put(key2, value);
	}
	
	public static <K1, K2, K3, V> void AddIntoTable(K1 key1, K2 key2, K3 key3, V value, Map<K1, Map<K2, Map<K3, V>>> table)
	{
		if(table == null)
			return;
		Map<K2, Map<K3, V>> tmpTable1 = null;
		if(table.containsKey(key1))
			tmpTable1 = table.get(key1);
		else
		{
			tmpTable1 = new HashMap<K2, Map<K3, V>>();
			table.put(key1, tmpTable1);
		}
		
		Map<K3, V> tmpTable2 = null;
		if(tmpTable1.containsKey(key2))
			tmpTable2 = tmpTable1.get(key2);
		else
		{
			tmpTable2 = new HashMap<K3, V>();
			tmpTable1.put(key2, tmpTable2);
		}
		
		tmpTable2.put(key3, value);
	}
	
	public static <K1, K2, V> V GetFromTable(K1 key1, K2 key2, Map<K1, Map<K2, V>> table)
	{
		if(table == null)
			return null;
		if(table.containsKey(key1))
		{
			Map<K2, V> tmpTable = table.get(key1);
			if(tmpTable.containsKey(key2))
				return tmpTable.get(key2);
		}
		return null;
	}
	
	public static <K1, K2, K3, V> V GetFromTable(K1 key1, K2 key2, K3 key3, Map<K1, Map<K2, Map<K3, V>>> table)
	{
		if(table == null)
			return null;
		if(table.containsKey(key1))
		{
			Map<K2, Map<K3, V>> tmpTable1 = table.get(key1);
			if(tmpTable1.containsKey(key2))
			{
				Map<K3, V> tmpTable2 = tmpTable1.get(key2);
				if(tmpTable2.containsKey(key3))
					return tmpTable2.get(key3);
			}
		}
		return null;
	}
	
	public static <K1, K2, V> V RemoveFromTable(K1 key1, K2 key2, Map<K1, Map<K2, V>> table)
	{
		if(table == null)
			return null;
		if(table.containsKey(key1))
		{
			Map<K2, V> tmpTable = table.get(key1);
			if(tmpTable.containsKey(key2))
				return tmpTable.remove(key2);
		}
		return null;
	}
	
	public static <K1, K2, K3, V> V RemoveFromTable(K1 key1, K2 key2, K3 key3, Map<K1, Map<K2, Map<K3, V>>> table)
	{
		if(table == null)
			return null;
		if(table.containsKey(key1))
		{
			Map<K2, Map<K3, V>> tmpTable1 = table.get(key1);
			if(tmpTable1.containsKey(key2))
			{
				Map<K3, V> tmpTable2 = tmpTable1.get(key2);
				if(tmpTable2.containsKey(key3))
					return tmpTable2.remove(key3);
			}
		}
		return null;
	}
	
	public static <T> Map<Integer, List<T>> GetAllPageList(Collection<T> p_Collection, int p_SizePrePage) {
		
		Map<Integer, List<T>> map = null;
		
		if(p_Collection != null && p_Collection.size() > 0) {
		
			int size = p_Collection.size();
			
			if(size > 0) {
				
				map = new HashMap<Integer, List<T>>();
				//第一项
				int index = 1;
				
				for (T item : p_Collection) {
					if(map.containsKey(index)) {
						List<T> list = map.get(index);
						if(list.size() < p_SizePrePage) {
							list.add(item);
						} else {
							List<T> list1 = new ArrayList<T>();
							list1.add(item);
							index++;
							map.put(index, list1);
						}
						
					} else {
						List<T> list = new ArrayList<T>();
						list.add(item);
						map.put(index, list);
					}
				}
				
			}
		}
		return map;
	}
}
