package com.odl.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * es搜索引擎的_id
 *
 * @author Antoneo
 * @create 2018-09-14 11:41
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DocumentID {
    /**
     * 默认不填写就好，只是标注一下这个id是es中document的id
     * @return
     */
    String value() default "";
}
