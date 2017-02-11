package com.exam.dao.user;

import com.exam.vo.option.OptionVo;

import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
public interface IOptionDao {
    List<OptionVo> queryAll();

    List<OptionVo> queryById(String optionId);
}
