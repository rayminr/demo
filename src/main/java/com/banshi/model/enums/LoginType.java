package com.banshi.model.enums;

/**
 * 登录类型
 */
public enum LoginType {

    MOBILE("手机"),
    EMAIL("邮箱"),
    NAME("用户名");

    private String value;

    private String getValue() {
        return this.value;
    }

    private LoginType(String value) {
        this.value = value;
    }

}
