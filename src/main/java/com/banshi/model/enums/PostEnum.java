package com.banshi.model.enums;

/**
 * 日志相关枚举类型
 */
public class PostEnum {

    /**
     * 用户状态
     */
    public enum PostStatus {

        NEW("新提交"),
        AUDIT("待审核");

        private String value;

        private String getValue() {
            return this.value;
        }

        private PostStatus(String value) {
            this.value = value;
        }

    }

}
