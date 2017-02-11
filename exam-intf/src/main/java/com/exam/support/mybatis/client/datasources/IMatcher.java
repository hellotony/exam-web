package com.exam.support.mybatis.client.datasources;

/**
 * 通配处理接口
 * 
 * @author wangchong
 * 
 * @param <F> 返回
 * @param <T> 参数
 */
public interface IMatcher<F, T> {
	F doMatch(T regexArgument);
}
