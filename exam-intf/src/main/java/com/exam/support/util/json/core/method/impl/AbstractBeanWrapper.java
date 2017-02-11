package com.exam.support.util.json.core.method.impl;

import com.exam.support.util.json.core.method.BeanWrapper;
import com.exam.support.util.json.core.method.ResponseData;
import org.springframework.core.MethodParameter;

public abstract class AbstractBeanWrapper implements BeanWrapper {

    public boolean supportsType(MethodParameter returnType) {
        if (ResponseData.class.isAssignableFrom(returnType.getParameterType())) {
            return false;
        }
        return supports(returnType);
    }

    public abstract boolean supports(MethodParameter returnType);

}
