package com.exam.service;

import com.exam.vo.title.TitleVo;

import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
public interface ITitleService {

    List<TitleVo> query(Integer genderType);

}
