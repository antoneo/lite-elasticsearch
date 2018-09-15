package com.odl.dao;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 获取通用的get，set方法
 *
 * @author Antoneo
 * @create 2018-09-14 9:33
 **/
public class ObjectHelper {

    private Method getter = null;
    private Method setter = null;
    private Method methods[];
    public ObjectHelper(Class clazz){
        this.methods= clazz.getMethods();
    }

    public ObjectHelper parser(String column){
        /**
         *
         * 下划线去除，单词首字母大写转换
         *
         */
        char beanPropertyChars[] = column.toCharArray();
        char rs[]=new char[beanPropertyChars.length];
        for(int i=0,j=0;i<beanPropertyChars.length;i++,j++){
            if(i==0){
                rs[j]=Character.toUpperCase(beanPropertyChars[i]);
            }else if(beanPropertyChars[i]=='_') {
                i += 1;
                rs[j] = Character.toUpperCase(beanPropertyChars[i]);
            }else{
                rs[j]=beanPropertyChars[i];
            }
        }
        String s=new String(rs).trim();
        String names[] = { ("set" + s).intern(), ("get" + s).intern(),
                        ("is" + s).intern(), ("write" + s).intern(),
                        ("read" + s).intern() };
        for (int i = 0; i < this.methods.length; i++) {
            Method method = this.methods[i];
            // 只取公共字段
            if (!Modifier.isPublic(method.getModifiers()))
                continue;
            String methodName = method.getName().intern();
            for (int j = 0; j < names.length; j++) {
                String name = names[j];
                if (!name.equals(methodName))
                    continue;
                if (methodName.startsWith("set")
                        || methodName.startsWith("read"))
                    this.setter = method;
                else
                    this.getter = method;
            }
        }
       return this;
    }

    public Method getGetter() {
        return this.getter;
    }

    public Method getSetter() {
        return this.setter;
    }

    
}
