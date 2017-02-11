package com.exam.dao.user;

import com.exam.vo.user.UserVo;

import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
public interface IUserDao {

    UserVo queryByPhone(String phone);

    int updateInfoByPhone(UserVo userVo);

    List<UserVo> queryUsers();

    void saveUser(UserVo userVo);
}
