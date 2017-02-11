package com.exam.dao.user.impl;

import com.exam.dao.BaseDao;
import com.exam.dao.user.IOptionDao;
import com.exam.vo.option.OptionVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
@Repository
public class OptionDao extends BaseDao<OptionVo> implements IOptionDao {

    @Override
    public List<OptionVo> queryAll() {
        return super.queryList("queryAll");
    }

    @Override
    public List<OptionVo> queryById(String optionId) {
        return super.queryList("queryListById", optionId);
    }

    @Override
    protected String interfaceSimpleName() {
        return IOptionDao.class.getSimpleName();
    }

}
