package com.exam.controller;

import com.exam.service.IUserService;
import com.exam.support.util.StringUtil;
import com.exam.support.util.json.core.annotation.Json;
import com.exam.support.util.json.core.annotation.ResponseJson;
import com.exam.support.util.json.core.utils.ResponseVo;
import com.exam.support.util.string.StringUtils;
import com.exam.vo.login.UserLoginVo;
import com.exam.vo.user.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private IUserService userService;

    /**
     * 登录首页面
     *
     * @return
     */
    @RequestMapping(value = "/html", method = RequestMethod.GET)
    public String html() {
        return "login/login";
    }

    /**
     * 登录逻辑
     *
     * @param userLoginVo
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseJson
    public ResponseVo login(@Json UserLoginVo userLoginVo, HttpServletRequest request) {

        ResponseVo responseVo = new ResponseVo();

        if(StringUtils.isEmpty(userLoginVo.getPhone()) || StringUtils.isEmpty(userLoginVo.getPassword())){
            logger.error("用户名或密码为空!");
            responseVo.setMsg("请输入用户名和密码!");
            responseVo.setSuccess(false);
            return responseVo;
        }

        // 名字位管理员跳转到admin首页
        if("admin".equals(userLoginVo.getPhone()) && "admin".equals(userLoginVo.getClass())) {
            responseVo.setSuccess(true);
            responseVo.setMsg("登录成功!");
            responseVo.setData("/main/index");
            return responseVo;
        }

        UserVo user = userService.queryByPhone(userLoginVo.getPhone());

        if(user == null){
            logger.error("用户不存在!");
            responseVo.setMsg("用户不存在!");
            responseVo.setSuccess(false);
            return responseVo;
        }

        //验证用户名 密码
        String password = userLoginVo.getPhone().substring(userLoginVo.getPhone().length()-4);
        if(!password.equals(userLoginVo.getPassword())){
            logger.error("密码错误!");
            responseVo.setMsg("密码错误!");
            responseVo.setSuccess(false);
            return responseVo;
        }

        if(!StringUtil.isEmpty(user.getName())){
            // 开始做题
            responseVo.setSuccess(true);
            responseVo.setMsg("登录成功!");
            responseVo.setData("/main/startExam");
            return responseVo;
        }

        responseVo.setSuccess(true);
        responseVo.setMsg("登录成功!");
        responseVo.setData("/main/fillInfo");
        return responseVo;
    }
}
