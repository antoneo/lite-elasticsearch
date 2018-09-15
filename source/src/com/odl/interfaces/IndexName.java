package com.odl.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ES的注解
 *
 * @author Antoneo
 * @create 2018-08-23 17:12
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface IndexName {
    /**
     * 索引
     * @return
     */
    String index() default "";

    /**
     * 类
     * @return
     */
    String type() default "";
}
