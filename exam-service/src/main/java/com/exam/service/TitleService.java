package com.exam.service;

import com.exam.dao.user.ITitleDao;
import com.exam.vo.title.TitleVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
@Service
public class TitleService implements ITitleService {

    @Resource
    ITitleDao titleDao;

    @Override
    public List<TitleVo> query(Integer genderType) {
        return titleDao.queryByGender(genderType);
    }
}
