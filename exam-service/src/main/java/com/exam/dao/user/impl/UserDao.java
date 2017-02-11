package com.exam.dao.user.impl;

import com.exam.dao.BaseDao;
import com.exam.dao.user.IUserDao;
import com.exam.vo.user.UserVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
@Repository
public class UserDao extends BaseDao<UserVo> implements IUserDao {

    @Override
    public UserVo queryByPhone(String phone) {
        return super.queryOne("queryByPhone", phone);
    }

    @Override
    protected String interfaceSimpleName() {
        return IUserDao.class.getSimpleName();
    }

    @Override
    public int updateInfoByPhone(UserVo userVo) {
        return super.update("updateInfoByPhone", userVo);
    }

    @Override
    public List<UserVo> queryUsers() {
        return super.queryList("queryUser");
    }

    @Override
    public void saveUser(UserVo userVo) {
        super.add("saveUser", userVo);
    }
}
