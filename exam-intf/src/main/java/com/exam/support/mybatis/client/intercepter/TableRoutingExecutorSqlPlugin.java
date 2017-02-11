package com.exam.support.mybatis.client.intercepter;

import com.exam.support.mybatis.client.router.table.config.vo.InternalTableRule;
import com.exam.support.mybatis.client.router.table.config.vo.InternalTableRules;
import com.exam.support.mybatis.client.router.table.rules.TableRoutingRouter;
import com.exam.support.mybatis.client.support.vo.BatchInsertTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sql执行拦截器<br>
 * Executor执行器在执行Sql之前，对SQL进行表名处理
 * 
 * 支持两种SQL的书写方式
 * 
 * <pre>
 * 	<li> select id from table where name=#{name}</li>
 *  <li> select id from ${table} where name=#{name}</li>
 * </pre>
 * 
 * ps:第二种相当于TableRoutingExecutorSqlPlugin无效，是一种向下兼容处理
 * 
 * @author wangchong
 * 
 */

public class TableRoutingExecutorSqlPlugin extends AbstractExecutorSqlPlugin implements InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(TableRoutingExecutorSqlPlugin.class);

	/**
	 * 分表路由规则容器
	 * 
	 * <pre>
	 * 结构如下：
	 * ---------------------------------------------
	 * namespace   		| maintable                |
	 * ---------------------------------------------
	 * productDatePrice | product_date_price       |
	 * ---------------------------------------------
	 * </pre>
	 */
	private Map<String, String> tableRuleContainter = new HashMap<String, String>();
	/**
	 * 分表路由器
	 */
	private TableRoutingRouter tableRoutingRouter;

	@Override
	protected boolean hasPartitional(String sqlAction, Object parameterObject) {
		return tableRuleContainter.get(StringUtils.substringBeforeLast(sqlAction, ".")) != null;
	}

	@Override
	protected String postProcess(String sqlAction, String boundSql, Object parameterObject) {
		if (parameterObject instanceof Map<?, ?> || parameterObject instanceof BatchInsertTask) {
			String namespace = StringUtils.substringBeforeLast(sqlAction, ".");
			try {
				String mainTableName = tableRuleContainter.get(namespace);
				String subTable = lookupSubTable(parameterObject, mainTableName);
				if (logger.isDebugEnabled()) {
					logger.debug("Before process namespace{} - sql{}", namespace, boundSql);
				}
				if (boundSql.indexOf(subTable) == -1) { // 处理已经做过表名替换的${table}写法
					String newSql = boundSql.replaceAll(mainTableName, subTable);
					if (logger.isDebugEnabled()) {
						logger.debug("After process namespace{} - sql{}", namespace, boundSql);
					}
					return newSql;
				}
			} catch (Exception e) {
				logger.warn("when process the sql, occur errors. namesace({})-sql({})", namespace, boundSql);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private String lookupSubTable(Object parameterObject, String mainTableName) {
		Map<String, Object> map = (Map<String, Object>) parameterObject;
		String subTable = (String) map.get(mainTableName);
		return subTable;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Validate.notNull(tableRoutingRouter);
		buildTableRuleContainter();
	}

	private void buildTableRuleContainter() {
		InternalTableRules internalTableRules = tableRoutingRouter.getInternalTableRules();
		List<InternalTableRule> tableRules = internalTableRules.getRules();
		for (InternalTableRule rule : tableRules) {
			Validate.notNull(rule.getNamespace());
			Validate.notNull(rule.getMainTable());
			tableRuleContainter.put(rule.getNamespace(), rule.getMainTable());
		}
	}

	public void setTableRoutingRouter(TableRoutingRouter tableRoutingRouter) {
		this.tableRoutingRouter = tableRoutingRouter;
	}
}
