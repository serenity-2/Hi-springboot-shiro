package com.jzjr.springbootshiro.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class RedisManagerCache implements CacheManager {
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        System.out.println("cacheName:\n"+s);
        return new RedisCache<K,V>(s);
    }
}
