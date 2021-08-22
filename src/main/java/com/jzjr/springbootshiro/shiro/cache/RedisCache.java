package com.jzjr.springbootshiro.shiro.cache;

import com.jzjr.springbootshiro.utils.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Slf4j
public class RedisCache<K, V> implements Cache<K, V> {
    private String cacheName;

    public RedisCache(String s) {
        this.cacheName = s;
    }

    @Override
    public V get(K k) throws CacheException {
        log.info("cacheName:\t"+cacheName+"\tkey:\t"+k.toString());
        return (V) getRedisTemplate().opsForHash().get(this.cacheName,k.toString());
    }

    @Override
    public V put(K k, V v) throws CacheException {
        log.info("cacheName:\t"+"\tkey:\t"+k.toString()+"\tvalue:\t"+v);
        getRedisTemplate().opsForHash().put(this.cacheName,k.toString(),v);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        log.info("cacheName:\t"+"\tremovekey:\t"+k.toString());
        return (V) getRedisTemplate().opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
        log.info("remove cacheName\t"+cacheName);
        getRedisTemplate().delete(this.cacheName);
    }
    @Override
    public int size() {
        log.info("size:\t"+size());
        return getRedisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<K> keys() {
        log.info("keys:\t"+getRedisTemplate().opsForHash().keys(this.cacheName));
        return getRedisTemplate().opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<V> values() {
        log.info("values:\t"+getRedisTemplate().opsForHash().values(this.cacheName));
        return getRedisTemplate().opsForHash().values(this.cacheName);
    }

    public RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
