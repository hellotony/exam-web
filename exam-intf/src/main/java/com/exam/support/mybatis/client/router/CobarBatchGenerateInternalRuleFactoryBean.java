package com.exam.support.mybatis.client.router;

import com.exam.support.mybatis.client.router.config.vo.InternalRule;
import com.exam.support.mybatis.client.router.table.utils.FreemarkerUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.FactoryBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据模板批量生成分库路由规则
 * 
 * @author wangchong
 * 
 */
public class CobarBatchGenerateInternalRuleFactoryBean implements FactoryBean<List<InternalRule>> {
	/**
	 * 路由模板集
	 */
	private List<InternalRuleModTemplate> templates;

	@Override
	public List<InternalRule> getObject() throws Exception {
		List<InternalRule> internalRules = new ArrayList<InternalRule>();
		for (InternalRuleModTemplate template : templates) {
			Validate.isTrue(template.getShardingCount() > 0);
			for (int i = 0; i < template.getShardingCount(); i++) {
				for (String expression : template.getShardingExpressions()) {
					InternalRule internalRule = new InternalRule();
					internalRule.setNamespace(template.getNamespace());
					internalRule.setShardingExpression(matchs(expression, i));
					internalRule.setShards(matchs(template.getShard(), i));
					internalRules.add(internalRule);
				}
			}
		}
		return internalRules;
	}

	private String matchs(String pattern, int shard) {
		Map<String, Object> argument = new HashMap<String, Object>();
		argument.put("index", shard);
		return FreemarkerUtils.execute(pattern, argument);
	}

	@Override
	public Class<?> getObjectType() {
		return List.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public List<InternalRuleModTemplate> getTemplates() {
		return templates;
	}

	public void setTemplates(List<InternalRuleModTemplate> templates) {
		this.templates = templates;
	}
}
