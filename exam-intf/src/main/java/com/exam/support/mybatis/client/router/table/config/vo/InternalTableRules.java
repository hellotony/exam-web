package com.exam.support.mybatis.client.router.table.config.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * 分表路由规则集合
 * 
 * @author wangchong
 * 
 */
public class InternalTableRules {
	@XStreamAlias(value = "tableRules")
	private List<InternalTableRule> rules;

	public List<InternalTableRule> getRules() {
		return rules;
	}

	public void setRules(List<InternalTableRule> rules) {
		this.rules = rules;
	}

	public InternalTableRules(List<InternalTableRule> rules) {
		super();
		this.rules = rules;
	}

	public InternalTableRules() {
		super();
	}
}
