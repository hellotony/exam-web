package com.exam.support.mybatis.client.router.table.config.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAnyAttribute;
import java.util.List;

/**
 * 分表路由规则对象
 * 
 * @author wangchong
 * 
 */
public class InternalTableRule {
	/** 数据库映射载体 */
	@XStreamAlias(value = "class")
	private String clazz;

	/** 主表名 */
	private String mainTable;

	/** 子表格式 */
	private String splitTableFormat;

	/** 分表数 */
	private int shardingCount;

	/** mapper的命令空间匹配 */
	private String namespace;

	/** 分表计算表达式 */
	private String shardingExpression;

	/** 分表计算参数 */
	private String shardingParameter;

	/** 不做分表处理的mapper */
	@XmlAnyAttribute
	private List<ExclusiveMapper> exclusives;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMainTable() {
		return mainTable;
	}

	public void setMainTable(String mainTable) {
		this.mainTable = mainTable;
	}

	public String getSplitTableFormat() {
		return splitTableFormat;
	}

	public void setSplitTableFormat(String splitTableFormat) {
		this.splitTableFormat = splitTableFormat;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public int getShardingCount() {
		return shardingCount;
	}

	public void setShardingCount(int shardingCount) {
		this.shardingCount = shardingCount;
	}

	public String getShardingExpression() {
		return shardingExpression;
	}

	public void setShardingExpression(String shardingExpression) {
		this.shardingExpression = shardingExpression;
	}

	public String getShardingParameter() {
		return shardingParameter;
	}

	public void setShardingParameter(String shardingParameter) {
		this.shardingParameter = shardingParameter;
	}

	public List<ExclusiveMapper> getExclusives() {
		return exclusives;
	}

	public void setExclusives(List<ExclusiveMapper> exclusives) {
		this.exclusives = exclusives;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + ((mainTable == null) ? 0 : mainTable.hashCode());
		result = prime * result + shardingCount;
		result = prime * result + ((splitTableFormat == null) ? 0 : splitTableFormat.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InternalTableRule other = (InternalTableRule) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		if (mainTable == null) {
			if (other.mainTable != null)
				return false;
		} else if (!mainTable.equals(other.mainTable))
			return false;
		if (shardingCount != other.shardingCount)
			return false;
		if (splitTableFormat == null) {
			if (other.splitTableFormat != null)
				return false;
		} else if (!splitTableFormat.equals(other.splitTableFormat))
			return false;
		return true;
	}
}
