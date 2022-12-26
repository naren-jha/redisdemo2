package com.example.redisdemo2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ValueOperationService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Adds a new KV value pair or updates value if an entry for key already exists
     * @param key
     * @param value
     */
    public void addOrUpdate(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * get the value for a key
     * @param key
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Set the value for the key and a TTL of {ttlInSecs} seconds
     * @param key
     * @param value
     * @param ttlInSecs
     */
    public void addWithTtl(String key, Object value, long ttlInSecs) {
        redisTemplate.opsForValue().set(key, value, ttlInSecs, TimeUnit.SECONDS);
    }

}
