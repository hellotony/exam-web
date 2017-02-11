package com.exam.support.util.json.core.method.impl;

import com.exam.support.util.json.core.method.SuccessData;
import org.springframework.core.MethodParameter;

public class DefaultBeanWrapper extends AbstractBeanWrapper {

    public Object wrap(Object bean) {
        return new SuccessData(bean);
    }

    // public Integer getPriority() {
    // return Integer.MAX_VALUE;
    // }

    @Override
    public boolean supports(MethodParameter returnType) {
        return true;
    }

}
