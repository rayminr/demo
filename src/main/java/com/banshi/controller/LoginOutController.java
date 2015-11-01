package com.banshi.controller;

import com.banshi.controller.vo.UserVO;
import com.banshi.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginOutController {

    @Resource
    private LoginService loginService;

    /**
     * 跳转到登录页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toLogin.do", method = RequestMethod.GET)
    public ModelAndView toLogin() throws Exception {
        return new ModelAndView("user/login");
    }

    /**
     * 登录
     *
     * @param account
     * @param pwd
     * @param validateCode
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam(value = "account") String account,
                              @RequestParam(value = "pwd") String pwd,
                              @RequestParam(value = "validateCode") String validateCode,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {

        UserVO userVo = loginService.login(account, pwd, validateCode, request, response);
        if (!UserVO.RET_CODE_SUCCESS.equals(userVo.getRetCode())) {
            return new ModelAndView("user/login", "user", userVo);
        } else {
            return new ModelAndView("user/loginSuccess", "user", userVo);
        }
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        loginService.logout(request, response);
        return new ModelAndView("user/login");
    }

}

