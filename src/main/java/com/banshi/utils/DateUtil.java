package com.banshi.utils;

import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil extends DateUtils {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MI = "yyyy-MM-dd HH:mm";

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 根据给定日期字符串和日期格式 创建日期
     *
     * @param dateString
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date createDate(String dateString, String pattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(dateString);
    }
}
