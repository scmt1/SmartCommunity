package me.zhengjie.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class JavaBeanUtil {
	// 利用反射
	/**
	 * 
	 * <p>
	 * Description:对象转换为map<br />
	 * </p>
	 * @author dengjie
	 * @version 0.1 2020年4月2日
	 * @param obj
	 * @return
	 * Map<String,Object>
	 */
	public static Map<String, Object> objectToMap(Object obj) {
		Map<String, Object> map = new HashMap<>();
		if (obj == null) {
			return map;
		}
		Class<? extends Object> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				map.put(field.getName(), field.get(obj));
			}
		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
