package com.exam.support.mybatis.client.datasources;

import com.exam.support.mybatis.client.datasources.ha.IHADataSourceCreator;
import com.exam.support.mybatis.client.datasources.vo.DatabaseTemplate;
import com.exam.support.mybatis.client.router.table.utils.BeanReflectUtils;
import org.springframework.beans.factory.FactoryBean;

import javax.sql.DataSource;
import java.util.*;
import java.util.Map.Entry;

/**
 * DataSrouceService的工厂bean，解决多数据源配置问题
 * 
 * @author wangchong
 * 
 */
public class CobarDataSourceServiceFactoryBean implements FactoryBean<ICobarDataSourceService> {
	private static final String JDBC_URL_NAME = "jdbcUrl";

	/**
	 * 数据源模板
	 */
	private List<DatabaseTemplate> templates;
	
	/**
	 * 根据监测情况，动态返回数据源的高可用创建器
	 */
	private IHADataSourceCreator haDataSourceCreator;

	/**
	 * 外部引入，由CobarDataSourceService来管理的数据源
	 */
	private List<CobarDataSourceDescriptor> imports;

	/**
	 * 批量创建数据源<br>
	 * 创建双数据源
	 */
	@Override
	public ICobarDataSourceService getObject() throws Exception {
		Set<CobarDataSourceDescriptor> dataSourceDescriptors = new LinkedHashSet<CobarDataSourceDescriptor>();
		Object mainDatabase = null;
		Object standbyDatabase = null;
		for(DatabaseTemplate template : templates){
			for (int i = 0; i < template.getShardingCount(); i++) {
				mainDatabase = generateClassInstance(template.getClassName());
				if (isHA()) {
					standbyDatabase = generateClassInstance(template.getClassName());
				}
				Map<String, String> propertyMap = template.getPropertyMap();
				Iterator<Entry<String, String>> it = propertyMap.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, String> entry = it.next();
					BeanReflectUtils.setProperty(mainDatabase, entry.getKey(), entry.getValue());
					if (isHA()) {
						BeanReflectUtils.setProperty(standbyDatabase, entry.getKey(), entry.getValue());
					}
				}
				String[] database = template.getMatcher().doMatch(i);
				BeanReflectUtils.setProperty(mainDatabase, JDBC_URL_NAME, database[0]);
				if (isHA()) {
					if (database[1] != null) {
						BeanReflectUtils.setProperty(standbyDatabase, JDBC_URL_NAME, database[1]);
					} else {
						BeanReflectUtils.setProperty(standbyDatabase, JDBC_URL_NAME, database[0]);
					}
				}
				CobarDataSourceDescriptor dataSourceDescriptor = new CobarDataSourceDescriptor();
				dataSourceDescriptor.setIdentity(template.match(i));
				dataSourceDescriptor.setIndex(i);
				if (template.getPoolSize() != 0) {
					dataSourceDescriptor.setPoolSize(template.getPoolSize());
				}
				dataSourceDescriptor.setTargetDataSource((DataSource) mainDatabase);
				dataSourceDescriptor.setTargetDetectorDataSource((DataSource) mainDatabase);
				if (isHA()) {
					dataSourceDescriptor.setStandbyDataSource((DataSource) standbyDatabase);
					dataSourceDescriptor.setStandbyDetectorDataSource((DataSource) standbyDatabase);
				}
				dataSourceDescriptors.add(dataSourceDescriptor);

			}
		}
		if (imports != null) {
			int autoCreateSize = dataSourceDescriptors.size();
			for (CobarDataSourceDescriptor descriptor : imports) {
				descriptor.setIndex(autoCreateSize);
				autoCreateSize++;
				dataSourceDescriptors.add(descriptor);
			}
		}
		DefaultCobarDataSourceService dataSourceService = new DefaultCobarDataSourceService();
		dataSourceService.setDataSourceDescriptors(dataSourceDescriptors);
		dataSourceService.setHaDataSourceCreator(getHaDataSourceCreator());
		dataSourceService.afterPropertiesSet();

		return dataSourceService;
	}

	/**
	 * 通过类加载器加载数据源实现类
	 * 
	 * @return
	 * @throws Exception
	 */
	private Object generateClassInstance(String className) throws Exception {
		return getContextClassLoader().loadClass(className).newInstance();
	}

	private boolean isHA() {
		return getHaDataSourceCreator() != null;
	}

	private ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	@Override
	public Class<?> getObjectType() {
		return ICobarDataSourceService.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public List<CobarDataSourceDescriptor> getImports() {
		return imports;
	}

	public void setImports(List<CobarDataSourceDescriptor> imports) {
		this.imports = imports;
	}
	

	public IHADataSourceCreator getHaDataSourceCreator() {
		return haDataSourceCreator;
	}

	public void setHaDataSourceCreator(IHADataSourceCreator haDataSourceCreator) {
		this.haDataSourceCreator = haDataSourceCreator;
	}

	public void setTemplates(List<DatabaseTemplate> templates) {
		this.templates = templates;
	}
}
