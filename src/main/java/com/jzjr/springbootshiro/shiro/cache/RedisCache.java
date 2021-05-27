package com.jzjr.springbootshiro.shiro.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;

@Slf4j
public class RedisCache<K, V> implements Cache<K, V> {
    @Autowired
    private RedisTemplate redisTemplate;
    private String cacheName;

    public RedisCache(String s) {
        this.cacheName = s;
    }

    @Override
    public V get(K k) throws CacheException {
        log.info("key:\t"+k.toString());
        return (V) redisTemplate.opsForHash().get(this.cacheName,k.toString());
    }

    @Override
    public V put(K k, V v) throws CacheException {
        log.info("key:\t"+k.toString()+"\tvalue:\t"+v);
        redisTemplate.opsForHash().put(this.cacheName,k.toString(),v);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        log.info("removekey:\t"+k.toString());
        return (V) redisTemplate.opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
        log.info("remove cacheName\t"+cacheName);
        redisTemplate.delete(this.cacheName);
    }
    @Override
    public int size() {
        log.info("size:\t"+size());
        return redisTemplate.opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<K> keys() {
        log.info("keys:\t"+redisTemplate.opsForHash().keys(this.cacheName));
        return redisTemplate.opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<V> values() {
        log.info("values:\t"+redisTemplate.opsForHash().values(this.cacheName));
        return redisTemplate.opsForHash().values(this.cacheName);
    }
}
