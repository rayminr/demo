package com.banshi.utils;


import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class MyString extends StringUtils {

    /**
     * 获取去掉“-”的UUID
     * @return
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-","");
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


    protected Map<Object, Object> buildCndMap(Object... conditions) {

        Map<Object, Object> cndMap = new HashMap<Object, Object>();
        for (int i = 0; i < conditions.length; i += 2) {
            cndMap.put(conditions[i], conditions[i + 1]);
        }
        return cndMap;
    }

}
