package com.banshi.service;

import com.banshi.model.dao.SessionDao;
import com.banshi.model.dto.SessionDTO;
import com.banshi.frame.cache.CacheProxy;
import com.banshi.utils.Constants;
import com.banshi.utils.Logger;
import com.banshi.utils.MyString;
import com.banshi.frame.web.CookieWrapper;
import com.banshi.controller.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SessionService {

    @Resource
    private SessionDao sessionDao;


    /**
     * 登录后产生session级别的令牌
     *
     * @param sessionDTO
     * @return
     */
    private String createSessionTicket(SessionDTO sessionDTO) {

        //产生ticket
        String ticketId = (MyString.getUUID() + Long.toString(sessionDTO.getUserId(), 24)).toUpperCase();
        //根据ticket缓存登录用户信息到Cache
        CacheProxy.put(CacheProxy.CACHE_LOGIN_USER, ticketId, sessionDTO);
        return ticketId;
    }

    /**
     * 校验登录令牌
     *
     * @param ticketId
     * @return
     */
    private SessionDTO validateSessionTicket(String ticketId) {

        Object cacheObj = CacheProxy.get(CacheProxy.CACHE_LOGIN_USER, ticketId);
        if (cacheObj != null && cacheObj instanceof SessionDTO) {
            SessionDTO sessionDTO = (SessionDTO) cacheObj;
            if (sessionDTO != null) {
                return sessionDTO;
            }
        }
        return null;
    }

    /**
     * 判断用户是否登录：session中用户不存在或者ticketId对应的cache用户不存在或者两者用户信息不一致返回false
     *
     * @param request
     * @param response
     * @return
     */
    public boolean isLogin(HttpServletRequest request, HttpServletResponse response) {
        SessionDTO sessionDTO = (SessionDTO) request.getSession().getAttribute(Constants.SESSION_USER);
        if (sessionDTO == null) {
            return false;
        }
        Logger.debug(this, String.format("sessionDTO.userId = %s", sessionDTO.getUserId()));

        CookieWrapper cookieWrapper = new CookieWrapper(request, response);
        String ticketId = cookieWrapper.getCookieValue(Constants.COOKIE_KEY_UT);
        SessionDTO cacheSessionDTO = validateSessionTicket(ticketId);
        if (cacheSessionDTO == null) {
            return false;
        }
        Logger.debug(this, String.format("sessionDTO.UserId = %s, cacheSessionDTO.UserId= %s", sessionDTO.getUserId(), cacheSessionDTO.getUserId()));
        if (sessionDTO.getUserId() != cacheSessionDTO.getUserId()) {
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
            String ticketId = "";//createSessionTicket(userVo);
            createCookie(request, response, userVo, ticketId, Constants.COOKIE_DOMAIN_ROOT);
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
