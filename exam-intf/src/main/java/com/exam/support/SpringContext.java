package com.exam.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * SpringContext 静态访问类,用于获取Spring 依赖的注入
 * @author fengxuekui
 *
 */
@Service
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.context = applicationContext;
    }

    public static Object getBean(String beanId) {
    	if(context.containsBean(beanId)) {
    		return context.getBean(beanId);
    	}
        return null;
    }

    public static <T> T getBean(Class<T> requiedClz){
        return context.getBean(requiedClz);
    }
    
    /**
     * @return the applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }
    
}
