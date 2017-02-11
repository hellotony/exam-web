package com.exam.support.util.json.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectUtils.class);

    public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        }
        LOG.warn("no such field {} in class {}", fieldName, obj.getClass());
        return null;
    }

    public static List<Field> getAllDeclaredFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<Field>();
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] fields = superClass.getDeclaredFields();
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                /** 过滤掉static、final的属性 **/
                if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                    continue;
                }
                allFields.add(field);
            }
        }
        return allFields;
    }

    public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }

    public static void setFieldValue(Object target, Field field, Object value) throws IllegalArgumentException,
            IllegalAccessException {
        if (field.isAccessible()) {
            field.set(target, value);
        } else {
            field.setAccessible(true);
            field.set(target, value);
            field.setAccessible(false);
        }
    }

    public static void invokeSetterMethod(Object target, String fieldName, Object value) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (value == null)
            return;
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method = target.getClass().getMethod(methodName, value.getClass());
        method.invoke(target, value);
    }

    public static Object invokeGetterMethod(Object target, String fieldName) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method = target.getClass().getMethod(methodName);
        if (method == null) {
            methodName = methodName.replace("get", "is");
            method = target.getClass().getMethod(methodName);
        }
        return method.invoke(target);
    }

}
