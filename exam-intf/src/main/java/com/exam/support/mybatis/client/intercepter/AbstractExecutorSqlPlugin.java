package com.exam.support.mybatis.client.intercepter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

/**
 * Sql执行拦截器的抽象处理类
 * 
 * @author wangchong
 * 
 */
@Intercepts({
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }) })
public abstract class AbstractExecutorSqlPlugin implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement ms = getObject(invocation, 0);
		Object parameterObject = getObject(invocation, 1);
		if (hasPartitional(ms.getId(), parameterObject)) {
			invocation.getArgs()[0] = buildMappedStatement(ms, new DelegetingSqlSource(ms));
		}
		return invocation.proceed();
	}

	/**
	 * 是否需要partition
	 * 
	 * @param sqlAction
	 * @param parameterObject
	 *            执行参数
	 * @return
	 */
	protected abstract boolean hasPartitional(String sqlAction, Object parameterObject);

	/**
	 * sql处理
	 * 
	 * @param sqlAction
	 *            执行的sqlId
	 * @param boundSql
	 *            需要处理的sql
	 * @param parameterObject
	 *            执行参数
	 * @return
	 */
	protected abstract String postProcess(String sqlAction, String boundSql, Object parameterObject);

	/**
	 * 重新构建一个MappedStatement
	 * 
	 * @param ms
	 * @param sqlSource
	 * @return
	 */
	private MappedStatement buildMappedStatement(MappedStatement ms, SqlSource sqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), sqlSource,
				ms.getSqlCommandType());
		builder.cache(ms.getCache());
		builder.databaseId(ms.getDatabaseId());
		builder.fetchSize(ms.getFetchSize());
		builder.keyColumn(ArrayUtils.toString(ms.getKeyColumns()));
		builder.keyGenerator(ms.getKeyGenerator());
		builder.keyProperty(ArrayUtils.toString(ms.getKeyProperties()));
		builder.lang(ms.getLang());
		builder.resultMaps(ms.getResultMaps());
		builder.parameterMap(ms.getParameterMap());
		builder.resource(ms.getResource());
		builder.resulSets(ArrayUtils.toString(ms.getResulSets()));
		builder.useCache(ms.isUseCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.resultOrdered(ms.isResultOrdered());
		builder.resultSetType(ms.getResultSetType());
		builder.statementType(ms.getStatementType());
		builder.timeout(ms.getTimeout());
		return builder.build();
	}

	@SuppressWarnings("unchecked")
	private <T> T getObject(Invocation invocation, int index) {
		return (T) invocation.getArgs()[index];
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

	private class DelegetingBoundSql extends BoundSql {

		private BoundSql boundSql;

		public DelegetingBoundSql(BoundSql boundSql, Configuration configuration, String sql,
                                  List<ParameterMapping> parameterMappings, Object parameterObject) {
			super(configuration, sql, parameterMappings, parameterObject);
			this.boundSql = boundSql;
		}

		@Override
		public boolean hasAdditionalParameter(String name) {
			return boundSql.hasAdditionalParameter(name);
		}

		@Override
		public void setAdditionalParameter(String name, Object value) {
			boundSql.setAdditionalParameter(name, value);
		}

		@Override
		public Object getAdditionalParameter(String name) {
			return boundSql.getAdditionalParameter(name);
		}
	}

	/**
	 * SqlSource代理路由
	 * 
	 * @author wangchong
	 */
	private class DelegetingSqlSource implements SqlSource {
		private MappedStatement mappedStatement;

		public DelegetingSqlSource(MappedStatement mappedStatement) {
			super();
			this.mappedStatement = mappedStatement;
		}

		@Override
		public BoundSql getBoundSql(Object parameterObject) {
			return processSql(mappedStatement, parameterObject);
		}

		public BoundSql processSql(MappedStatement ms, Object parameterObject) {
			BoundSql boundSql = ms.getBoundSql(parameterObject);
			String processSql = postProcess(ms.getId(), boundSql.getSql(), parameterObject);
			if (StringUtils.isNotBlank(processSql)) {
				return buildBoundSql(boundSql, ms.getConfiguration(), processSql, boundSql.getParameterMappings(),
						parameterObject);
			}
			return boundSql;
		}

		private BoundSql buildBoundSql(BoundSql boundSql, Configuration configuration, String sql,
                                       List<ParameterMapping> parameterMappings, Object parameterObject) {
			return new DelegetingBoundSql(boundSql, configuration, sql, parameterMappings, parameterObject);
		}
	}

}
