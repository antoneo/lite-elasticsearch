package com.odl.model;

import com.odl.dao.ObjectHelper;
import com.odl.interfaces.DocumentID;
import com.odl.interfaces.IndexName;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
/**
 * 基础Model封装
 *
 * @author Antoneo
 * @create 2018-09-14 10:33
 **/
public abstract class Model<T extends Model> {

    public String getIndexName(){
        Object obj=this;
        if (obj.getClass().isAnnotationPresent(IndexName.class)) {
            //获得所有的注解
            for (Annotation anno : obj.getClass().getDeclaredAnnotations()) {
                //找到自己的注解
                if (anno.annotationType().equals(IndexName.class)) {
                    if (!StringUtils.isEmpty(((IndexName) anno).index())) {
                        return ((IndexName) anno).index();
                    }
                }
            }
        }
        return null;
    }

    public String getIndexType(){
        Object obj=this;
        if (obj.getClass().isAnnotationPresent(IndexName.class)) {
            //获得所有的注解
            for (Annotation anno : obj.getClass().getDeclaredAnnotations()) {
                //找到自己的注解
                if (anno.annotationType().equals(IndexName.class)) {
                    if (!StringUtils.isEmpty(((IndexName) anno).type())) {
                        return ((IndexName) anno).type();
                    }
                }
            }
        }
        return null;
    }

    /**
     * es的 id
     * @return
     */
    public String _id(){
        Object obj=this;
        ObjectHelper objectHelper = new ObjectHelper(obj.getClass());
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                objectHelper.parser(field.getName());
                if (field.isAnnotationPresent(DocumentID.class)) {
                    for (Annotation anno : field.getDeclaredAnnotations()){
                        if (anno.annotationType().equals(DocumentID.class)){
                            return objectHelper.getGetter().invoke(obj).toString();
                        }
                    }

                }
            }
        } catch (Exception e) {
            //还是不打印了，在不给id插入时会有这个异常
//            System.err.println("获取注解id失败.");
        }
        return null;
    }

    /**
     * 判断是否有documnetID注解
     * @return
     */
    public boolean hasDocumentID(){
        Object obj=this;
        ObjectHelper objectHelper = new ObjectHelper(obj.getClass());
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                objectHelper.parser(field.getName());
                if (field.isAnnotationPresent(DocumentID.class)) {
                    for (Annotation anno : field.getDeclaredAnnotations()){
                        if (anno.annotationType().equals(DocumentID.class)){
                            return true;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置document id，在返回结果转实体时使用
     */
    public void setDocumentID(String id){
        Object obj=this;
        ObjectHelper objectHelper = new ObjectHelper(obj.getClass());
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                objectHelper.parser(field.getName());
                if (field.isAnnotationPresent(DocumentID.class)) {
                    for (Annotation anno : field.getDeclaredAnnotations()){
                        if (anno.annotationType().equals(DocumentID.class)){
                            objectHelper.getSetter().invoke(obj,id);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}