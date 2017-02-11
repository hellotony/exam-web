package com.exam.support.mybatis.client.router.table.rules;

/**
 * 分表路由参数载体
 * 
 * @author wangchong
 * 
 */
public class TableRoutingFact {

	/**
	 * 路由参数
	 */
	private Object argument;

	/**
	 * 执行mapper的id
	 */
	private String stamtement;

	public TableRoutingFact() {
		super();
	}

	public TableRoutingFact(String stamtement, Object argument) {
		super();
		this.argument = argument;
		this.stamtement = stamtement;
	}

	public Object getArgument() {
		return argument;
	}

	public void setArgument(Object argument) {
		this.argument = argument;
	}

	public String getStamtement() {
		return stamtement;
	}

	public void setStamtement(String stamtement) {
		this.stamtement = stamtement;
	}

}
