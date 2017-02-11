package com.exam.support.mybatis.client.support.vo;

import java.util.Collection;

public abstract class AbstractBatchTask implements BatchTask {
	private Collection<?> list;

	public AbstractBatchTask() {
		super();
	}

	protected AbstractBatchTask(Collection<?> list) {
		setList(list);
	}

	public Collection<?> getList() {
		return list;
	}

	public void setList(Collection<?> list) {
		this.list = list;
	}
}
