package com.jzjr.springbootshiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class RedisCacheManager implements CacheManager {
    private String chcheName;

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        this.chcheName = s;
        return new RedisCache(chcheName);
    }
}
