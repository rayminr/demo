package com.banshi.controller.vo;

import java.util.Date;

public class SessionUserVO extends BaseVO {

    // 用户登录ID
    private Long loginId;
    // 用户名
    private String userName;
    // 用户名(保存用户原始输入，页面显示用)
    private String userNameDisp;
    // 用户昵称
    private String userNickName;
    // 手机号码
    private String mobile;
    // 电子邮箱
    private String email;
    // 电子邮箱(保存用户原始输入，页面显示用)
    private String emailDisp;
    // 用户类型
    private String type;
    // 用户来源
    private String src;
    // 用户状态
    private String status;
    // 上次登录时间
    private Date lastLoginTime;

}
