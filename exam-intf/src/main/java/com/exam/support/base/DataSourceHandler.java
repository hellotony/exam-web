package com.exam.support.base;

import com.exam.support.annotation.ReadOnly;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
@Aspect
public class DataSourceHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Around(value = "@annotation(com.exam.support.annotation.ReadOnly)")
    public Object changeDataSource(ProceedingJoinPoint point) throws Throwable {
        try {
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            Annotation[] annotations = methodSignature.getMethod().getAnnotations();
            if (annotations.length > 0) {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof ReadOnly) {
                        ReadOnly readOnly = (ReadOnly) annotation;
                        if (readOnly.value()) {
                            DataSourceContextHolder.setDataSourceType(DataSourceContextHolder.SLAVE);
                            logger.info("线程{}切换到读库", Thread.currentThread().getName());
                            break;
                        }
                    }
                }
            }
             return point.proceed();
        } finally {
            logger.info("线程{}切回到写库", Thread.currentThread().getName());
            DataSourceContextHolder.setDataSourceType(DataSourceContextHolder.MASTER);
        }
    }

    @After(value = "@annotation(com.exam.support.annotation.ReadOnly)")
    public void changeMaster() {
        logger.info("线程{}切回到写库", Thread.currentThread().getName());
        DataSourceContextHolder.setDataSourceType(DataSourceContextHolder.MASTER);
    }
}
