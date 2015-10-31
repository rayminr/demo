package com.banshi.model.dto;

import java.util.Date;

public class LinkDTO extends BaseDTO {

    // 对应数据库表主键
    private Long id;
    // postID
    private Long postId;
    // 添加者用户ID
    private Long userId;
    // 链接URL
    private String linkUrl;
    // 链接标题
    private String linkName;
    // 链接图片
    private String linkImage;
    // 链接打开方式
    private String linkTarget;
    // 链接描述
    private String linkRemark;
    // 是否可见（Y/N）
    private String linkVisible;
    // 评分等级
    private String linkRating;
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

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getLinkTarget() {
        return linkTarget;
    }

    public void setLinkTarget(String linkTarget) {
        this.linkTarget = linkTarget;
    }

    public String getLinkRemark() {
        return linkRemark;
    }

    public void setLinkRemark(String linkRemark) {
        this.linkRemark = linkRemark;
    }

    public String getLinkVisible() {
        return linkVisible;
    }

    public void setLinkVisible(String linkVisible) {
        this.linkVisible = linkVisible;
    }

    public String getLinkRating() {
        return linkRating;
    }

    public void setLinkRating(String linkRating) {
        this.linkRating = linkRating;
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
