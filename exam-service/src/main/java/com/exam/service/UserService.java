package com.exam.service;

import com.exam.dao.user.IUserDao;
import com.exam.vo.user.UserVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
@Service
public class UserService implements IUserService {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    IUserDao userDao;

    @Override
    public UserVo queryByPhone(String phone) {
        return userDao.queryByPhone(phone);
    }

    @Override
    public int updateInfoByPhone(UserVo userVo) {
        return userDao.updateInfoByPhone(userVo);
    }

    @Override
    public void saveUser(String phoneNumber) {
        UserVo userVo = new UserVo();
        userVo.setPhone(phoneNumber);
        userDao.saveUser(userVo);
    }

    @Override
    public List<UserVo> queryUsers() {
        return userDao.queryUsers();
    }
}
