package com.example.redisdemo2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class HashOperationService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setHashField(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public Object getHashField(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public Map<Object, Object> getAllHashFields(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void deleteHashField(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    public void incrHashField(String key, String field, long delta) {
        redisTemplate.opsForHash().increment(key, field, delta);
    }

}
