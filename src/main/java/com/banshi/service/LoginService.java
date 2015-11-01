package com.banshi.service;

import com.banshi.controller.vo.UserVO;
import com.banshi.frame.cache.CacheProxy;
import com.banshi.frame.web.CookieWrapper;
import com.banshi.model.dao.LoginLogDao;
import com.banshi.model.dao.UserDao;
import com.banshi.model.dto.LoginLogDTO;
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


    /**
     * 用户登录处理，根据用户名/密码判断用户信息，登录成功产生session/cookie/cache
     *
     * @param account
     * @param pwd
     * @param validateCode
     * @param request
     * @param response
     * @return
     */
    public UserVO login(String account, String pwd, String validateCode,
                        HttpServletRequest request, HttpServletResponse response) {

        //check输入内容
        //登录名/密码/验证码不能为空
        if (MyString.isBlank(account)) {
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_CHECK_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("account", "登录名不能为空"));
            return userVo;
        }

        if (MyString.isBlank(pwd)) {
            UserVO userVo = new UserVO();
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
        UserDTO cndDto = new UserDTO();
        LoginLogDTO loginLogDto = new LoginLogDTO();
        if (MyString.isMatch(account, Constants.REGEX_USER_MOBILE_FORMAT)) {
            cndDto.setMobile(account);
            loginLogDto.setLoginType(LoginType.MOBILE.name());
        } else if (MyString.isMatch(account, Constants.REGEX_USER_EMAIL_FORMAT)) {
            cndDto.setEmail(account.toLowerCase());
            loginLogDto.setLoginType(LoginType.EMAIL.name());
        } else {
            cndDto.setName(account.toLowerCase());
            loginLogDto.setLoginType(LoginType.NAME.name());
        }

        UserDTO userDto = userDao.selectByUniqueKey(cndDto);
        if (userDto == null) {
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_LOGIC_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("login", "登录用户名不存在，请核对！"));
            return userVo;

        } else if (!CipherUtil.validatePassword(userDto.getPwd(), pwd)) {
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_LOGIC_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("login", "密码错误，请核对！"));
            return userVo;

        } else if (UserEnum.UserStatus.INVALID.name().equals(userDto.getStatus())) {
            UserVO userVo = new UserVO();
            userVo.setRetCode(UserVO.RET_CODE_LOGIC_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("login", "此用户已注销，请核对！"));
            return userVo;

        } else {
            String loginIp = WebUtil.getRemoteIp(request);
            String loginAgent = WebUtil.getUserAgent(request);

            //登录成功，更新登录日志
            loginLogDto.setLoginSrc("PC");
            loginLogDto.setLoginAccount(account);
            loginLogDto.setLoginPwd(CipherUtil.generatePassword(pwd));
            loginLogDto.setLoginIp(loginIp);
            loginLogDto.setLoginAgent(loginAgent);
            loginLogDto.setLoginTime(new Date());

            //后续需要异步，可以在此加上其它异步事件
            loginlogDao.insert(loginLogDto);
        }

        UserVO userVo = new UserVO(userDto);
        boolean isCreated = createSessionAndCookie(request, response, userVo);
        if (isCreated) {
            userVo.setRetCode(UserVO.RET_CODE_SUCCESS);
            userVo.setRetMsg("登录成功");
        } else {
            logout(request, response);
            userVo.setRetCode(UserVO.RET_CODE_LOGIC_ERROR);
            userVo.setRetErrorMap(MapUtil.buildMap("login", "用户登录失败"));
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
        UserVO sessionUserVo = (UserVO) request.getSession().getAttribute(Constants.SESSION_USER);
        if (sessionUserVo == null) {
            return false;
        }
        Logger.debug(this, String.format("sessionUserVo.id = %s", sessionUserVo.getId()));

        CookieWrapper cookieWrapper = new CookieWrapper(request, response);
        String ticketId = cookieWrapper.getCookieValue(Constants.COOKIE_KEY_UT);
        UserVO cacheUserVo = validateTicket(ticketId);
        if (cacheUserVo == null) {
            return false;
        }
        Logger.debug(this, String.format("sessionUserVo.id = %s, cacheUserVo.id = %s", sessionUserVo.getId(), cacheUserVo.getId()));
        if (cacheUserVo.getId() != sessionUserVo.getId()) {
            return false;
        }
        return true;
    }

    /**
     * 退出时清空session,cookie,cache中用户相关信息
     *
     * @param request
     * @param response
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        request.getSession().removeAttribute(Constants.SESSION_USER);
        CookieWrapper cookieWrapper = new CookieWrapper(request, response);
        String ticketId = cookieWrapper.getCookieValue(Constants.COOKIE_KEY_UT);
        if (MyString.isNotBlank(ticketId)) {
            CacheProxy.remove(CacheProxy.CACHE_LOGIN_USER, ticketId);
            cookieWrapper.clearCookie(ticketId, Constants.COOKIE_DOMAIN_ROOT);
        }
    }

    /**
     * 保存登录用户session信息及产生cookie信息
     *
     * @param request
     * @param response
     * @param userVo
     * @return
     */
    private boolean createSessionAndCookie(HttpServletRequest request, HttpServletResponse response,
                                           UserVO userVo) {
        boolean ret = false;
        try {
            request.getSession().setAttribute(Constants.SESSION_USER, userVo);
            String ticketId = createTicket(userVo);
            createCookie(request, response, userVo, ticketId, Constants.COOKIE_DOMAIN_ROOT);
            ret = true;
        } catch (Exception e) {
            Logger.error(this, String.format("createSessionAndCookie failed, error=%s", e.getMessage()), e);
        }
        return ret;
    }

    /**
     * 产生登录令牌
     *
     * @param userVo
     * @return
     */
    private String createTicket(UserVO userVo) {

        //产生ticket
        String ticketId = (MyString.getUUID() + Long.toString(userVo.getId(), 24)).toUpperCase();
        //根据ticket缓存登录用户信息到Cache
        CacheProxy.put(CacheProxy.CACHE_LOGIN_USER, ticketId, userVo);
        return ticketId;
    }

    /**
     * 校验登录令牌
     *
     * @param ticketId
     * @return
     */
    private UserVO validateTicket(String ticketId) {

        Object cacheObj = CacheProxy.get(CacheProxy.CACHE_LOGIN_USER, ticketId);
        if (cacheObj instanceof UserVO) {
            UserVO userVO = (UserVO) cacheObj;
            if (userVO != null) {
                return userVO;
            }
        }
        return null;
    }

    /**
     * 创建登录后cookie
     *
     * @param request
     * @param response
     * @param userVo
     * @param ticketId
     * @param domain
     */
    private void createCookie(HttpServletRequest request, HttpServletResponse response,
                              UserVO userVo, String ticketId, String domain) {

        try {
            CookieWrapper cookieWrapper = new CookieWrapper(request, response);
            //登录后产生的令牌，后面用于登录检验
            cookieWrapper.setCookie(Constants.COOKIE_KEY_UT, ticketId, domain, Constants.COOKIE_TIME_UT);
            //用户显示名，用来在页面上显示用 (对于中文名称可能需要进行转码)
            cookieWrapper.setCookie(Constants.COOKIE_KEY_UN, userVo.getNameDisp(), domain, Constants.COOKIE_TIME_UN);

        } catch (Exception e) {
            Logger.error(this, String.format("createCookie failed, error=%s", e.getMessage()), e);
        }
    }



}
