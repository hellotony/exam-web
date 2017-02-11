package com.exam.support.mybatis.client.datasources;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.Ordered;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Set;

/**
 * 从DataSourceService中根据数据源标识，获取数据源
 * 
 * @author wangchong
 * 
 */
public class CobarLookupDataSourceFromDataSourceServiceFacotryBean implements FactoryBean<DataSource>, Ordered {

	private ICobarDataSourceService cobarDataSourceService;

	private String identityName;

	private boolean mainDataSource;

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	@Override
	public DataSource getObject() throws Exception {
		Set<CobarDataSourceDescriptor> descriptors = cobarDataSourceService.getDataSourceDescriptors();
		Iterator<CobarDataSourceDescriptor> it = descriptors.iterator();
		while (it.hasNext()) {
			CobarDataSourceDescriptor descriptor = it.next();
			if (descriptor.getIdentity().equalsIgnoreCase(identityName)) {
				if (mainDataSource) {
					return descriptor.getTargetDataSource();
				}
				if (descriptor.getStandbyDataSource() != null) {
					return descriptor.getStandbyDataSource();
				}
			}
		}
		throw new IllegalAccessException("can not find " + identityName + "[isMain=" + mainDataSource + "]");
	}

	@Override
	public Class<?> getObjectType() {
		return DataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setCobarDataSourceService(ICobarDataSourceService cobarDataSourceService) {
		this.cobarDataSourceService = cobarDataSourceService;
	}

	public void setIdentityName(String identityName) {
		this.identityName = identityName;
	}

	public void setMainDataSource(boolean mainDataSource) {
		this.mainDataSource = mainDataSource;
	}
}
