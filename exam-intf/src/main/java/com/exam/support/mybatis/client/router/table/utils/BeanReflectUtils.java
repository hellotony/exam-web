package com.exam.support.mybatis.client.router.table.utils;

import com.exam.support.mybatis.client.CobarConstants;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类<br>
 * 1.通过反射获取属性写方法，并缓存
 * 
 * @author wangchong
 * 
 */
public class BeanReflectUtils {
	private static Logger logger = LoggerFactory.getLogger(BeanReflectUtils.class);

	/**
	 * 属性写方法缓存
	 */
	private static Map<String, Method> writeMethodCache = new ConcurrentHashMap<String, Method>();

	public static void setProperty(Object target, String propertyName, Object propertyValue) {
		Method method = writeMethodCache.get(propertyName);
		if (method == null) {
			PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(target.getClass(), propertyName);
			method = descriptor.getWriteMethod();
			writeMethodCache.put(propertyName, method);
		}
		if (method != null) {
			try {
				method.invoke(target, typeMatcher(propertyValue));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static Map<Object, Object> beanToMap(Object argument) {
		Map<Object, Object> resultMap = new HashMap<Object, Object>();
		try {
			List<Field> fieldList = findFieldForParent(argument.getClass());
			for (Field field : fieldList) {
				field.setAccessible(true);
				resultMap.put(field.getName(), field.get(argument));
			}
			resultMap.put("class", argument.getClass().getName());
			return resultMap;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	private static List<Field> findFieldForParent(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		Class<?> parent = clazz.getSuperclass();
		if (parent != null && !"java.lang.Object".equals(parent.getName())) {
			fields.addAll(findFieldForParent(parent));
		}
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		return fields;
	}
	public static boolean isSimpleType(Object argumentType) {
		return argumentType.getClass().equals(String.class) || argumentType.getClass().equals(Number.class)
				|| Number.class.isInstance(argumentType);
	}

	private static Object typeMatcher(Object propertyValue) {
		if (propertyValue instanceof String) {
			if (NumberUtils.isNumber((String) propertyValue)) {
				return Integer.valueOf((String) propertyValue);
			}
			if ("true".equalsIgnoreCase((String) propertyValue) || "false".equalsIgnoreCase((String) propertyValue)) {
				return Boolean.valueOf((String) propertyValue);
			}
		}
		return propertyValue;
	}

	public static Object getProperty(Object target, String propertyName) {
		PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(target.getClass(), propertyName);
		if (descriptor != null) {
			Method method = descriptor.getReadMethod();
			if (method != null) {
				try {
					return method.invoke(target, new Object[] {});
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}

	public static void beanCopy(Object source, Object target) {
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public static Object refactorArgument(String statement, Object argument, int dbIndex) {
		Map<Object, Object> newMap = new HashMap<Object, Object>(5);
		if (dbIndex != CobarConstants.DEFAULT_DATASOURCE_INDEX) {
			newMap.put(CobarConstants.DEFAULT_DATASOURCENO_NAME, dbIndex);
		}
		if (argument != null) {
			if (argument instanceof Map<?, ?>) {// bad smell 重复创建map集合
				newMap = (Map<Object, Object>) argument;
			} else if (BeanReflectUtils.isSimpleType(argument)) {
				newMap.put("value", argument);
			} else if (Collection.class.isInstance(argument)) {
				newMap.put("list", argument);
			} else if (argument.getClass().isArray()) {
				newMap.put("array", argument);
			} else {
				Map<Object, Object> beanMap = beanToMap(argument);
				if (beanMap != null) {
					newMap.putAll(beanMap);
				}
			}
			return newMap;
		}
		return argument;
	}

}
