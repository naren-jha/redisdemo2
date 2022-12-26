package com.example.redisdemo2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListOperationService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addToList(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public Object getFromList(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public List<Object> getAllFromList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public List<Object> getListRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void deleteFromList(String key, long count, Object value) {
        redisTemplate.opsForList().remove(key, count, value);
    }

}
