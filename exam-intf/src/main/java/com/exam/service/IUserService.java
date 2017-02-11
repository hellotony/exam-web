package com.exam.service;

import com.exam.vo.user.UserVo;

import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
public interface IUserService {

    UserVo queryByPhone(String phone);

    int updateInfoByPhone(UserVo userVo);

    void saveUser(String phoneNumber);

    List<UserVo> queryUsers();
}
