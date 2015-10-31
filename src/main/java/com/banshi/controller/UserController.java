package com.banshi.controller;

import com.banshi.model.dto.UserDTO;
import com.banshi.service.UserService;
import com.banshi.utils.WebUtil;
import com.banshi.controller.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 跳转到用户注册页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addUser.do", method = RequestMethod.GET)
    public ModelAndView register() throws Exception {
        return new ModelAndView("user/addUser");
    }

    /**
     * 用户注册
     * @param userDto
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/register.do", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("registerUser") UserDTO userDto, HttpServletRequest request) throws Exception {
        userDto.setUserIp(WebUtil.getRemoteIp(request));
        UserVO userVo = userService.addUser(userDto);

        if(!UserVO.RET_CODE_SUCCESS.equals(userVo.getRetCode())){
            //登录失败
            return new ModelAndView("user/addUser", "user", userVo);
        } else {
            //登录成功，产生csessionid并放入cookie TODO
            return new ModelAndView("user/registerSuccess", "user", userVo);
        }
    }

    /**
     * 显示用户信息
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/showUser.do")
    public ModelAndView showUser(@RequestParam(value = "id") Long id) throws Exception {
        UserDTO userDto = userService.getUserById(id);
        return new ModelAndView("user/showUser", "user", userDto);
    }

    /**
     * 显示用户信息（异步接口）
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/showUser.ajax")
    @ResponseBody
    public String AjaxShowUser(@RequestParam(value = "id") Long id) throws Exception {
        UserDTO userDto = userService.getUserById(id);
        UserVO userVo = new UserVO(userDto);
        userVo.setRetCode(UserVO.RET_CODE_SUCCESS);
        return userVo.toJson();
    }

}

