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

    /**
     * increments value of the given key by 1.
     * if the key doesn't exist, it will first initialize with key=0 and then increment to 1
     * if the key has a non-long value, it will first initialize with key=0 and then increment to 1
     * @param key
     */
    public void incr(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    /**
     * increments value of the given key by given delta.
     * if the key doesn't exist, it will first initialize with key=0 and then increment by delta
     * if the key has a non-long value, it will first initialize with key=0 and then increment by delta
     * @param key
     * @param delta
     */
    public void incrBy(String key, long delta) {
        redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * decrements value of the given key by 1.
     * if the key doesn't exist, it will first initialize with key=0 and then decrement by -1
     * if the key has a non-long value, it will first initialize with key=0 and then decrement to -1
     * @param key
     */
    public void decr(String key) {
        redisTemplate.opsForValue().decrement(key);
    }

    /**
     * decrements value of the given key by given delta.
     * if the key doesn't exist, it will first initialize with key=0 and then decrement by delta
     * if the key has a non-long value, it will first initialize with key=0 and then decrement by delta
     * @param key
     * @param delta
     */
    public void decrBy(String key, long delta) {
        redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * sets expiry time for a key
     * @param key
     */
    public void setTtl(String key) {
        redisTemplate.expire(key, 20, TimeUnit.SECONDS);
    }

}
