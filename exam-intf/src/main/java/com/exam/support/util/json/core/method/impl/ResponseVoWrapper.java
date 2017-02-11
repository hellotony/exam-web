package com.exam.support.util.json.core.method.impl;

import com.exam.support.util.json.core.utils.ResponseVo;
import org.springframework.core.MethodParameter;


public class ResponseVoWrapper extends AbstractBeanWrapper {

	public Object wrap(Object bean) {
		// TODO Auto-generated method stub
		return (ResponseVo)bean;
	}

	@Override
	public boolean supports(MethodParameter returnType) {
		// TODO Auto-generated method stub
		return ResponseVo.class.isAssignableFrom(returnType.getParameterType());
	}

}