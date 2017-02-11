package com.exam.dao.user;

import com.exam.vo.AnswerVo;
import com.exam.vo.ResultVo;

import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
public interface IAnswerDao {

    int addAnswer(AnswerVo answerVo);

    List<ResultVo> getResult(Integer id);

    List<AnswerVo> queryAnswerById(int id);
}
