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
package com.exam.support.mybatis.client.support.execution;

import com.exam.support.mybatis.client.support.SqlSessionCallback;
import com.exam.support.mybatis.client.support.utils.CollectionUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DefaultConcurrentRequestProcessor implements IConcurrentRequestProcessor {

	private transient final Logger logger = LoggerFactory.getLogger(DefaultConcurrentRequestProcessor.class);

	private SqlSessionFactory sqlSessionFactory;

	private ExecutorType executorType;

	public DefaultConcurrentRequestProcessor() {
	}

	public DefaultConcurrentRequestProcessor(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
		this.sqlSessionFactory = sqlSessionFactory;
		this.executorType = executorType;
	}

	public List<Object> process(List<ConcurrentRequest> requests) {
		List<Object> resultList = new ArrayList<Object>();

		if (CollectionUtils.isEmpty(requests)) {
			return resultList;
		}

		List<RequestDepository> requestsDepo = fetchConnectionsAndDepositForLaterUse(requests);
		final CountDownLatch latch = new CountDownLatch(requestsDepo.size());
		List<Future<Object>> futures = new ArrayList<Future<Object>>();
		Map<Connection, SqlSession> sqlSessionMapper = new HashMap<Connection, SqlSession>();
		try {

			for (RequestDepository rdepo : requestsDepo) {
				final ConcurrentRequest request = rdepo.getOriginalRequest();
				final SqlSessionCallback action = request.getAction();
				final Connection connection = rdepo.getConnectionToUse();
				final SqlSession session = getSqlSessionFactory().openSession(executorType, connection);
				final int dbIndex = request.getDataSourceIndex();
				sqlSessionMapper.put(connection, session);
				futures.add(request.getExecutor().submit(new Callable<Object>() {
					public Object call() throws Exception {
						try {
							return executeWith(session, action, dbIndex);
						} finally {
							latch.countDown();
						}
					}
				}));
			}
			try {
				latch.await();
			} catch (InterruptedException e) {
				throw new ConcurrencyFailureException("interrupted when processing data access request in concurrency",
						e);
			}

		} finally {
			for (RequestDepository depo : requestsDepo) {
				Connection springCon = depo.getConnectionToUse();
				DataSource dataSource = depo.getOriginalRequest().getDataSource();
				boolean isRealRequireClosedConnection = true;
				try {
					if (springCon != null) {
						if (depo.isTransactionAware()) {
							springCon.close();
						} else {
							DataSourceUtils.doReleaseConnection(springCon, dataSource);
						}
						isRealRequireClosedConnection = springCon.isClosed();
					}
				} catch (Throwable ex) {
					logger.info("Could not close JDBC Connection", ex);
				}
				try {
					if (isRealRequireClosedConnection && springCon != null) {
						SqlSession session = sqlSessionMapper.get(springCon);
						if (session != null) {
							session.close();
						}
					}
				} catch (Throwable ex) {
					logger.info("Could not close SqlSession", ex);
				}
			}
		}

		fillResultListWithFutureResults(futures, resultList);

		return resultList;
	}

	protected Object executeWith(SqlSession session, SqlSessionCallback action, int dbIndex) {
		try {
			return action.doInSqlSession(session, dbIndex);
		} catch (SQLException ex) {
			throw new SQLErrorCodeSQLExceptionTranslator().translate("SqlSession operation", null, ex);
		}
	}

	private void fillResultListWithFutureResults(List<Future<Object>> futures, List<Object> resultList) {
		for (Future<Object> future : futures) {
			try {
				resultList.add(future.get());
			} catch (InterruptedException e) {
				throw new ConcurrencyFailureException("interrupted when processing data access request in concurrency",
						e);
			} catch (ExecutionException e) {
				throw new ConcurrencyFailureException("something goes wrong in processing", e);
			}
		}
	}

	private List<RequestDepository> fetchConnectionsAndDepositForLaterUse(List<ConcurrentRequest> requests) {
		List<RequestDepository> depos = new ArrayList<RequestDepository>();
		for (ConcurrentRequest request : requests) {
			DataSource dataSource = request.getDataSource();

			Connection springCon = null;
			boolean transactionAware = (dataSource instanceof TransactionAwareDataSourceProxy);
			try {
				springCon = (transactionAware ? dataSource.getConnection() : DataSourceUtils
						.doGetConnection(dataSource));
			} catch (SQLException ex) {
				throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", ex);
			}

			RequestDepository depo = new RequestDepository();
			depo.setOriginalRequest(request);
			depo.setConnectionToUse(springCon);
			depo.setTransactionAware(transactionAware);
			depos.add(depo);
		}

		return depos;
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
}
