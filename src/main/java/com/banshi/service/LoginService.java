package com.banshi.service;

import com.banshi.controller.vo.UserVO;
import com.banshi.frame.cache.CacheProxy;
import com.banshi.frame.web.CookieWrapper;
import com.banshi.model.dao.LoginLogDao;
import com.banshi.model.dao.UserDao;
import com.banshi.model.dto.LoginLogDTO;
import com.banshi.model.dto.SessionDTO;
import com.banshi.model.dto.UserDTO;
import com.banshi.model.enums.LoginType;
import com.banshi.model.enums.UserEnum;
import com.banshi.utils.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class LoginService {

    @Resource
    private UserDao userDao;

    @Resource
    private LoginLogDao loginlogDao;

    @Resource
    private SessionService sessionService;


    /**
     * 用户登录处理，根据登录名/密码判断用户信息，登录成功产生session/cookie/cache
     *
     * @param loginSrc
     * @param loginAccount
     * @param loginPwd
     * @param loginValidateCode
     * @param request
     * @param response
     * @return
     */
    public UserVO login(String loginSrc, String loginAccount, String loginPwd, String loginValidateCode,
                        HttpServletRequest request, HttpServletResponse response) {

        UserVO userVo = new UserVO();

        //check输入内容
        //登录名/密码/验证码不能为空
        if (MyString.isBlank(loginAccount)) {
            userVo.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("account", "登录名不能为空"));
            return userVo;
        }

        if (MyString.isBlank(loginPwd)) {
            userVo.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("pwd", "密码不能为空"));
            return userVo;
        }

        //校验验证码
        //TODO
        /*if (MyString.isBlank(validateCode)) {
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("validateCode", "验证码不能为空"));
            return userVo;
        }*/

        //根据account判断是哪种类型的登录
        UserDTO cndUserDto = new UserDTO();
        LoginLogDTO loginLogDto = new LoginLogDTO();

        if (MyString.isMatch(loginAccount, Constants.REGEX_USER_MOBILE_FORMAT)) {
            cndUserDto.setMobile(loginAccount.toLowerCase());
            loginLogDto.setLoginType(LoginType.MOBILE.name());
        } else if (MyString.isMatch(loginAccount, Constants.REGEX_USER_EMAIL_FORMAT)) {
            cndUserDto.setEmail(loginAccount.toLowerCase());
            loginLogDto.setLoginType(LoginType.EMAIL.name());
        } else {
            cndUserDto.setName(loginAccount.toLowerCase());
            loginLogDto.setLoginType(LoginType.NAME.name());
        }

        UserDTO userDto = userDao.selectByUniqueKey(cndUserDto);
        if (userDto == null) {
            userVo.setRetCode(UserVO.RET_CODE_LOGIC_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("login", "登录用户名不存在，请核对！"));
            return userVo;

        } else if (!CipherUtil.validatePassword(userDto.getPwd(), loginPwd)) {
            userVo.setRetCode(UserVO.RET_CODE_LOGIC_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("login", "密码错误，请核对！"));
            return userVo;

        } else if (UserEnum.UserStatus.INVALID.name().equals(userDto.getStatus())) {
            userVo.setRetCode(UserVO.RET_CODE_LOGIC_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("login", "此用户已注销，请核对！"));
            return userVo;

        } else {
            String loginIp = WebUtil.getRemoteIp(request);
            String loginAgent = WebUtil.getUserAgent(request);

            SessionDTO sessionDTO = new SessionDTO();
            sessionDTO.setLoginAccount(loginAccount);
            sessionDTO.setLoginIp(loginIp);
            sessionDTO.setLoginAgent(loginAgent);
            sessionDTO.setLoginSrc(loginSrc);

            sessionDTO.setUserId(userDto.getId());
            sessionDTO.setUserName(userDto.getName());
            sessionDTO.setUserType(userDto.getType());

            boolean isCreated = createSessionAndCookie(request, response, sessionDTO);
            if (isCreated) {
                userVo.setRetCode(UserVO.RET_CODE_SUCCESS);
                userVo.setRetMsg("登录成功");
            } else {
                logout(request, response);
                userVo.setRetCode(UserVO.RET_CODE_LOGIC_ERROR);
                userVo.setRetErrorMap(MapUtil.buildMap("login", "用户登录失败"));
            }

            //登录成功，更新登录日志
            loginLogDto.setLoginSrc(loginSrc);
            loginLogDto.setLoginAccount(loginAccount);
            loginLogDto.setLoginPwd(userDto.getPwd());
            loginLogDto.setLoginIp(loginIp);
            loginLogDto.setLoginAgent(loginAgent);

            //后续需要异步，可以在此加上其它异步事件
            loginlogDao.insert(loginLogDto);
        }

        return userVo;
    }

    /**
     * 判断用户是否登录：session中用户不存在或者ticketId对应的cache用户不存在或者两者用户信息不一致返回false
     *
     * @param request
     * @param response
     * @return
     */
    public boolean isLogin(HttpServletRequest request, HttpServletResponse response) {

        CookieWrapper cookieWrapper = new CookieWrapper(request, response);
        String ticketId = cookieWrapper.getCookieValue(Constants.COOKIE_KEY_UT);
        return sessionService.validateSessionTicket(ticketId);
    }

    /**
     * 退出时清空session,cookie,cache中用户相关信息
     *
     * @param request
     * @param response
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        CookieWrapper cookieWrapper = new CookieWrapper(request, response);
        String ticketId = cookieWrapper.getCookieValue(Constants.COOKIE_KEY_UT);
        if (MyString.isNotBlank(ticketId)) {
            CacheProxy.remove(CacheProxy.CACHE_LOGIN_USER, ticketId);
        }
        cookieWrapper.clearCookie(Constants.COOKIE_KEY_UT, Constants.COOKIE_DOMAIN_ROOT);
        cookieWrapper.clearCookie(Constants.COOKIE_KEY_UN, Constants.COOKIE_DOMAIN_ROOT);
    }

    /**
     * 保存登录用户session信息及产生cookie信息
     *
     * @param request
     * @param response
     * @param sessionDTO
     * @return
     */
    private boolean createSessionAndCookie(HttpServletRequest request, HttpServletResponse response,
                                           SessionDTO sessionDTO) {
        boolean ret = false;
        try {
            String ticketId = sessionService.createSessionTicket(sessionDTO);
            Logger.debug(this,"createSessionAndCookie ticketId = " + ticketId);
            createCookie(request, response, sessionDTO, Constants.COOKIE_DOMAIN_ROOT);
            ret = true;
        } catch (Exception e) {
            Logger.error(this, String.format("createSessionAndCookie failed, error=%s", e.getMessage()), e);
        }
        return ret;
    }

    /**
     * 创建登录后cookie
     *
     * @param request
     * @param response
     * @param sessionDTO
     * @param domain
     */
    private void createCookie(HttpServletRequest request, HttpServletResponse response,
                              SessionDTO sessionDTO, String domain) {

        try {
            CookieWrapper cookieWrapper = new CookieWrapper(request, response);
            //登录后产生的令牌，后面用于登录检验
            cookieWrapper.setCookie(Constants.COOKIE_KEY_UT, sessionDTO.getTicketId(), domain, Constants.COOKIE_TIME_UT);
            //用户显示名，用来在页面上显示用 (对于中文名称可能需要进行转码)
            cookieWrapper.setCookie(Constants.COOKIE_KEY_UN, sessionDTO.getLoginAccount(), domain, Constants.COOKIE_TIME_UN);

        } catch (Exception e) {
            Logger.error(this, String.format("createCookie failed, error=%s", e.getMessage()), e);
        }
    }

}
