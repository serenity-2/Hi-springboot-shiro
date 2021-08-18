package com.jzjr.springbootshiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;

public class RedisCache implements Cache {
    @Autowired
    private RedisTemplate redisTemplate;
    private String cacheName;

    public RedisCache(String cacheName) {
       this.cacheName = cacheName;
    }

    @Override
    public Object get(Object o) throws CacheException {
        String value = redisTemplate.opsForHash().get(cacheName, o).toString();
        return value;
    }

    @Override
    public Object put(Object o, Object o2) throws CacheException {
        redisTemplate.opsForHash().put(cacheName,o,o2);
        return null;
    }

    @Override
    public Object remove(Object o) throws CacheException {
        return redisTemplate.delete(o);
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set keys() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }
}
