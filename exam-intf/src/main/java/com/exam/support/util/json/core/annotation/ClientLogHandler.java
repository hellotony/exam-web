package com.exam.support.util.json.core.annotation;

import com.exam.support.util.json.core.method.impl.DefaulClientLogHandler;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: liurui
 * Date: 14-5-23
 * Time: 下午4:11
 * 请求的log记录处理注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface ClientLogHandler {
    /**
     * log记录处理类
     */
    Class handlerClass() default DefaulClientLogHandler.class;
}
