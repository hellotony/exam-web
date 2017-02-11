package com.exam.support.mybatis.client.router.table.rules;

/**
 * 分表路由结果
 * 
 * @author wangchong
 * 
 */
public class TableRouterResult {
	/**
	 * 主表名<br>
	 * 表名变量的key
	 */
	private String mainTable;

	/**
	 * 子表名<br>
	 * 表名变量的value
	 */
	private String subTable;

	public TableRouterResult() {
		super();
	}

	public TableRouterResult(String mainTable, String subTable) {
		super();
		this.mainTable = mainTable;
		this.subTable = subTable;
	}

	public String getMainTable() {
		return mainTable;
	}

	public void setMainTable(String mainTable) {
		this.mainTable = mainTable;
	}

	public String getSubTable() {
		return subTable;
	}

	public void setSubTable(String subTable) {
		this.subTable = subTable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mainTable == null) ? 0 : mainTable.hashCode());
		result = prime * result + ((subTable == null) ? 0 : subTable.hashCode());
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
		TableRouterResult other = (TableRouterResult) obj;
		if (mainTable == null) {
			if (other.mainTable != null)
				return false;
		} else if (!mainTable.equals(other.mainTable))
			return false;
		if (subTable == null) {
			if (other.subTable != null)
				return false;
		} else if (!subTable.equals(other.subTable))
			return false;
		return true;
	}
}
