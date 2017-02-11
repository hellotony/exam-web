package com.exam.support.util.json.core.method;

import org.springframework.core.MethodParameter;

/**
 * 对象包装器
 */
public interface BeanWrapper {

    /**
     * 支持性判断
     * 
     * @param returnType
     * @return
     */
    boolean supportsType(MethodParameter returnType);

    /**
     * 对象包装
     * 
     * @param bean
     * @return
     */
    Object wrap(Object bean);

}
