package com.exam.service;

import com.exam.vo.AnswerVo;
import com.exam.vo.ResultVo;

import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
public interface IAnswerService {

    int addAnswer(AnswerVo answerVo) throws Exception;

    List<ResultVo> getResult(Integer userId);
}
