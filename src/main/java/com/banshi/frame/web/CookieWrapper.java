package com.banshi.frame.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CookieWrapper {

    private HttpServletRequest request;

    private HttpServletResponse response;

    public CookieWrapper(HttpServletRequest req, HttpServletResponse res) {
        request = req;
        response = res;
    }

    /**
     * 获取某个cookie值
     *
     * @param name
     * @return
     */
    public String getCookieValue(String name) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 设置cookie值
     *
     * @param name
     * @param value
     * @param domain
     * @param expireSeconds
     */
    public void setCookie(String name, String value, String domain, int expireSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        if (expireSeconds >= 0) {
            cookie.setMaxAge(expireSeconds);
        }
        response.addCookie(cookie);
    }

    /**
     * 设置cookie值
     *
     * @param name
     * @param value
     * @param domain
     */
    public void setCookie(String name, String value, String domain) {
        setCookie(name, value, domain, -1);
    }

    /**
     * 清除某个cookie值
     *
     * @param name
     * @param domain
     */
    public void clearCookie(String name, String domain) {
        setCookie(name, "", domain, 0);
    }

    /**
     * 清除所有cookie
     */
    public void clearAllCookie() {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setValue(null);
        }
    }

    /**
     * 获取某个cookie域
     *
     * @param name
     * @return
     */
    public String getCookieDomain(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                return cookie.getDomain();
            }
        }

        return null;
    }

    /**
     * 获取某个cookie
     *
     * @param name
     * @return
     */
    public Cookie getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                return cookie;
            }
        }
        return null;
    }

}
