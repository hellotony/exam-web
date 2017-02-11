package com.exam.support.mybatis.client.datasources.vo;

import com.exam.support.mybatis.client.datasources.IMatcher;
import com.exam.support.mybatis.client.router.table.utils.FreemarkerUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库模板，处理批量数据源配置
 * 
 * @author wangchong
 * 
 */
public class DatabaseTemplate {
	/**
	 * JDBC连接池实现类(datasource的实现类)
	 */
	private String className;
	/**
	 * 各个datasource的标识规则
	 */
	private String identity;

	/**
	 * 通配处理器
	 */
	private IMatcher<String[], Integer> matcher;
	
	/**
	 * 线程池大小
	 */
	private int poolSize;

	/**
	 * JDBC连接池实现类的连接池属性集合
	 */
	private Map<String, String> propertyMap = new HashMap<String, String>();
	
	/**
	 * 生成的分库配置实例数
	 */
	private int shardingCount;
	

	public String match(int index) {
		if (StringUtils.isNotBlank(identity)) {
			Map<String, Object> env = new HashMap<String, Object>();
			env.put("index", String.valueOf(index));
			return FreemarkerUtils.execute(identity, env);
		}
		return identity;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(Map<String, String> propertyMap) {
		this.propertyMap = propertyMap;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}


	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
	

	public IMatcher<String[], Integer> getMatcher() {
		return matcher;
	}

	public void setMatcher(IMatcher<String[], Integer> matcher) {
		this.matcher = matcher;
	}
	
	public int getShardingCount() {
		return shardingCount;
	}

	public void setShardingCount(int shardingCount) {
		this.shardingCount = shardingCount;
	}

}
