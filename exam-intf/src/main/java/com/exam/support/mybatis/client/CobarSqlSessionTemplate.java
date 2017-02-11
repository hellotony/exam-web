package com.exam.support.mybatis.client;

import com.exam.support.mybatis.client.datasources.CobarDataSourceDescriptor;
import com.exam.support.mybatis.client.datasources.ICobarDataSourceService;
import com.exam.support.mybatis.client.exception.UncategorizedCobarClientException;
import com.exam.support.mybatis.client.merger.IMerger;
import com.exam.support.mybatis.client.router.ICobarRouter;
import com.exam.support.mybatis.client.router.support.IBatisRoutingFact;
import com.exam.support.mybatis.client.router.support.RoutingResult;
import com.exam.support.mybatis.client.router.table.rules.TableRouterResult;
import com.exam.support.mybatis.client.router.table.rules.TableRoutingFact;
import com.exam.support.mybatis.client.router.table.utils.BeanReflectUtils;
import com.exam.support.mybatis.client.support.SqlSessionCallback;
import com.exam.support.mybatis.client.support.execution.ConcurrentRequest;
import com.exam.support.mybatis.client.support.execution.DefaultConcurrentRequestProcessor;
import com.exam.support.mybatis.client.support.execution.IConcurrentRequestProcessor;
import com.exam.support.mybatis.client.support.utils.CollectionUtils;
import com.exam.support.mybatis.client.support.utils.MapUtils;
import com.exam.support.mybatis.client.support.utils.Predicate;
import com.exam.support.mybatis.client.support.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

/**
 * 基于mybatis-spring，对SqlSessionTemplate做二次封装
 * 
 * @author wangchong
 * 
 */
public class CobarSqlSessionTemplate extends SqlSessionTemplate implements InitializingBean, DisposableBean {

    private transient Logger logger = LoggerFactory.getLogger(CobarSqlSessionTemplate.class);

    /**
     * 默认的数据源名称
     */
    private String defaultDataSourceName = CobarConstants.DEFAULT_DATASOURCE_IDENTITY;

    /**
     * 数据源管理器
     */
    private ICobarDataSourceService cobarDataSourceService;

    /**
     * 分库路由器
     */
    private ICobarRouter<RoutingResult, IBatisRoutingFact> router;

    /**
     * 分表路由器
     */
    private ICobarRouter<TableRouterResult, TableRoutingFact> tableRouter;

    /**
     * 并行处理器
     */
    private IConcurrentRequestProcessor concurrentRequestProcessor;

    /**
     * 数据源名称与线程执行器的映射
     */
    private Map<String, ExecutorService> dataSourceSpecificExecutors = new HashMap<String, ExecutorService>();

    /**
     * 数据Merge接口
     */
    private Map<String, IMerger<Object, Object>> mergers = new HashMap<String, IMerger<Object, Object>>();

    /**
     * 线程执行器容器
     */
    private List<ExecutorService> internalExecutorServiceRegistry = new ArrayList<ExecutorService>();

    /**
     * 是否打印超时查询SQL
     */
    private boolean profileLongTimeRunningSql = false;

    /**
     * 打印超时查询SQL的阀值
     */
    private long longTimeRunningSqlIntervalThreshold;

