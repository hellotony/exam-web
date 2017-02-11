package com.exam.support.mybatis.client.router;

import java.util.List;

/**
 * 分库路由配置模板
 * 
 * @author wangchong
 * 
 */
public class InternalRuleModTemplate {
	private String namespace;
	private List<String> shardingExpressions;
	private String shard;
	private int shardingCount;

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public List<String> getShardingExpressions() {
		return shardingExpressions;
	}

	public void setShardingExpressions(List<String> shardingExpressions) {
		this.shardingExpressions = shardingExpressions;
	}

	public String getShard() {
		return shard;
	}

	public void setShard(String shard) {
		this.shard = shard;
	}

	public int getShardingCount() {
		return shardingCount;
	}

	public void setShardingCount(int shardingCount) {
		this.shardingCount = shardingCount;
	}
}
