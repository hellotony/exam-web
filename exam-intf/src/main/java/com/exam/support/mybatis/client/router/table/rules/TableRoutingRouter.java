package com.exam.support.mybatis.client.router.table.rules;

import com.exam.support.mybatis.client.router.ICobarRouter;
import com.exam.support.mybatis.client.router.ModFunction;
import com.exam.support.mybatis.client.router.RoutingException;
import com.exam.support.mybatis.client.router.table.config.vo.InternalTableRule;
import com.exam.support.mybatis.client.router.table.config.vo.InternalTableRules;
import org.apache.commons.lang3.StringUtils;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分表路由计算
 * 
 * @author wangchong
 * 
 */
public class TableRoutingRouter implements ICobarRouter<TableRouterResult, TableRoutingFact> {
	private static Logger logger = LoggerFactory.getLogger(TableRoutingRouter.class);
	/**
	 * 分表路由规则
	 */
	private InternalTableRules internalTableRules;

	private Map<String, Object> functionMap = new HashMap<String, Object>();

	@Override
	public TableRouterResult doRoute(TableRoutingFact routingFact) throws RoutingException {
		if (internalTableRules != null) {
			List<InternalTableRule> tableRules = internalTableRules.getRules();
			for (InternalTableRule tableRule : tableRules) {
				if (matchsNamespace(routingFact.getStamtement(), tableRule.getNamespace())// 命名空间匹配
						|| matchsClassName(routingFact.getArgument(), tableRule.getClazz())) { // 类名匹配
					if (!StringUtils.isBlank(tableRule.getShardingExpression())) { // 优先计算表达式
						return parseExpression(tableRule, routingFact);
					}
					if (!StringUtils.isBlank(tableRule.getShardingParameter())) { // 其次计算分表参数
						return parseParameter(tableRule, routingFact);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 类名匹配
	 * 
	 * @param argument
	 *            api传入参数对象
	 * @param clazzName
	 *            规则定义的类名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean matchsClassName(Object argument, String clazzName) {
		String clazz = null;
		if (argument instanceof Map<?, ?>) {
			Map<String, Object> param = (Map<String, Object>) argument;
			clazz = (String) param.get("class");
		}
		if (StringUtils.isBlank(clazz)) {
			clazz = argument.getClass().getName();
		}
		return clazz.equals(clazzName);
	}

	/**
	 * 命名空间匹配
	 * 
	 * @param stamtement
	 *            api传入mapper的id
	 * @param namespace
	 *            规则定义的命名空间
	 * @return
	 */
	private boolean matchsNamespace(String stamtement, String namespace) {
		return StringUtils.substringBeforeLast(stamtement, ".").equals(namespace);
	}

	/**
	 * 分表参数路由解析<br>
	 * 使用MVEL表达式引擎进行参数值的获取与匹配
	 * 
	 * @param tableRule
	 *            分表路由规则
	 * @param routingFact
	 *            分表路由参数载体
	 * @return
	 */
	private TableRouterResult parseParameter(InternalTableRule tableRule, TableRoutingFact routingFact) {
		try {
			Map<String, Object> vrs = new HashMap<String, Object>();
			vrs.putAll(getFunctionMap());
			vrs.put("$ROOT", routingFact.getArgument());
			VariableResolverFactory vrfactory = new MapVariableResolverFactory(vrs);
			Object calculate = MVEL.eval(tableRule.getShardingParameter(), routingFact.getArgument(), vrfactory);
			return new TableRouterResult(tableRule.getMainTable(), String.format(tableRule.getSplitTableFormat(),
					calculate));
		} catch (Throwable e) {
			logger.info(e.getMessage() + ". TableRule {parameter:" + tableRule.getShardingParameter() + ",argument:"
					+ routingFact.getArgument(), e);
		}
		return null;
	}

	/**
	 * 表达式路由解析<br>
	 * MVEL表达式引擎进行表达式的计算，该引擎支持java方法的调用 <br>
	 * <code>
	 * 		String str = "A";
	 * 		String expression="str.startWith('A')"
	 * 		//MVEL在计算该表达式时，会调用String内置方法startWith，进行匹配
	 * </code>
	 * 
	 * @param tableRule
	 * @param routingFact
	 * @return
	 */
	private TableRouterResult parseExpression(InternalTableRule tableRule, TableRoutingFact routingFact) {
		try {
			Map<String, Object> vrs = new HashMap<String, Object>();
			vrs.putAll(getFunctionMap());
			// 内置取模函数
			vrs.put("mod", new ModFunction(tableRule.getShardingCount()));
			vrs.put("$ROOT", routingFact.getArgument());
			VariableResolverFactory vrfactory = new MapVariableResolverFactory(vrs);
			String calculate = MVEL.evalToString(tableRule.getShardingExpression(), routingFact.getArgument(),
					vrfactory);
			return new TableRouterResult(tableRule.getMainTable(), String.format(tableRule.getSplitTableFormat(),
					calculate));
		} catch (Exception e) {
			logger.info(e.getMessage() + ". TableRule {expression:" + tableRule.getShardingExpression() + ",argument:"
					+ routingFact.getArgument(), e);
		}
		return null;
	}

	public InternalTableRules getInternalTableRules() {
		return internalTableRules;
	}

	public void setInternalTableRules(InternalTableRules internalTableRules) {
		this.internalTableRules = internalTableRules;
	}

	public Map<String, Object> getFunctionMap() {
		if (functionMap == null) {
			functionMap = new HashMap<String, Object>();
		}
		return functionMap;
	}

	public void setFunctionMap(Map<String, Object> functionMap) {
		this.functionMap = functionMap;
	}
}
