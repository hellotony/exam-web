package com.exam.service;

import com.exam.dao.user.IOptionDao;
import com.exam.vo.option.OptionVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
@Service
public class OptionService implements IOptionService {

    @Resource
    IOptionDao optionDao;

    @Override
    public List<OptionVo> queryAll() {
        return optionDao.queryAll();
    }
}
