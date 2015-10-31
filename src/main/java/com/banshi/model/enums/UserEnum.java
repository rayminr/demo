package com.banshi.model.enums;

/**
 * 用户相关枚举类型
 */
public class UserEnum {

    /**
     * 用户状态
     */
    public enum UserStatus {

        VALID("有效"),
        INVALID("无效");

        private String value;

        private String getValue() {
            return this.value;
        }

        private UserStatus(String value) {
            this.value = value;
        }

    }

    /**
     * 用户类型
     */
    public enum UserType {

        OWN("本站用户"),
        UNION("联合登录用户");

        private String value;

        private String getValue() {
            return this.value;
        }

        private UserType(String value) {
            this.value = value;
        }

    }

    /**
     * 用户来源
     */
    public enum UserSrc {

        PC("PC用户"),
        MOBILE("Mobile用户");

        private String value;

        private String getValue() {
            return this.value;
        }

        private UserSrc(String value) {
            this.value = value;
        }

    }

    /**
     * 用户来源
     */
    public enum UserSex {

        M("男性"),
        F("女性");

        private String value;

        private String getValue() {
            return this.value;
        }

        private UserSex(String value) {
            this.value = value;
        }

    }

}
