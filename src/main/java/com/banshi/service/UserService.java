package com.banshi.service;

import com.banshi.model.dao.UserDao;
import com.banshi.model.dto.UserDTO;
import com.banshi.utils.*;
import com.banshi.controller.vo.UserVO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    /**
     * 用户注册处理
     *
     * @param userDto
     * @return
     */
    public UserVO addUser(UserDTO userDto) {

        //输入check
        //用户名/手机/邮箱不能全为空
        if (MyString.isBlank(userDto.getName()) && MyString.isBlank(userDto.getEmail()) && MyString.isBlank(userDto.getMobile())) {
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("name_email_mobile", "用户名/手机/邮箱不能全为空"));
            return userVo;
        }

        //用户名规则check
        if (MyString.isNotBlank(userDto.getName()) && !MyString.isMatch(userDto.getName(), Constants.REGEX_USER_NAME_FORMAT)) {
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("name", "用户名不符合规则"));
            return userVo;
        }

        //手机规则check
        if (MyString.isNotBlank(userDto.getMobile()) && !MyString.isMatch(userDto.getMobile(), Constants.REGEX_USER_MOBILE_FORMAT)) {
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("mobile", "手机号码不符合规则"));
            return userVo;
        }

        //邮箱规则check
        if (MyString.isNotBlank(userDto.getEmail()) && !MyString.isMatch(userDto.getEmail(), Constants.REGEX_USER_EMAIL_FORMAT)) {
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("mobile", "邮箱不符合规则"));
            return userVo;
        }

        //密码规则check
        if (MyString.isBlank(userDto.getPwd()) || !MyString.isMatch(userDto.getPwd(), Constants.REGEX_USER_PWD_FORMAT)) {
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("pwd", "密码不符合规则"));
            return userVo;
        }

        //加密密码
        userDto.setPwd(CipherUtil.generatePassword(userDto.getPwd()));
        userDto.setCreatedAt(new Date());
        userDto.setCreatedBy("app");

        try{
            userDao.insert(userDto);
        } catch (DuplicateKeyException e){
            e.printStackTrace();
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_LOGIC_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("logic", "唯一约束字段重复"));
            return userVo;
        } catch (Exception e){
            e.printStackTrace();
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_SYS_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("system", "系统异常"));
            return userVo;
        }

        UserVO userVo = new UserVO(userDto);
        userVo.setRetCode(UserVO.RET_CODE_SUCCESS);
        userVo.setRetMsg("注册成功");

        //注册成功后异步处理事件可以加在这里

        return userVo;
    }



    /**
     * 根据id更新用户
     *
     * @param userDto
     * @return
     */
    public int updateUserById(UserDTO userDto) {
        return userDao.update(userDto);
    }

    /**
     * 按id查找用户
     *
     * @param id
     * @return
     */
    public UserDTO getUserById(Long id) {
        return userDao.selectById(id);
    }

}
