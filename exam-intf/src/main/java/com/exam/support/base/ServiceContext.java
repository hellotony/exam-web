package com.exam.support.base;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务上下文
 * @author wangchong
 * 
 */
public class ServiceContext {
    /**
     * 线程变量
     */
    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new ThreadLocal<Map<String, String>>();

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL_OBJECT = new ThreadLocal<Map<String, Object>>();

    /**
     * 设置属性
     * @param key
     * @param value
     */
    public static void setObjectProperty(String key, Object value) {
        if (StringUtils.isNotBlank(key)) {
            Map<String, Object> threadMap = THREAD_LOCAL_OBJECT.get();
            if (null == threadMap) {
                threadMap = new HashMap<String, Object>();
                THREAD_LOCAL_OBJECT.set(threadMap);
            }
            threadMap.put(key.toUpperCase(), value);
        }
    }
    /**
     * 获取变量
     * @param key
     * @return 获取的变量值
     */
    public static Object getObjectProperty(String key) {
        Object result = null;
        if (StringUtils.isNotBlank(key)) {
            Map<String, Object> threadMap = THREAD_LOCAL_OBJECT.get();
            if (null != threadMap) {
                result = threadMap.get(key.toUpperCase());
            }
        }
        return result;
    }

    public static void clearThreadContextInfo() {
        THREAD_LOCAL.remove();
        THREAD_LOCAL_OBJECT.remove();
    }
}
