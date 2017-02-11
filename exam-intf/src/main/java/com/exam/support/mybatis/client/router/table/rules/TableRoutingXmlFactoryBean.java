package com.exam.support.mybatis.client.router.table.rules;

import com.thoughtworks.xstream.XStream;
import com.exam.support.mybatis.client.router.table.config.vo.ExclusiveMapper;
import com.exam.support.mybatis.client.router.table.config.vo.InternalTableRule;
import com.exam.support.mybatis.client.router.table.config.vo.InternalTableRules;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 分表路由工厂类<br>
 * 通过Spring schema定义，注入配置文件，然后通过xstream进行分表路由xml解析
 * 
 * @author wangchong
 * 
 */
public class TableRoutingXmlFactoryBean implements FactoryBean<TableRoutingRouter> {
	private Resource[] configLocations;

	@Override
	public TableRoutingRouter getObject() throws Exception {
		if (ArrayUtils.isNotEmpty(configLocations)) {
			Set<InternalTableRule> ruleSets = new LinkedHashSet<InternalTableRule>();
			for (Resource resource : configLocations) {
				XStream xstream = new XStream();
				xstream.autodetectAnnotations(true);
				xstream.alias("tableRules", InternalTableRules.class);
				xstream.alias("tableRule", InternalTableRule.class);
				xstream.alias("exclusive", ExclusiveMapper.class);
				xstream.addImplicitCollection(InternalTableRules.class, "rules");
				InternalTableRules internalTableRules = (InternalTableRules) xstream.fromXML(resource.getInputStream());
				ruleSets.addAll(internalTableRules.getRules());
			}
			TableRoutingRouter routingRouter = new TableRoutingRouter();
			routingRouter.setInternalTableRules(new InternalTableRules(new ArrayList<InternalTableRule>(ruleSets)));
			return routingRouter;
		}
		return null;
	}

	@Override
	public Class<?> getObjectType() {
		return TableRoutingRouter.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Resource[] getConfigLocations() {
		return configLocations;
	}

	public void setConfigLocations(Resource[] configLocations) {
		this.configLocations = configLocations;
	}
}
