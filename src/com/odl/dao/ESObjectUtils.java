package com.odl.dao;

import com.odl.interfaces.DocumentID;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * ObjectUtils工具，实体和map的相互转换
 *
 * @author Antoneo
 * @create 2018-09-14 15:06
 **/
public class ESObjectUtils {

    /**
     * 实体类转map
     * @param obj
     * @return
     */
    public static Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if(!"serialVersionUID".equals(field.getName())){
                    if (!field.isAnnotationPresent(DocumentID.class)) {
                        map.put(field.getName(), field.get(obj));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * map转实体
     * @param map
     * @param clazz
     * @return
     */
    public static Object toObject(Map<String, Object> map, Class<?> clazz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
