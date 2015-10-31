package com.banshi.utils;

import java.util.HashMap;
import java.util.Map;


public class MapUtil {

    /**
     * 根据key/value对的参数列表构造参数Map,key必须为String类型
     *
     * @param parameters
     * @return
     */
    public static Map<String, Object> buildMap(Object... parameters) {

        Map<String, Object> retMap = new HashMap<String, Object>();
        for (int i = 0; i < parameters.length; i += 2) {
            retMap.put((String) parameters[i], parameters[i + 1]);
        }
        return retMap;
    }
}
