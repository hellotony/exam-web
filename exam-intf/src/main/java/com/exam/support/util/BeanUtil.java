package com.exam.support.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanUtil {

    /**
     * 对象浅拷贝
     * @param source  源对象
     * @param targetClass 目标对象Class
     * @param excludes 排除属性
     * @param <T>
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T weakCopy(Object source, Class<T> targetClass, String ... excludes) throws InvocationTargetException,
            IllegalAccessException, InstantiationException {
        T target = targetClass.newInstance();
        weakCopy(source, target, excludes);
        return target;
    }

    /**
     * 对象浅拷贝
     * @param source  源对象
     * @param targetClass 目标对象Class
     * @param <T>
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T weakCopy(Object source, Class<T> targetClass) throws InvocationTargetException,
            IllegalAccessException, InstantiationException {
        T target = targetClass.newInstance();
        weakCopy(source, target);
        return target;
    }


    /**
     * 对象浅拷贝
     * @param source 源对象
     * @param target 目标对象
     */
    public static  void weakCopy(Object source, Object target) throws InvocationTargetException, IllegalAccessException {
        Field[] fields = source.getClass().getDeclaredFields();
        String name;
        Method getMethod;
        Method setMethod;
        for (Field field : fields) {
            name = field.getName();
            name = "get" + name.substring(0,1).toUpperCase().concat(name.substring(1));
            try {
                getMethod = source.getClass().getMethod(name);
                setMethod = target.getClass().getMethod(name.replace("get", "set"), getMethod.getReturnType());
            } catch (NoSuchMethodException e) {
                continue;
            }
            Object value = getMethod.invoke(source);
            setMethod.invoke(target, value);
        }
    }

    /**
     * 对象浅拷贝
     * @param source 源对象
     * @param target 目标对象
     * @param excludes 排除属性
     */
    public static  void weakCopy(Object source, Object target, String ... excludes) throws InvocationTargetException, IllegalAccessException {
        Field[] fields = source.getClass().getDeclaredFields();
        String name;
        Method getMethod;
        Method setMethod;
        List<String> excludeList = Arrays.asList(excludes);
        for (Field field : fields) {
            name = field.getName();
            if (excludeList.contains(name)) {
                continue;
            }
            name = "get" + name.substring(0,1).toUpperCase().concat(name.substring(1));
            try {
                getMethod = source.getClass().getMethod(name);
                setMethod = target.getClass().getMethod(name.replace("get", "set"), getMethod.getReturnType());
            } catch (NoSuchMethodException e) {
                continue;
            }
            Object value = getMethod.invoke(source);
            setMethod.invoke(target, value);
        }
    }

    /**
     * 对象集合浅拷贝
     * @param sourceList  源对象集合
     * @param targetClass 目标集合对象class
     * @param excludes    排除字段
     * @param <S>         源对象class
     * @param <T>         目标对象class
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <S, T> List<T> weekCopy(List<S> sourceList, Class<T> targetClass, String ... excludes) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        List<T> targetList = new ArrayList<T>(sourceList.size());
        for (S s : sourceList) {
            T t = targetClass.newInstance();
            weakCopy(s, t, excludes);
            targetList.add(t);
        }
        return targetList;
    }

}
