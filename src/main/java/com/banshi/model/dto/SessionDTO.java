package com.banshi.model.dto;

import java.util.Date;

public class SessionDTO extends BaseDTO {

    // 登录用户帐号
    private String loginAccount;
    // 登录用户终端
    private String loginAgent;
    // 登录时间
    private Date loginTime;

    // 用户ID
    private Long userId;
    // 用户名
    private String userName;
    // 用户类型
    private String userType;
    // session过期时间
    private Date expireTime;
    // session最大过期时间
    private Date maxExpireTime;

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getLoginAgent() {
        return loginAgent;
    }

    public void setLoginAgent(String loginAgent) {
        this.loginAgent = loginAgent;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getMaxExpireTime() {
        return maxExpireTime;
    }

    public void setMaxExpireTime(Date maxExpireTime) {
        this.maxExpireTime = maxExpireTime;
    }
}
