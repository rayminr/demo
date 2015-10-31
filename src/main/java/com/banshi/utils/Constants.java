package com.banshi.utils;


public class Constants {

    /*字符集UTF-8*/
    public static final String CHARSET_UTF_8 = "UTF-8";

    /*正则表达式：用户名以字母开头的，可以包含数字和符号("_","-",".") */
    public static final String REGEX_USER_NAME_FORMAT = "^[a-zA-Z]([\\w-.]){2,20}$";
    /*正则表达式：邮箱格式xxx@yyy.com*/
    public static final String REGEX_USER_EMAIL_FORMAT = "^\\w+([-.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /*正则表达式：手机格式为11位数字且以1开头*/
    public static final String REGEX_USER_MOBILE_FORMAT = "^[1]\\d{10}$";
    /*正则表达式：密码包含数字\字母和符号的6位以上字符串，且不能为纯数字或纯字母*/
    public static final String REGEX_USER_PWD_FORMAT = "(([\\w!@#$%^&*-_.]){6,20}$)";

    /*session user key*/
    public static final String SESSION_USER = "session.user";
    /*cookie 根域*/
    public static final String COOKIE_DOMAIN_ROOT = "localhost";

    /** cookie ut 过期时间(单位秒) */
    public static final String COOKIE_KEY_UT = "ut";
    public static final int COOKIE_TIME_UT = -1;

    /** cookie un 过期时间(单位秒) */
    public static final String COOKIE_KEY_UN = "un";
    public static final int COOKIE_TIME_UN = 90 * 24 * 60 * 60 ;

}
