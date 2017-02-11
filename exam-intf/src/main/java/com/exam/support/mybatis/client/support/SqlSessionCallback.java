package com.exam.support.mybatis.client.support;

import org.apache.ibatis.session.SqlSession;

import java.sql.SQLException;

/**
 * 回调处理，封装sqlSession的调用过程
 * 
 * @author wangchong
 * 
 */
public interface SqlSessionCallback {
	/**
	 * 执行回调
	 * 
	 * @param sqlSession
	 * @param dbIndex
	 *            自动创建的数据源的库号(ps：这种处理不太友好，后期会优化掉)
	 * @return
	 * @throws SQLException
	 */
	Object doInSqlSession(SqlSession sqlSession, int dbIndex) throws SQLException;
}
