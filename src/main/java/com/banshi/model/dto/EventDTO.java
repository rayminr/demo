package com.banshi.model.dto;

import java.util.Date;

public class EventDTO extends BaseDTO {

    // 对应数据库表主键
    private Long id;
    // 事件名
    private String name;
    // 事件类型(工作/生活/娱乐)
    private String type;
    // 任务优先级(紧急重要/紧急不重要/不紧急重要/不紧急不重要)
    private String priority;
    // 频率(每小时/每天/每周/每月)
    private String frequency;
    // 状态
    private String status;
    // 开始时间
    private String when_start;
    // 结束时间
    private String when_end;
    // 对象
    private String who;
    // 地点
    private String where;
    // 内容
    private String what;
    // 用户的IP（客户端请求IP）
    private String userIp;
    // 程序调用端IP
    private String clientIp;
    // 创建时间
    private Date createdAt;
    // 创建人
    private String createdBy;
    // 更新时间
    private Date updatedAt;
    // 更新人
    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWhen_start() {
        return when_start;
    }

    public void setWhen_start(String when_start) {
        this.when_start = when_start;
    }

    public String getWhen_end() {
        return when_end;
    }

    public void setWhen_end(String when_end) {
        this.when_end = when_end;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
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
