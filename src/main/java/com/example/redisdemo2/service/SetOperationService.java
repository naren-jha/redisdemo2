package com.example.redisdemo2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SetOperationService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addToSet(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    public Set<Object> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public void removeFromSet(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    public void isMember(String key, Object value) {
        redisTemplate.opsForSet().isMember(key, value);
    }

    public void intersectSets(String key, List<String> otherSetKeys, String destKey) {
        redisTemplate.opsForSet().intersectAndStore(key, otherSetKeys, destKey);
    }

}
