package com.exam.support.mybatis.client.datasources;

import com.exam.support.mybatis.client.router.table.utils.FreemarkerUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * jdbcUrl的匹配处理
 * 
 * @author wangchong
 * 
 */
public class JdbcUrlMatcher implements IMatcher<String[], Integer> {
	/**
	 * 主库jdbcurl的通配置规则
	 */
	private String mainRegex;
	/**
	 * 备库jdbcurl的通配置规则
	 */
	private String standbyRegex;

	public JdbcUrlMatcher() {
		super();
	}

	public JdbcUrlMatcher(String mainRegex, String standbyRegex) {
		super();
		this.mainRegex = mainRegex;
		this.standbyRegex = standbyRegex;
	}

	@Override
	public String[] doMatch(Integer regexArgument) {
		String mainJdbcUrl = null;
		String standbyJdbcUrl = null;
		Map<String, Object> env = new HashMap<String, Object>();
		env.put("index", regexArgument);
		if (StringUtils.isNotBlank(mainRegex)) {
			mainRegex = StringUtils.trimToEmpty(mainRegex);
			mainJdbcUrl = FreemarkerUtils.execute(mainRegex, env);
		}
		if (StringUtils.isNotBlank(standbyRegex)) {
			standbyRegex = StringUtils.trimToEmpty(standbyRegex);
			standbyJdbcUrl = FreemarkerUtils.execute(standbyRegex, env);
		}
		return new String[] { mainJdbcUrl, standbyJdbcUrl };
	}

	public String getMainRegex() {
		return mainRegex;
	}

	public void setMainRegex(String mainRegex) {
		this.mainRegex = mainRegex;
	}

	public String getStandbyRegex() {
		return standbyRegex;
	}

	public void setStandbyRegex(String standbyRegex) {
		this.standbyRegex = standbyRegex;
	}
}