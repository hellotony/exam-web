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
@RequestMapping("/user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private IUserService userService;

    /**
     * 更新信息
     *
     * @param userVo
     * @return
     */
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    @ResponseJson
    public ResponseVo updateInfo(@Json UserVo userVo) {

        ResponseVo responseVo = new ResponseVo();

        try{
            userService.updateInfoByPhone(userVo);
            responseVo.setSuccess(true);
        } catch (Exception e) {
            responseVo.setSuccess(false);
            responseVo.setData(e.getMessage());
        }

        return responseVo;
    }
}
