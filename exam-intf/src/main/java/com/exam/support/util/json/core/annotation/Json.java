package com.exam.support.util.json.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Json {

    /**
     * 针对List 泛型对象化映射,单元类型定义
     */
    Class[] types() default java.lang.Object.class;

    /**
     * json key path定义
     */
    String path() default "";

}
