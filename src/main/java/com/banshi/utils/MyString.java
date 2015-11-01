package com.banshi.utils;


import org.apache.commons.lang.StringUtils;

import java.util.UUID;
import java.util.regex.Pattern;

public class MyString extends StringUtils {

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 校验UUID格式
     *
     * @return
     */
    public static boolean isUUID(String uuid) {
        return isNotBlank(uuid) && uuid.length() == 36 && uuid.split("-").length == 5;
    }

    /**
     * 按正则表达时匹配
     *
     * @param str
     * @param regex
     * @return 符合规则返回true
     */
    public static boolean isMatch(String str, String regex) {
        return Pattern.compile(regex).matcher(str).matches();
    }

    /**
     * 截取指定长度字符串
     *
     * @param str
     * @param length
     * @return
     */
    public static String trimToLen(String str, int length) {
        if (isBlank(str) || str.length() <= length) {
            return str;
        } else {
            return substring(str, 0, length);
        }
    }

}
