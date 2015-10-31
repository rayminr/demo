package com.banshi.frame.cache;


import com.banshi.utils.Logger;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class CacheProxy {

    public static final String CACHE_LOGIN_USER = "cache.login";

    private static CacheManager cacheManager;

    private CacheProxy(){
        URL url = getClass().getResource("/cache/ehcache.xml");
        cacheManager = CacheManager.create(url);
    }

    /**
     * 添加缓存key/value
     * @param key
     * @param value
     * @return
     */
    public static boolean put(String cacheName, Object key, Object value) {
        boolean result=true;
        try{
            Cache cache = cacheManager.getCache(cacheName);
            Element element = new Element(key, value);
            cache.put(element);
        } catch (Exception e){
            result=false;
            Logger.error(CacheProxy.class, String.format("ehCache put failed, key=%s", key), e);
        }
        return result;
    }

    /**
     * 获取缓存key对应值
     * @param key
     * @return
     */
    public static Object get(String cacheName, Object key) {
        try{
            Cache cache = cacheManager.getCache(cacheName);
            return cache.get(key).getObjectValue();
        } catch (Exception e){
            Logger.error(CacheProxy.class, String.format("ehCache get failed, key=%s",key),e);
        }
        return null;
    }

    /**
     * 清除缓存key对应值
     * @param key
     * @return
     */
    public static boolean remove(String cacheName, Object key) {
        try{
            Cache cache = cacheManager.getCache(cacheName);
            return cache.remove(key);
        } catch (Exception e){
            Logger.error(CacheProxy.class, String.format("ehCache remove failed, key=%s",key),e);
        }
        return false;
    }
}
