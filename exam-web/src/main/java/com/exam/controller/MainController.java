package com.exam.controller;

import com.exam.service.IAnswerService;
import com.exam.service.IOptionService;
import com.exam.service.ITitleService;
import com.exam.service.IUserService;
import com.exam.support.util.json.core.annotation.ResponseJson;
import com.exam.support.util.json.core.utils.ResponseVo;
import com.exam.vo.AnswerVo;
import com.exam.vo.ResultVo;
import com.exam.vo.option.OptionVo;
import com.exam.vo.title.TitleVo;
import com.exam.vo.user.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/main")
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Resource
    private IUserService userService;

    @Resource
    private ITitleService titleService;

    @Resource
    private IOptionService optionService;

    @Resource
    private IAnswerService answerService;

    /**
     * 登录成功页面
     * @return
     */
    @RequestMapping(value = "/fillInfo/{phone}", method = RequestMethod.GET)
    public String loginSuccess(@PathVariable("phone") String phone, ModelMap model) {
        model.put("phone", phone);
        return "main/fillInfo";
    }

    /**
     * 后台页面
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin() {
        return "main/index";
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @ResponseJson
    public ResponseVo regist(String phoneNumber) {
        ResponseVo responseVo = new ResponseVo();

        userService.saveUser(phoneNumber);
        responseVo.setSuccess(true);
        responseVo.setMsg("登录成功!");
        responseVo.setData("/main/fillInfo");
        return responseVo;
    }

    /**
     * 获取用户列表
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseJson
    public String users(String phoneNumber,  ModelMap model) {
        List<UserVo> userVos = userService.queryUsers();
        model.put("user", userVos);
        return "main/users";
    }

    @RequestMapping(value = "/answer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVo answer(AnswerVo answerVo) {
        ResponseVo responseVo = new ResponseVo();
        try{
            answerService.addAnswer(answerVo);
        } catch (Exception e) {
            responseVo.setSuccess(false);
            logger.error("insert error: " + e);
        }
        responseVo.setSuccess(true);
        return responseVo;
    }

    @RequestMapping(value = "/answers/{id}", method = RequestMethod.GET)
    public String answers(@PathVariable("id") String id,  ModelMap model) {
        List<ResultVo> answers = answerService.getResult(Integer.valueOf(id));
        model.put("answers", answers);
        return "main/answer";
    }

    /**
     * 开始考试
     * @return
     */
    @RequestMapping(value = "/startExam/{phone}", method = RequestMethod.GET)
    public String startExam(@PathVariable("phone") String phone, ModelMap model) {
        UserVo userVo = userService.queryByPhone(phone);
        Integer gender = userVo.getGender();
        // 根据id查询所有题目
        List<TitleVo> titleVos = titleService.query(gender);

        // 查询所有选项
        List<OptionVo> optionVos = optionService.queryAll();

        Map<Integer, List<OptionVo>> optionMap = new HashMap<>();

        for(OptionVo o : optionVos){
            List<OptionVo> options = optionMap.get(o.getTitleId());
            if(options == null){
                options = new ArrayList<>();
                options.add(o);
                optionMap.put(o.getTitleId(), options);
            } else {
                options.add(o);
                optionMap.put(o.getTitleId(), options);
            }
        }

        for(TitleVo t : titleVos){
            t.setOptions(optionMap.get(t.getId()));
            if(t.getNextTitleId() == 34  && gender == 2){
                t.setNextTitleId(40);
            }
        }

        model.put("titleVos", titleVos);
        model.put("uid", userVo.getId());
        return "main/exam";
    }
}
