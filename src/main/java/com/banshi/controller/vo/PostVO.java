package com.banshi.controller.vo;

import com.banshi.model.dto.PostDTO;

import java.util.Date;

public class PostVO extends BaseVO {

    // 对应数据库表主键
    private Long id;
    // 用户ID
    private Long userId;
    // 用户名
    private String userName;
    // 用户IP
    private String userIp;
    // 日志标题
    private String postTitle;
    // 日志内容
    private String postContent;
    // 日志状态
    private String status;
    // 创建时间
    private Date createAt;
    // 修改时间
    private Date updateAt;

    public PostVO(){

    }

    public PostVO(PostDTO postDTO){
        if(postDTO != null){
            this.id = postDTO.getId();
            this.userId = postDTO.getUserId();
            this.userName = postDTO.getUserName();
            this.userIp = postDTO.getUserIp();
            this.postTitle = postDTO.getTitle();
            this.postContent = postDTO.getContent();
            this.status = postDTO.getStatus();
            this.createAt = postDTO.getCreatedAt();
            this.updateAt = postDTO.getUpdatedAt();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
