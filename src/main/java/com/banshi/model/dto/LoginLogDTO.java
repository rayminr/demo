package com.banshi.model.dto;

import java.util.Date;

public class LoginLogDTO extends BaseDTO {

    // 对应数据库表主键
    private Long id;
    // 登录用户来源
    private String loginSrc;
    // 登录用户类型(用户名/手机/邮箱)
    private String loginType;
    // 登录用户帐号
    private String loginAccount;
    // 登录密码（加密后）
    private String loginPwd;
    // 登录时间
    private Date loginTime;
    // 登录用户的IP（客户端请求IP）
    private String loginIp;
    // 登录用户的浏览器信息
    private String loginAgent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginSrc() {
        return loginSrc;
    }

    public void setLoginSrc(String loginSrc) {
        this.loginSrc = loginSrc;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginAgent() {
        return loginAgent;
    }

    public void setLoginAgent(String loginAgent) {
        this.loginAgent = loginAgent;
    }
}
