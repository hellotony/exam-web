package com.exam.dao.user.impl;

import com.exam.dao.BaseDao;
import com.exam.dao.user.IAnswerDao;
import com.exam.dao.user.IOptionDao;
import com.exam.vo.AnswerVo;
import com.exam.vo.ResultVo;
import com.exam.vo.option.OptionVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
@Repository
public class AnswerDao extends BaseDao<AnswerVo> implements IAnswerDao {

    @Override
    public int addAnswer(AnswerVo answerVo) {
        return super.add("add", answerVo);
    }

    @Override
    public List<ResultVo> getResult(Integer userId) {
         return super.queryList("getResult", userId);
    }

    @Override
    public List<AnswerVo> queryAnswerById(int userId) {
        return super.queryList("queryAll", userId);
    }

    @Override
    protected String interfaceSimpleName() {
        return IAnswerDao.class.getSimpleName();
    }

}
