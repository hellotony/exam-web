package com.exam.support.mybatis;

import com.exam.support.util.page.Pagination;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.util.Properties;

/**
 * mybatis 自动分页和统计的拦截器<br>
 * 通过Pagination组件来控制
 *
 */
@Component
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class PaginationInterceptor implements Interceptor{

    private static Logger logger = LoggerFactory.getLogger(PaginationInterceptor.class);
       
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();  
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();  
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
    	StatementHandler  statementHandler = (StatementHandler)invocation.getTarget();
    	MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
    	// 分离代理对象链  
    	while (metaStatementHandler.hasGetter("h")) {  
    		Object object = metaStatementHandler.getValue("h");  
    		metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
    	}  
    	// 分离最后一个代理对象的目标类  
    	while (metaStatementHandler.hasGetter("target")) {  
    		Object object = metaStatementHandler.getValue("target");  
    		metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
    	}  

        StatementHandler delegate = null;
        try {
        	delegate = (StatementHandler) metaStatementHandler.getValue("delegate");
        } catch (Exception e) {
            return invocation.proceed();
        } 
        //获取分页属性
        RowBounds rowBounds = (RowBounds) ReflectUtils.getValueByFieldName(delegate, "rowBounds");
       
        if (null != rowBounds && rowBounds instanceof Pagination)
        {
            String sql = delegate.getBoundSql().getSql();
            Pagination page = (Pagination) rowBounds;
            int start = (page.getCurrentPage()-1)*(page.getPageSize());
            String limitGramar =sql + " limit " +  start + " , " + page.getPageSize();
            if (sql.indexOf("limit") < 0) {
                ReflectUtils.setValueByFieldName(delegate.getBoundSql(), "sql",limitGramar);
            } else {
                logger.warn("sql statement has already contained 'limit' grammar!");
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        
    }
}
