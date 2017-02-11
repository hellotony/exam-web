package com.exam.dao;

import com.exam.support.util.page.Pagination;
import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * 读写分离后可以直接修改此处query的sqlSessionTemplate
 * 考虑到事务的问题，读写分离还是建议放到service层控制，以后有时间再优化
 */
public abstract class BaseDao<T> {

    protected abstract String interfaceSimpleName();
    /**
     * mybatis mapper配置文件命名空间前缀
     */
    protected static final String NAMESPACE_PREFIX = "exam.";

    @Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sst;

    /**
     * 查询列表
     * @param statement mapper语句
     * @return
     */
    protected <E> List<E> queryList(String statement) {
        return sst.selectList(NAMESPACE_PREFIX.concat(interfaceSimpleName()).concat(".").concat(statement));
    }

    /**
     * 查询列表
     * @param statement mapper语句
     * @param params    参数
     * @return
     */
    protected <E> List<E> queryList(String statement, Object params) {
        return sst.selectList(NAMESPACE_PREFIX.concat(interfaceSimpleName()).concat(".").concat(statement), params);
    }
    /**
     * 分页查询列表
     * @param statement mapper语句
     * @param params    参数
     * @param pagination
     * @return
     */
    protected <E> List<E> queryList(String statement, Object params, Pagination pagination) {
        if (pagination == null)
            pagination = new Pagination();
        return sst.selectList(NAMESPACE_PREFIX.concat(interfaceSimpleName()).concat(".").concat(statement), params, pagination);
    }

    /**
     * 查询单个记录
     * @param statement mapper语句
     * @param param     参数
     * @return
     */
    protected <E> E queryOne(String statement, Object param) {
        return sst.selectOne(NAMESPACE_PREFIX.concat(interfaceSimpleName()).concat(".").concat(statement), param);
    }

    /**
     * 查询单个记录
     * @param statement mapper语句
     * @return
     */
    protected <E> E queryOne(String statement) {
        return sst.selectOne(NAMESPACE_PREFIX.concat(interfaceSimpleName()).concat(".").concat(statement));
    }

    /**
     *
     * @param statement
     * @param param
     * @return
     */
    protected int getCount(String statement, Object param) {
        return (Integer)sst.selectOne(NAMESPACE_PREFIX.concat(interfaceSimpleName()).concat(".").concat(statement), param);
    }


    /**
     * 写入记录
     * @param statement mapper语句
     * @param t         记录对象
     * @return
     */
    protected int add(String statement, T t) {
        return sst.insert(NAMESPACE_PREFIX.concat(interfaceSimpleName()).concat(".").concat(statement), t);
    }

    /**
     * 写入记录
     * @param t         记录对象
     * @return
     */
    protected int add(T t) {
       return add("add", t);
    }

    /**
     * 批量写入记录
     * @param statement  mapper语句
     * @param list       记录对象集合
     * @return
     */
    protected int addBatch(String statement, List<T> list) {
        return sst.insert(NAMESPACE_PREFIX.concat(interfaceSimpleName()).concat(".").concat(statement), list);
    }
    /**
     * 批量写入记录
     * @param list       记录对象集合
     * @return
     */
    protected int addBatch(List<T> list) {
        return addBatch("addBatch", list);
    }

    /**
     * 修改操作
     * @param statement mapper语句
     * @param param     参数
     * @return
     */
    protected int update(String statement, Object param) {
        return sst.update(NAMESPACE_PREFIX.concat(interfaceSimpleName()).concat(".").concat(statement), param);
    }

    /**
     * 修改操作
     * @param statement mapper语句
     * @return
     */
    protected int update(String statement) {
        return sst.update(statement);
    }

    /**
     * 删除操作
     * @param statement 语句
     * @param param   参数
     * @return
     */
    protected int delete(String statement, Object param) {
        return sst.delete(NAMESPACE_PREFIX.concat(interfaceSimpleName()).concat(".").concat(statement), param);
    }
}
