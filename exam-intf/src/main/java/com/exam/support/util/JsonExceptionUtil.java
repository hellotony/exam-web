package com.exam.support.util;

import com.exam.support.exceptions.ErrorCode;
import com.exam.support.exceptions.ServiceException;
import com.exam.support.exceptions.SystemException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 功能描述： 对JsonUtil类进行简单封装，添加对异常的处理方法 支持驼峰命名与下划线命名转换以及其他常用方法封装
 * @author liuchao2
 */
public class JsonExceptionUtil {
    /**
     * 日志类
     */
    private static Logger logger = LoggerFactory.getLogger(JsonExceptionUtil.class);

    /**
     * 功能描述：重新封装toJsonString方法，添加对异常的处理
     * @param object 输入参数
     * @return <T> JsonString
     * @throws ServiceException
     */
    public static <T> String toJsonString(T object) throws ServiceException {
        return toString(object, false);
    }

    /**
     * 功能描述：重新封装toBean方法，添加对异常的处理
     * @param obj 输入
     * @param cls 需要反序列化的Class
     * @return 反序列化后的bean
     */
    public static <T> T toBean(Object obj, Class<T> cls) {
        T bean = JsonUtil.toBean(obj, cls);
        if (bean != null) {
            return bean;
        } else {
            return null;
        }
    }

    /**
     * json to bean
     * @param obj 需要反序列化的内容
     * @param cls bean class
     * @param deserializePropertiesByCamelStrategy
     *            是否使用驼峰命名规则转换(下划线转驼峰命名);<strong>该策略与@JsonProperty注解不冲突,
     *            注解优先级高于该策略配置</strong>
     * @return
     */
    public static <T> T toBean(Object obj, Class<T> cls, boolean deserializePropertiesByCamelStrategy) {
        if (obj == null || cls == null) {
            return null;
        }
        if (deserializePropertiesByCamelStrategy) {
            ObjectMapper mapper = new ObjectMapper();
            // 反序列化遇到bean 中没有的属性时跳过,不抛出异常
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            try {
                String inputStr = JsonUtil.toString(obj);
                T bean = mapper.readValue(inputStr, cls);
                return bean;
            } catch (Exception e) {
                // 转换到json格式字符串错误
                throw new SystemException(ErrorCode.CONVERT_JSON_ERROR);
            }
        }
        return toBean(obj, cls);
    }

    /**
     * toList方法
     * @param obj input data,通常为ResponseVo.data
     * @param cls 需要被反序列化类class,如:xxxDto.class
     * @param deserializePropertiesByCamelStrategy
     *            是否使用驼峰命名法反序列化属性,将下划线反序列化成驼峰命名;<strong>该策略与@JsonProperty注解不冲突,
     *            注解优先级高于该策略配置</strong>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Object obj, Class<T> cls, boolean deserializePropertiesByCamelStrategy) {
        if (obj == null || cls == null) {
            return null;
        }
        if (deserializePropertiesByCamelStrategy) {
            ObjectMapper mapper = new ObjectMapper();
            // 反序列化遇到bean 中没有的属性时跳过,不抛出异常
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            try {
                String inputStr = JsonUtil.toString(obj);
                List<Object> tempList = mapper.readValue(inputStr, ArrayList.class);
                if (tempList != null && !tempList.isEmpty()) {
                    List<T> resultList = new ArrayList<T>(tempList.size());
                    for (Object t : tempList) {
                        resultList.add(mapper.readValue(toString(t, true, deserializePropertiesByCamelStrategy), cls));
                    }
                    return resultList;
                }
                return null;
            } catch (Exception e) {
                // 转换到json格式字符串错误
                throw new SystemException(ErrorCode.CONVERT_JSON_ERROR);
            }
        }
        return JsonUtil.toList(obj, cls);
    }

    /**
     * 将对象序列化成json字符串
     * @param input 输入 java bean,map,list etc.
     * @return json 字符串
     */
    public static String toString(Object input) {
        return JsonExceptionUtil.toString(input, true, false);
    }

    /**
     * 将对象序列化成json字符串
     * <p>
     * <strong>example:</strong><br>
     * <code>
     *  DepartCityDto testDto = new DepartCityDto();<br>
        testDto.setDepartCityCode(2500);<br>
        JsonExceptionUtil.toString(testDto,true);=>{"departCityCode":2500,"departCityName":null}<br>
        JsonExceptionUtil.toString(testDto,false);=>{"departCityCode":2500}<br>
     *  </code>
     * </p>
     * @param input 输入 java bean,map,list etc.
     * @param serializeNull 是否序列化值为null的属性,true时json中包含值为null的属性
     *            <strong>此功能仅对java
     *            bean有效(如XXXDto),对Map或List<java.util.Map>无效</strong>
     * @return json 字符串
     */
    public static String toString(Object input, boolean serializeNull) {
        return JsonExceptionUtil.toString(input, serializeNull, false);
    }

    /**
     * 将对象序列化成json字符串
     * <p>
     * <strong>example:</strong><br>
     * <code>
     *  DepartCityDto testDto = new DepartCityDto();<br>
        testDto.setDepartCityCode(2500);<br>
        JsonExceptionUtil.toString(testDto, true, true);=>{"depart_city_code":2500,"depart_city_name":null}<br>
        JsonExceptionUtil.toString(testDto, true, false);=>{"departCityCode":2500,"departCityName":null}<br>
        JsonExceptionUtil.toString(testDto, false, true);=>{"depart_city_code":2500}<br>
        JsonExceptionUtil.toString(testDto, false, false);=>{"departCityCode":2500}<br>
     *  </code>
     * </p>
     * @param input 输入 java bean,map,list etc.
     * @param serializeNull 是否序列化值为null的属性,true时json中包含值为null的属性
     *            <strong>此功能仅对java
     *            bean有效(如XXXDto),对Map或List&lt;Map&gt;无效</strong>
     * @param serializePropertiesInCamelStrategy 序列化使用驼峰命名规则,序列化后驼峰命名属性变为下划线分割
     *            <strong>该策略仅对java
     *            bean有效(如XXXDto),对Map或List&lt;Map&gt;无效;该策略与@JsonProperty注解不冲突,
     *            注解优先级高于该策略配置</strong>
     * @return json 字符串
     */
    public static String toString(Object input, boolean serializeNull, boolean serializePropertiesInCamelStrategy) {
        if (input == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        if (serializePropertiesInCamelStrategy) {
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        }
        if (!serializeNull) {
            mapper.setSerializationInclusion(Inclusion.NON_NULL);
        }
        try {
            return mapper.writeValueAsString(input);
        } catch (Exception e) {
            // 转换到json格式字符串错误
            throw new SystemException(ErrorCode.CONVERT_JSON_ERROR);
        }
    }

}
