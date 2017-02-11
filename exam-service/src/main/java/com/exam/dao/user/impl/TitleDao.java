package com.exam.dao.user.impl;

import com.exam.dao.BaseDao;
import com.exam.dao.user.ITitleDao;
import com.exam.dao.user.IUserDao;
import com.exam.vo.title.TitleVo;
import com.exam.vo.user.UserVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
@Repository
public class TitleDao extends BaseDao<TitleVo> implements ITitleDao {

    @Override
    public List<TitleVo> queryByGender(Integer gender) {
        return super.queryList("queryByGender", gender);
    }

    @Override
    public TitleVo queryById(Integer userId) {
        return super.queryOne("queryById", userId);
    }

    @Override
    protected String interfaceSimpleName() {
        return ITitleDao.class.getSimpleName();
    }
}
