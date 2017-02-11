/**
 * Copyright 1999-2011 Alibaba Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exam.support.mybatis.client.datasources;

import com.exam.support.mybatis.client.CobarConstants;
import com.exam.support.mybatis.client.datasources.ha.IHADataSourceCreator;
import com.exam.support.mybatis.client.datasources.ha.NonHADataSourceCreator;
import com.exam.support.mybatis.client.datasources.ha.support.IDataSourcePostProcessor;
import com.exam.support.mybatis.client.support.utils.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.*;

/**
 * StrongRefDataSourceLocator is mainly responsible for assembling data sources
 * mapping relationship as per data source definitions in spring container.
 * 
 * @author fujohnwang
 */
public class DefaultCobarDataSourceService implements ICobarDataSourceService, InitializingBean {
	private Set<CobarDataSourceDescriptor> dataSourceDescriptors = new LinkedHashSet<CobarDataSourceDescriptor>();
	private List<IDataSourcePostProcessor> dataSourcePostProcessor = new ArrayList<IDataSourcePostProcessor>();
	private IHADataSourceCreator haDataSourceCreator;
	private Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
	/**
	 * 数据源索引号查找定位器
	 */
	private Map<DataSource, Integer> dataSourceIndexMap = new HashMap<DataSource, Integer>();

	public Map<String, DataSource> getDataSources() {
		return dataSources;
	}

	public void afterPropertiesSet() throws Exception {
		if (getHaDataSourceCreator() == null) {
			setHaDataSourceCreator(new NonHADataSourceCreator());
		}
		if (CollectionUtils.isEmpty(dataSourceDescriptors)) {
			return;
		}
		int count = 0;
		for (CobarDataSourceDescriptor descriptor : getDataSourceDescriptors()) {
			Validate.notEmpty(descriptor.getIdentity());
			Validate.notNull(descriptor.getTargetDataSource());

			DataSource dataSourceToUse = descriptor.getTargetDataSource();

			if (descriptor.getStandbyDataSource() != null) {
				dataSourceToUse = getHaDataSourceCreator().createHADataSource(descriptor);
				if (CollectionUtils.isNotEmpty(dataSourcePostProcessor)) {
					for (IDataSourcePostProcessor pp : dataSourcePostProcessor) {
						dataSourceToUse = pp.postProcess(dataSourceToUse);
					}
				}
			}
			LazyConnectionDataSourceProxy proxy = new LazyConnectionDataSourceProxy(dataSourceToUse);
			dataSources.put(descriptor.getIdentity(), proxy);
			dataSourceIndexMap.put(proxy, descriptor.getIndex());
			if (descriptor.getIndex() == CobarConstants.DEFAULT_DATASOURCE_INDEX) {
				dataSourceIndexMap.put(proxy, count);
			}
			count++;
		}
	}

	public void setDataSourceDescriptors(Set<CobarDataSourceDescriptor> dataSourceDescriptors) {
		this.dataSourceDescriptors = dataSourceDescriptors;
	}

	public Set<CobarDataSourceDescriptor> getDataSourceDescriptors() {
		return dataSourceDescriptors;
	}

	public void setHaDataSourceCreator(IHADataSourceCreator haDataSourceCreator) {
		this.haDataSourceCreator = haDataSourceCreator;
	}

	public IHADataSourceCreator getHaDataSourceCreator() {
		return haDataSourceCreator;
	}

	@Override
	public int getDataSourceIndex(DataSource dataSource) {
		if (dataSourceIndexMap != null && !dataSourceIndexMap.isEmpty() && dataSourceIndexMap.containsKey(dataSource)) {
			return dataSourceIndexMap.get(dataSource);
		}
		return CobarConstants.DEFAULT_DATASOURCE_INDEX;
	}
}
