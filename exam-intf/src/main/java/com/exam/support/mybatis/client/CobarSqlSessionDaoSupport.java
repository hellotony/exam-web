package com.exam.support.mybatis.client;

import org.apache.ibatis.executor.BatchResult;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.util.Collection;
import java.util.List;

/**
 * 对批理处理操作的封装 1.ExecutorType必须是Batch模式<br>
 * 2.sharding模式下，采用普通的增删改操作方式<br>
 * 3.非sharding模式下，采用Mybatis提供的批处理操作方式<br>
 * (ps: spring-mybatis的实现上需要支持事务，才可以使用批处理<br>
 * 
 * @see SqlSessionTemplate.SqlSessionInterceptor#isSqlSessionTransactional)
 * 
 * @author wangchong
 * 
 */
public class CobarSqlSessionDaoSupport extends SqlSessionDaoSupport {
	public int batchInsert(final String statementName, final Collection<?> entities) throws DataAccessException {
		if (isPartitionBehaviorEnabled()) {
			int counter = 0;
			DataAccessException lastEx = null;
			for (Object parameterObject : entities) {
				try {
					getSqlSession().insert(statementName, parameterObject);
					counter++;
				} catch (DataAccessException e) {
					lastEx = e;
				}
			}
			if (lastEx != null) {
				throw lastEx;
			}
			return counter;
		} else {
			DataAccessException lastEx = null;
			for (Object parameterObject : entities) {
				try {
					getSqlSession().insert(statementName, parameterObject);
				} catch (DataAccessException e) {
					lastEx = e;
				}
			}
			if (lastEx != null) {
				throw lastEx;
			}
			return sumBatchResultCount(getSqlSession().flushStatements());
		}
	}

	public int batchDelete(final String statementName, final Collection<?> entities) throws DataAccessException {
		if (isPartitionBehaviorEnabled()) {
			int counter = 0;
			DataAccessException lastEx = null;
			for (Object entity : entities) {
				try {
					counter += getSqlSession().delete(statementName, entity);
				} catch (DataAccessException e) {
					lastEx = e;
				}
			}
			if (lastEx != null) {
				throw lastEx;
			}
			return counter;
		} else {
			DataAccessException lastEx = null;
			for (Object entity : entities) {
				try {
					getSqlSession().delete(statementName, entity);
				} catch (DataAccessException e) {
					lastEx = e;
				}
			}
			if (lastEx != null) {
				throw lastEx;
			}
			return sumBatchResultCount(getSqlSession().flushStatements());
		}
	}

	private int sumBatchResultCount(List<BatchResult> batchResults) {
		int counter = 0;
		if (batchResults.size() != 1) {
			throw new InvalidDataAccessResourceUsageException("Batch execution returned invalid results. "
					+ "Expected 1 but number of BatchResult objects returned was " + batchResults.size());
		}
		int[] updateCounts = batchResults.get(0).getUpdateCounts();
		for (int i = 0; i < updateCounts.length; i++) {
			counter += updateCounts[i];
		}
		return counter;
	}

	public int batchUpdate(final String statementName, final Collection<?> entities) throws DataAccessException {
		if (isPartitionBehaviorEnabled()) {
			int counter = 0;
			DataAccessException lastEx = null;
			for (Object parameterObject : entities) {
				try {
					counter += getSqlSession().update(statementName, parameterObject);
				} catch (DataAccessException e) {
					lastEx = e;
				}
			}
			if (lastEx != null) {
				throw lastEx;
			}
			return counter;
		} else {
			DataAccessException lastEx = null;
			for (Object parameterObject : entities) {
				try {
					getSqlSession().update(statementName, parameterObject);
				} catch (DataAccessException e) {
					lastEx = e;
				}
			}
			if (lastEx != null) {
				throw lastEx;
			}
			return sumBatchResultCount(getSqlSession().flushStatements());
		}
	}

	protected boolean isPartitionBehaviorEnabled() {
		if (getSqlSession() instanceof CobarSqlSessionTemplate) {
			return ((CobarSqlSessionTemplate) getSqlSession()).isPartitioningBehaviorEnabled();
		}
		return false;
	}
}
