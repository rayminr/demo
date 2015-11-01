package com.banshi.model.dto;

import java.util.Date;

public class SessionDTO extends BaseDTO {

    // 主键
    private String id;
    // 登录用户帐号
    private String loginAccount;
    // 登录用户终端
    private String loginAgent;
    // 登录用户IP
    private String loginIp;
    // 登录用户来源
    private String loginSrc;

    // 用户ID
    private Long userId;
    // 用户名
    private String userName;
    // 用户类型
    private String userType;

    // session ticketId
    private String ticketId;
    // session创建时间
    private Long ticketCreateTime;
    // session访问时间
    private Long ticketAccessTime;

    // 创建时间
    private Date createdAt;
    // 创建人
    private String createdBy;
    // 更新时间
    private Date updatedAt;
    // 更新人
    private String updatedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginSrc() {
        return loginSrc;
    }

    public void setLoginSrc(String loginSrc) {
        this.loginSrc = loginSrc;
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

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Long getTicketCreateTime() {
        return ticketCreateTime;
    }

    public void setTicketCreateTime(Long ticketCreateTime) {
        this.ticketCreateTime = ticketCreateTime;
    }

    public Long getTicketAccessTime() {
        return ticketAccessTime;
    }

    public void setTicketAccessTime(Long ticketAccessTime) {
        this.ticketAccessTime = ticketAccessTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
