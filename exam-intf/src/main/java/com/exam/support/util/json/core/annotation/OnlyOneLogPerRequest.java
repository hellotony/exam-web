package com.exam.support.util.json.core.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: liurui
 * Date: 14-5-23
 * Time: 下午3:33
 * 标识方法一次请求只记录一次log
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface OnlyOneLogPerRequest {
}
