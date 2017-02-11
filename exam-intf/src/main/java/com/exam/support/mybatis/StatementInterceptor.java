package com.exam.support.mybatis;

import com.exam.support.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

/**
 * 开发模式下可以打印sql
 * @author liuchao2
 *
 */
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class StatementInterceptor  implements Interceptor{

	/** 日志接口类 */
	private static Logger logger = LoggerFactory.getLogger(StatementInterceptor.class);

	/** 应用运行模式 ，只有在开发或测试环境下才可以打印sql */
	@Value(value = "@{envName}")
	public String runMod = "prd";
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (null ==runMod || StringUtils.isBlank(runMod) || "prd".equalsIgnoreCase(runMod))
		{
			//logger.info("runMod= " + runMod);
		}
		else
		{
		    StatementHandler target = (StatementHandler)invocation.getTarget();
		    BoundSql bs = target.getBoundSql();
		    
		    String sql = bs.getSql();
		    sql = StringUtils.replaceChars(sql, " ", "@");
		    sql = StringUtils.deleteWhitespace(sql);
		    sql = StringUtils.replaceChars(sql, "@", " ");
		    sql = sql.replaceAll(" +", " ");
		    System.out.println("target=" + sql);
		    System.out.println("args=" + JsonUtil.toJsonAsString(bs.getParameterObject(), false));
		    
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
