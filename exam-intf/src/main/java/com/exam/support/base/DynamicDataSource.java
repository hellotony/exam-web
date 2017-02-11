package com.exam.support.base;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * spring动态数据源
 */
public class DynamicDataSource  extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceType();
    }
}
