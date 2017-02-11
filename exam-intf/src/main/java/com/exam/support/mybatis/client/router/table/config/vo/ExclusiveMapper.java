package com.exam.support.mybatis.client.router.table.config.vo;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 分表路由规则的例外载体
 * 
 * @author wangchong
 * 
 */
public class ExclusiveMapper {

	@XStreamAsAttribute
	private String mapper;

	public ExclusiveMapper() {
		super();
	}

	public ExclusiveMapper(String mapper) {
		super();
		this.mapper = mapper;
	}

	public String getMapper() {
		return mapper;
	}

	public void setMapper(String mapper) {
		this.mapper = mapper;
	}
}
