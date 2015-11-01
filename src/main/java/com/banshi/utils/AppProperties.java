package com.banshi.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created with IntelliJ IDEA.
 * User: rayminr
 * Date: 3/1/14
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class AppProperties extends PropertyPlaceholderConfigurer {

    private static ConcurrentMap<String, Object> ctxPropertiesMap;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);

        ctxPropertiesMap = new ConcurrentHashMap<String, Object>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }
    }

    /**
     * 根据key获取对应的value
     *
     * @param key
     * @return
     */
    public static String getKVStr(String key) {
        return (String) ctxPropertiesMap.get(key);
    }

    /**
     * 根据key的前缀获取K/V的集合
     *
     * @param prefixKey
     * @return
     */
    public static Map<String, String> getKVMap(String prefixKey) {
        Map<String, String> retMap = new HashMap<String, String>();

        for (String key : ctxPropertiesMap.keySet()) {
            if (key.startsWith(prefixKey)) {
                retMap.put(key, (String) ctxPropertiesMap.get(key));
            }
        }
        return retMap;
    }
}
