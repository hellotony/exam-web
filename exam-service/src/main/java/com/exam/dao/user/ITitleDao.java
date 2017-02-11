package com.exam.dao.user;

import com.exam.vo.title.TitleVo;

import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
public interface ITitleDao {
    List<TitleVo> queryByGender(Integer gender);

    TitleVo queryById(Integer titleId);
}