    public CobarSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType,
                                   PersistenceExceptionTranslator exceptionTranslator) {
        super(sqlSessionFactory, executorType, exceptionTranslator);
    }

    public CobarSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
        super(sqlSessionFactory, executorType);
    }

    public CobarSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T selectOne(final String statement, final Object parameter) {
        long startTimestamp = System.currentTimeMillis();
        try {
            if (isPartitioningBehaviorEnabled()) {

                SortedMap<String, DataSource> dsMap = lookupDataSourcesByRouter(statement, parameter);
                if (!MapUtils.isEmpty(dsMap)) {
                    SqlSessionCallback callback = new SqlSessionCallback() {
                        @Override
                        public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                            return sqlSession.selectOne(statement,
                                    refactorArgumentWithTableRouting(statement, parameter, dbIndex));
                        }
                    };
                    List<Object> resultList = executeInConcurrency(callback, dsMap);
                    Collection<T> filteredResultList = CollectionUtils.select(resultList, new Predicate<T>() {
                        public boolean evaluate(T item) {
                            return item != null;
                        }
                    });
                    if (filteredResultList.size() > 1) {
                        throw new IncorrectResultSizeDataAccessException(1);
                    }
                    if (CollectionUtils.isEmpty(filteredResultList)) {
                        return null;
                    }
                    return filteredResultList.iterator().next();
                }
            } // end if for partitioning status checking
            return super.selectOne(statement,
                    refactorArgumentWithTableRouting(statement, parameter, CobarConstants.DEFAULT_DATASOURCE_INDEX));
        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn(
                            "SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                            new Object[] { statement, parameter, interval });
                }
            }
        }
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return this.selectMap(statement, mapKey, null, null);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return this.selectMap(statement, parameter, mapKey, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> selectMap(final String statement, final Object parameter, final String mapKey,
            final RowBounds rowBounds) {
        long startTimestamp = System.currentTimeMillis();
        try {
            if (isPartitioningBehaviorEnabled()) {

                SortedMap<String, DataSource> dsMap = lookupDataSourcesByRouter(statement, parameter);
                if (!MapUtils.isEmpty(dsMap)) {
                    SqlSessionCallback callback = new SqlSessionCallback() {
                        @Override
                        public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                            return sqlSession.selectMap(statement,
                                    refactorArgumentWithTableRouting(statement, parameter, dbIndex), mapKey,
                                    defaultRowBoundsChecker(rowBounds));
                        }
                    };
                    List<Object> originalResults = executeInConcurrency(callback, dsMap);
                    Map<K, V> resultMap = new HashMap<K, V>();
                    for (Object item : originalResults) {
                        resultMap.putAll((Map<K, V>) item);
                    }
                    return resultMap;
                }
            } // end if for partitioning status checking

            return super.selectMap(statement,
                    refactorArgumentWithTableRouting(statement, parameter, CobarConstants.DEFAULT_DATASOURCE_INDEX),
                    mapKey, defaultRowBoundsChecker(rowBounds));
        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn(
                            "SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                            new Object[] { statement, parameter, interval });
                }
            }
        }
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return this.selectList(statement, null, null);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return this.selectList(statement, parameter, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> List<E> selectList(final String statement, final Object parameter, final RowBounds rowBounds) {
        long startTimestamp = System.currentTimeMillis();
        try {
            if (isPartitioningBehaviorEnabled()) {
                SortedMap<String, DataSource> dsMap = lookupDataSourcesByRouter(statement, parameter);
                if (!MapUtils.isEmpty(dsMap)) {
                    SqlSessionCallback callback = new SqlSessionCallback() {
                        @Override
                        public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                            return sqlSession.selectList(statement,
                                    refactorArgumentWithTableRouting(statement, parameter, dbIndex),
                                    defaultRowBoundsChecker(rowBounds));
                        }
                    };
                    List<Object> originalResultList = executeInConcurrency(callback, dsMap);
                    if (MapUtils.isNotEmpty(getMergers()) && getMergers().containsKey(statement)) {
                        IMerger<Object, Object> merger = getMergers().get(statement);
                        if (merger != null) {
                            return (List<E>) merger.merge(originalResultList);
                        }
                    }

                    List<E> resultList = new ArrayList<E>();
                    for (Object item : originalResultList) {
                        resultList.addAll((List<E>) item);
                    }
                    return (List<E>) resultList;
                }
            } // end if for partitioning status checking
            return super.selectList(statement,
                    refactorArgumentWithTableRouting(statement, parameter, CobarConstants.DEFAULT_DATASOURCE_INDEX),
                    defaultRowBoundsChecker(rowBounds));
        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn(
                            "SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                            new Object[] { statement, parameter, interval });
                }
            }
        }

    }

    private RowBounds defaultRowBoundsChecker(RowBounds rowBounds) {
        return rowBounds == null ? RowBounds.DEFAULT : rowBounds;
    }

    @Override
    public void select(String statement, ResultHandler handler) {
        this.select(statement, null, null, handler);
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {
        this.select(statement, parameter, null, handler);
    }

    @Override
    public void select(final String statement, final Object parameter, final RowBounds rowBounds,
            final ResultHandler handler) {
        long startTimestamp = System.currentTimeMillis();
        try {
            if (isPartitioningBehaviorEnabled()) {
                SortedMap<String, DataSource> dsMap = lookupDataSourcesByRouter(statement, parameter);
                if (!MapUtils.isEmpty(dsMap)) {
                    SqlSessionCallback callback = new SqlSessionCallback() {
                        @Override
                        public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                            sqlSession.select(statement,
                                    refactorArgumentWithTableRouting(statement, parameter, dbIndex),
                                    defaultRowBoundsChecker(rowBounds), handler);
                            return null;
                        }
                    };
                    executeInConcurrency(callback, dsMap);
                    return;
                }
            } // end if for partitioning status checking
            super.select(statement,
                    refactorArgumentWithTableRouting(statement, parameter, CobarConstants.DEFAULT_DATASOURCE_INDEX),
                    defaultRowBoundsChecker(rowBounds), handler);
            return;
        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn(
                            "SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                            new Object[] { statement, parameter, interval });
                }
            }
        }
    }

    @Override
    public int insert(String statement) {
        return this.insert(statement, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int insert(final String statement, final Object parameter) {
        long startTimestamp = System.currentTimeMillis();
        try {
            /**
             * sometimes, client will submit batch insert request like
             * "insert into ..values(), (), ()...", it's a rare situation, but
             * does exist, so we will create new executor on this kind of
             * request processing, and map each values to their target data
             * source and then reduce to sub-collection, finally, submit each
             * sub-collection of entities to executor to execute.
             */
            if (parameter != null && parameter instanceof BatchInsertTask) {

                // map collection into mapping of data source and sub
                // collection of entities
                logger.info("start to prepare batch insert operation with parameter type of:{}.", parameter.getClass());
                List<Object> listResult = (List<Object>) batchOperationAfterReordering(statement, parameter);
                return CollectionUtils.sumCollectionElement(listResult);
            }
            if (isPartitioningBehaviorEnabled()) {

                DataSource targetDataSource = null;
                SqlSessionCallback action = new SqlSessionCallback() {
                    @Override
                    public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                        return sqlSession.insert(statement,
                                refactorArgumentWithTableRouting(statement, parameter, dbIndex));
                    }
                };
                SortedMap<String, DataSource> resultDataSources = lookupDataSourcesByRouter(statement, parameter);
                if (MapUtils.isEmpty(resultDataSources) || resultDataSources.size() == 1) {
                    targetDataSource = getSqlSessionFactory().getConfiguration().getEnvironment().getDataSource(); // fall
                    // back to default datasource
                    if (resultDataSources.size() == 1) {
                        targetDataSource = resultDataSources.values().iterator().next();
                    }
                    return (Integer) executeWith(targetDataSource, action);
                } else {
                    List<Object> listResult = (List<Object>) executeInConcurrency(action, resultDataSources);
                    return CollectionUtils.sumCollectionElement(listResult);
                }

            }
            return super.insert(statement,
                    refactorArgumentWithTableRouting(statement, parameter, CobarConstants.DEFAULT_DATASOURCE_INDEX));
        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn(
                            "SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                            new Object[] { statement, parameter, interval });
                }
            }
        }
    }

    private CobarMRBase<TableRouterResult, Object> prepareShardRouting(final String statement, final Object parameter) {
        Set<String> keys = new HashSet<String>();
        keys.add(getDefaultDataSourceName());
        if (isPartitioningBehaviorEnabled()) {
            keys.addAll(getCobarDataSourceService().getDataSources().keySet());
        }

        final CobarMRBase<TableRouterResult, Object> mrbase = new CobarMRBase<TableRouterResult, Object>(keys);
        ExecutorService executor = createCustomExecutorService(Runtime.getRuntime().availableProcessors(),
                "prepareShardRouting");
        try {
            final StringBuffer exceptionStaktrace = new StringBuffer();

            Collection<?> paramCollection = ((BatchTask) parameter).getList();

            final CountDownLatch latch = new CountDownLatch(paramCollection.size());

            Iterator<?> iter = paramCollection.iterator();
            while (iter.hasNext()) {
                final Object entity = iter.next();
                Runnable task = new Runnable() {
                    public void run() {
                        try {
                            SortedMap<String, DataSource> dsMap = lookupDataSourcesByRouter(statement, entity);
                            String identity = getDefaultDataSourceName();
                            if (MapUtils.isEmpty(dsMap)) {
                                logger.info(
                                        "can't find routing rule for {} with parameter {}, so use default data source for it.",
                                        statement, entity);
                                mrbase.emit(identity, entity);
                            } else {
                                if (dsMap.size() > 1) {
                                    throw new IllegalArgumentException(
                                            "unexpected routing result, found more than 1 target data source for current entity:"
                                                    + entity);
                                }
                                identity = dsMap.firstKey();
                                mrbase.emit(identity, entity);
                            }
                            final Object refactorParameterObject = BeanReflectUtils.refactorArgument(statement, entity,
                                    findDataSourceIndex(findDataSourceToUse(identity)));
                            TableRouterResult routerResult = lookupTableNameByRouter(statement, refactorParameterObject);
                            if (routerResult != null) {
                                mrbase.emit(identity, routerResult, entity);
                            }
                        } catch (Throwable t) {
                            exceptionStaktrace.append(ExceptionUtils.getStackTrace(t));
                        } finally {
                            latch.countDown();
                        }
                    }
                };
                executor.execute(task);
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new ConcurrencyFailureException(
                        "unexpected interruption when re-arranging parameter collection into sub-collections ", e);
            }

            if (exceptionStaktrace.length() > 0) {
                throw new ConcurrencyFailureException(
                        "unpected exception when re-arranging parameter collection, check previous log for details.\n"
                                + exceptionStaktrace);
            }
        } finally {
            executor.shutdown();
        }
        return mrbase;
    }

    /**
     * we reorder the collection of entities in concurrency and commit them in
     * sequence, because we have to conform to the infrastructure of spring's
     * transaction management layer.
     * 
     * @param statementName
     * @param parameterObject
     * @return
     */
    private Object batchOperationAfterReordering(final String statement, final Object parameter) {
        CobarMRBase<TableRouterResult, Object> mrbase = prepareShardRouting(statement, parameter);
        List<ConcurrentRequest> requests = new ArrayList<ConcurrentRequest>();
        for (final Map.Entry<String, List<Object>> entity : mrbase.getResources().entrySet()) {
            final List<Object> paramList = entity.getValue();
            if (CollectionUtils.isEmpty(paramList)) {
                continue;
            }
            if (!mrbase.getGroupResource().isEmpty()) {
                for (final Map.Entry<TableRouterResult, List<Object>> entry : mrbase.getGroupResource()
                        .get(entity.getKey()).entrySet()) {
                    requests.add(generateConcurrentRequest(entity.getKey(), statement, entry.getValue(),
                            entry.getKey(), parameter));
                }
            } else {
                requests.add(generateConcurrentRequest(entity.getKey(), statement, entity.getValue(), null, parameter));
            }
        }
        return getConcurrentRequestProcessor().process(requests);
    }

    private ConcurrentRequest generateConcurrentRequest(String identity, final String statement,
            final Object parameter, final TableRouterResult routerResult, Object parameterRaw) {
        final DataSource dataSourceToUse = findDataSourceToUse(identity);
        SqlSessionCallback callback = null;
        if (routerResult != null) {
            if (parameterRaw instanceof BatchInsertTask) {
                callback = new SqlSessionCallback() {
                    @Override
                    public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                        return sqlSession.insert(statement,
                                refactorArgumentWithBatchTableRouting(statement, parameter, dbIndex, routerResult));
                    }
                };
            }
            if (parameterRaw instanceof BatchUpdateTask) {
                callback = new SqlSessionCallback() {
                    @Override
                    public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                        return sqlSession.update(statement,
                                refactorArgumentWithBatchTableRouting(statement, parameter, dbIndex, routerResult));
                    }
                };
            }
            if (parameterRaw instanceof BatchDeleteTask) {
                callback = new SqlSessionCallback() {
                    @Override
                    public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                        return sqlSession.delete(statement,
                                refactorArgumentWithBatchTableRouting(statement, parameter, dbIndex, routerResult));
                    }
                };
            }
        } else {
            if (parameterRaw instanceof BatchInsertTask) {
                callback = new SqlSessionCallback() {
                    @Override
                    public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                        return sqlSession.insert(statement,
                                refactorArgumentWithTableRouting(statement, parameter, dbIndex));
                    }
                };
            }
            if (parameterRaw instanceof BatchUpdateTask) {
                callback = new SqlSessionCallback() {
                    @Override
                    public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                        return sqlSession.update(statement,
                                refactorArgumentWithTableRouting(statement, parameter, dbIndex));
                    }
                };
            }
            if (parameterRaw instanceof BatchDeleteTask) {
                callback = new SqlSessionCallback() {
                    @Override
                    public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                        return sqlSession.delete(statement,
                                refactorArgumentWithTableRouting(statement, parameter, dbIndex));
                    }
                };
            }
        }
        Validate.notNull(callback);
        ConcurrentRequest request = new ConcurrentRequest();
        request.setDataSource(dataSourceToUse);
        request.setDataSourceIndex(findDataSourceIndex(dataSourceToUse));
        request.setAction(callback);
        request.setExecutor(getDataSourceSpecificExecutors().get(identity));
        return request;
    }

    private int findDataSourceIndex(DataSource dataSource) {
        if (isPartitioningBehaviorEnabled()) {
            return this.getCobarDataSourceService().getDataSourceIndex(dataSource);
        }
        return CobarConstants.DEFAULT_DATASOURCE_INDEX;
    }

    private DataSource findDataSourceToUse(String key) {
        DataSource dataSourceToUse = null;
        if (StringUtils.equals(key, getDefaultDataSourceName())) {
            dataSourceToUse = getSqlSessionFactory().getConfiguration().getEnvironment().getDataSource();
        } else {
            dataSourceToUse = getCobarDataSourceService().getDataSources().get(key);
        }
        return dataSourceToUse;
    }

    @Override
    public int update(String statement) {
        return this.update(statement, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int update(final String statement, final Object parameter) {
        long startTimestamp = System.currentTimeMillis();
        try {
            if (parameter != null && parameter instanceof BatchUpdateTask) {
                // map collection into mapping of data source and sub
                // collection of entities
                logger.info("start to prepare batch update operation with parameter type of:{}.", parameter.getClass());
                List<Object> listResult = (List<Object>) batchOperationAfterReordering(statement, parameter);
                return CollectionUtils.sumCollectionElement(listResult);
            }

            if (isPartitioningBehaviorEnabled()) {

                SortedMap<String, DataSource> dsMap = lookupDataSourcesByRouter(statement, parameter);
                if (!MapUtils.isEmpty(dsMap)) {
                    SqlSessionCallback action = new SqlSessionCallback() {
                        public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                            return sqlSession.update(statement,
                                    refactorArgumentWithTableRouting(statement, parameter, dbIndex));
                        }
                    };

                    List<Object> results = executeInConcurrency(action, dsMap);
                    return CollectionUtils.sumCollectionElement(results);
                }
            } // end if for partitioning status checking
            return super.update(statement,
                    refactorArgumentWithTableRouting(statement, parameter, CobarConstants.DEFAULT_DATASOURCE_INDEX));
        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn(
                            "SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                            new Object[] { statement, parameter, interval });
                }
            }
        }
    }

    /**
     * if a router and a data source locator is provided, it means data access
     * on different databases is enabled.<br>
     */
    protected boolean isPartitioningBehaviorEnabled() {
        return ((router != null) && (getCobarDataSourceService() != null));
    }

    protected boolean isTableRoutingBehaviorEnabled() {
        return tableRouter != null;
    }

    @Override
    public int delete(String statement) {
        return this.delete(statement, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int delete(final String statement, final Object parameter) {
        long startTimestamp = System.currentTimeMillis();
        try {
            if (parameter != null && parameter instanceof BatchDeleteTask) {
                // map collection into mapping of data source and sub
                // collection of entities
                logger.info("start to prepare batch update operation with parameter type of:{}.", parameter.getClass());
                List<Object> listResult = (List<Object>) batchOperationAfterReordering(statement, parameter);
                return CollectionUtils.sumCollectionElement(listResult);
            }
            if (isPartitioningBehaviorEnabled()) {

                SortedMap<String, DataSource> dsMap = lookupDataSourcesByRouter(statement, parameter);
                if (!MapUtils.isEmpty(dsMap)) {

                    SqlSessionCallback action = new SqlSessionCallback() {
                        @Override
                        public Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException {
                            return sqlSession.delete(statement,
                                    refactorArgumentWithTableRouting(statement, parameter, dbIndex));
                        }
                    };
                    if (dsMap.size() == 1) {
                        DataSource dataSource = dsMap.get(dsMap.firstKey());
                        return (Integer) executeWith(dataSource, action);
                    } else {
                        List<Object> results = executeInConcurrency(action, dsMap);
                        return CollectionUtils.sumCollectionElement(results);
                    }
                }
            } // end if for partitioning status checking
            return super.delete(statement,
                    refactorArgumentWithTableRouting(statement, parameter, CobarConstants.DEFAULT_DATASOURCE_INDEX));
        } finally {
            if (isProfileLongTimeRunningSql()) {
                long interval = System.currentTimeMillis() - startTimestamp;
                if (interval > getLongTimeRunningSqlIntervalThreshold()) {
                    logger.warn(
                            "SQL Statement [{}] with parameter object [{}] ran out of the normal time range, it consumed [{}] milliseconds.",
                            new Object[] { statement, parameter, interval });
                }
            }
        }
    }

    public List<Object> executeInConcurrency(SqlSessionCallback action, SortedMap<String, DataSource> dsMap) {
        List<ConcurrentRequest> requests = new ArrayList<ConcurrentRequest>();
        for (Map.Entry<String, DataSource> entry : dsMap.entrySet()) {
            ConcurrentRequest request = new ConcurrentRequest();
            request.setAction(action);
            request.setDataSource(entry.getValue());
            request.setExecutor(getDataSourceSpecificExecutors().get(entry.getKey()));
            request.setDataSourceIndex(getCobarDataSourceService().getDataSourceIndex(request.getDataSource()));
            requests.add(request);
        }
        return getConcurrentRequestProcessor().process(requests);
    }

    protected Object executeWith(DataSource dataSource, SqlSessionCallback action) {
        SqlSession sqlSession = null;
        boolean isRealRequireClosedConnection = true;
        try {
            Connection springCon = null;
            boolean transactionAware = (dataSource instanceof TransactionAwareDataSourceProxy);

            // Obtain JDBC Connection to operate on...
            try {
                springCon = (transactionAware ? dataSource.getConnection() : DataSourceUtils
                        .doGetConnection(dataSource));
                sqlSession = getSqlSessionFactory().openSession(getExecutorType(), springCon);
            } catch (SQLException ex) {
                throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", ex);
            }

            try {
                Object result = action.doInSqlSession(sqlSession,
                        getCobarDataSourceService().getDataSourceIndex(dataSource));
                return result;
            } catch (SQLException ex) {
                throw new SQLErrorCodeSQLExceptionTranslator().translate("SqlSession operation", null, ex);
            } catch (Throwable t) {
                throw new UncategorizedCobarClientException("unknown excepton when performing data access operation.",
                        t);
            } finally {
                try {
                    if (springCon != null) {
                        if (transactionAware) {
                            springCon.close();
                        } else {
                            DataSourceUtils.doReleaseConnection(springCon, dataSource);
                        }
                        isRealRequireClosedConnection = springCon.isClosed();
                    }
                } catch (Throwable ex) {
                    logger.debug("Could not close JDBC Connection", ex);
                }
            }
            // Processing finished - potentially session still to be closed.
        } finally {
            if (isRealRequireClosedConnection && sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return super.getMapper(type);
    }

    /**
     * 无用的方法、使之失效<br>
     * commit() rollback() close()
     */
    @Override
    public void commit() {
    }

    @Override
    public void commit(boolean force) {
    }

    @Override
    public void rollback() {
    }

    @Override
    public void rollback(boolean force) {
    }

    @Override
    public void close() {
    }

    @Override
    public void clearCache() {
        if (isPartitioningBehaviorEnabled()) {
            throw new UnsupportedOperationException();
        }
        super.clearCache();
    }

    @Override
    public Configuration getConfiguration() {
        // if (isPartitioningBehaviorEnabled()) {
        // throw new UnsupportedOperationException();
        // }
        return super.getConfiguration();
    }

    @Override
    public Connection getConnection() {
        if (isPartitioningBehaviorEnabled()) {
            throw new UnsupportedOperationException();
        }
        return super.getConnection();
    }

    @Override
    public List<BatchResult> flushStatements() {
        if (isPartitioningBehaviorEnabled()) {
            throw new UnsupportedOperationException();
        }
        return super.flushStatements();
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.selectOne(statement, null);
    }

    protected SortedMap<String, DataSource> lookupDataSourcesByRouter(final String statementName, Object parameterObject) {
        final Object refactorParameterObject = BeanReflectUtils.refactorArgument(statementName, parameterObject,
                CobarConstants.DEFAULT_DATASOURCE_INDEX);

        SortedMap<String, DataSource> resultMap = new TreeMap<String, DataSource>();
        if (this.router != null && this.cobarDataSourceService != null) {
            List<String> dsSet = this.router.doRoute(new IBatisRoutingFact(statementName, refactorParameterObject))
                    .getResourceIdentities();
            if (CollectionUtils.isNotEmpty(dsSet)) {
                Collections.sort(dsSet);
                for (String dsName : dsSet) {
                    resultMap.put(dsName, this.cobarDataSourceService.getDataSources().get(dsName));
                }
            }
        }
        return resultMap;
    }

    protected TableRouterResult lookupTableNameByRouter(String statement, Object argument) {
        if (isTableRoutingBehaviorEnabled()) {
            return tableRouter.doRoute(new TableRoutingFact(statement, argument));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Object refactorArgumentWithTableRouting(String statement, Object argument, int dbIndex) {
        if (isTableRoutingBehaviorEnabled()) {
            Object newArg = BeanReflectUtils.refactorArgument(statement, argument, dbIndex);
            if (newArg instanceof Map<?, ?>) {
                Map<Object, Object> newMap = (Map<Object, Object>) newArg;
                TableRouterResult routerResult = lookupTableNameByRouter(statement, newMap);
                if (routerResult != null) {
                    newMap.put(routerResult.getMainTable(), routerResult.getSubTable());
                    return newMap;
                }
            }
        }
        return argument;
    }

    @SuppressWarnings("unchecked")
    private Object refactorArgumentWithBatchTableRouting(String statement, Object argument, int dbIndex,
            TableRouterResult routerResult) {
        if (isTableRoutingBehaviorEnabled()) {
            Object newArg = BeanReflectUtils.refactorArgument(statement, argument, dbIndex);
            if (newArg instanceof Map<?, ?>) {
                Map<Object, Object> newMap = (Map<Object, Object>) newArg;
                if (routerResult != null) {
                    newMap.put(routerResult.getMainTable(), routerResult.getSubTable());
                    return newMap;
                }
            }
        }
        return argument;
    }

    private ExecutorService createCustomExecutorService(int poolSize, final String method) {
        int coreSize = Runtime.getRuntime().availableProcessors();
        if (poolSize < coreSize) {
            coreSize = poolSize;
        }
        ThreadFactory tf = new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "thread created at CobarSqlSessionTemplate method [" + method + "]");
                t.setDaemon(true);
                return t;
            }
        };
        BlockingQueue<Runnable> queueToUse = new LinkedBlockingQueue<Runnable>(coreSize);
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize, poolSize, 60, TimeUnit.SECONDS,
                queueToUse, tf, new ThreadPoolExecutor.CallerRunsPolicy());

        return executor;
    }

    public ICobarDataSourceService getCobarDataSourceService() {
        return cobarDataSourceService;
    }

    public ICobarRouter<RoutingResult, IBatisRoutingFact> getRouter() {
        return router;
    }

    public IConcurrentRequestProcessor getConcurrentRequestProcessor() {
        return concurrentRequestProcessor;
    }

    public Map<String, ExecutorService> getDataSourceSpecificExecutors() {
        return dataSourceSpecificExecutors;
    }

    public Map<String, IMerger<Object, Object>> getMergers() {
        return mergers;
    }

    public boolean isProfileLongTimeRunningSql() {
        return profileLongTimeRunningSql;
    }

    public long getLongTimeRunningSqlIntervalThreshold() {
        return longTimeRunningSqlIntervalThreshold;
    }

    public String getDefaultDataSourceName() {
        return defaultDataSourceName;
    }

    public void setCobarDataSourceService(ICobarDataSourceService cobarDataSourceService) {
        this.cobarDataSourceService = cobarDataSourceService;
    }

    public void setRouter(ICobarRouter<RoutingResult, IBatisRoutingFact> router) {
        this.router = router;
    }

    public void setConcurrentRequestProcessor(IConcurrentRequestProcessor concurrentRequestProcessor) {
        this.concurrentRequestProcessor = concurrentRequestProcessor;
    }

    public void setMergers(Map<String, IMerger<Object, Object>> mergers) {
        this.mergers = mergers;
    }

    public ICobarRouter<TableRouterResult, TableRoutingFact> getTableRouter() {
        return tableRouter;
    }

    public void setTableRouter(ICobarRouter<TableRouterResult, TableRoutingFact> tableRouter) {
        this.tableRouter = tableRouter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isProfileLongTimeRunningSql()) {
            if (longTimeRunningSqlIntervalThreshold <= 0) {
                throw new IllegalArgumentException(
                        "'longTimeRunningSqlIntervalThreshold' should have a positive value if 'profileLongTimeRunningSql' is set to true");
            }
        }
        setupDefaultExecutorServicesIfNecessary();
        // setUpDefaultSqlAuditorExecutorIfNecessary();
        if (getConcurrentRequestProcessor() == null) {
            setConcurrentRequestProcessor(new DefaultConcurrentRequestProcessor(getSqlSessionFactory(),
                    getExecutorType()));
        }
    }

    private void setupDefaultExecutorServicesIfNecessary() {
        if (isPartitioningBehaviorEnabled()) {
            if (MapUtils.isEmpty(getDataSourceSpecificExecutors())) {

                Set<CobarDataSourceDescriptor> dataSourceDescriptors = getCobarDataSourceService()
                        .getDataSourceDescriptors();
                for (CobarDataSourceDescriptor descriptor : dataSourceDescriptors) {
                    ExecutorService executor = createExecutorForSpecificDataSource(descriptor);
                    getDataSourceSpecificExecutors().put(descriptor.getIdentity(), executor);
                }
            }
        }
        addDefaultSingleThreadExecutorIfNecessary();
    }

    private void addDefaultSingleThreadExecutorIfNecessary() {
        String identity = getDefaultDataSourceName();
        CobarDataSourceDescriptor descriptor = new CobarDataSourceDescriptor();
        descriptor.setIdentity(identity);
        descriptor.setPoolSize(Runtime.getRuntime().availableProcessors() * 5);
        getDataSourceSpecificExecutors().put(identity, createExecutorForSpecificDataSource(descriptor));
    }

    private ExecutorService createExecutorForSpecificDataSource(CobarDataSourceDescriptor descriptor) {
        final String identity = descriptor.getIdentity();
        final ExecutorService executor = createCustomExecutorService(descriptor.getPoolSize(),
                "createExecutorForSpecificDataSource-" + identity + " data source");
        // 1. register executor for disposing explicitly
        internalExecutorServiceRegistry.add(executor);
        // 2. dispose executor implicitly
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (executor == null) {
                    return;
                }

                try {
                    executor.shutdown();
                    executor.awaitTermination(5, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    logger.warn("interrupted when shuting down the query executor:\n{}", e);
                }
            }
        });
        return executor;
    }

    @Override
    public void destroy() throws Exception {
        logger.info("destroy instance");
    }
}
