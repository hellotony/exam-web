package com.exam.service;

import com.exam.dao.user.IAnswerDao;
import com.exam.dao.user.IOptionDao;
import com.exam.dao.user.ITitleDao;
import com.exam.vo.AnswerVo;
import com.exam.vo.ResultVo;
import com.exam.vo.option.OptionVo;
import com.exam.vo.title.TitleVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijun on 2017/2/6.
 */
@Service
public class AnswerService implements IAnswerService {

    @Resource
    IAnswerDao answerDao;

    @Resource
    ITitleDao titleDao;

    @Resource
    IOptionDao optionDao;

    @Override
    public int addAnswer(AnswerVo answerVo) throws Exception {
        return answerDao.addAnswer(answerVo);
    }

    @Override
    public List<ResultVo> getResult(Integer userId) {
        List<ResultVo> result = answerDao.getResult(userId);
        for (ResultVo r:result) {
            List<String> options = new ArrayList<>();
            options.add(r.getOption());
            r.setOptions(options);
        }
        List<AnswerVo> answers = answerDao.queryAnswerById(userId);
        for(AnswerVo a:answers) {
            TitleVo title = titleDao.queryById(a.getTitleId());
            ResultVo r = new ResultVo();
            r.setTitleId(title.getAbstractNo());
            r.setTitle(title.getContent());
            List<OptionVo> o = optionDao.queryById(a.getOptionId());
            List<String> optionContents = new ArrayList<>();
            for (OptionVo op:o) {
                optionContents.add(op.getContent());
            }
            r.setOptions(optionContents);
            result.add(r);
        }
        return result;
    }

}
