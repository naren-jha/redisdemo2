package com.example.redisdemo2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

/*
A Redis sorted set is a data structure that represents an ordered collection of elements,
with each element having an associated score. The elements are sorted in ascending order by score.
You can use sorted sets to store items that need to be ranked or sorted in some way, such as the top scores in a game
or the most popular articles on a website. You can use the opsForZSet() methods to manipulate the contents of a sorted set.
 */
@Service
public class ZSetOperationService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addToZSet(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public Set<Object> getAllKeys(String key) {
        return redisTemplate.opsForZSet().range(key, 0, -1);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getAllKeyValues(String key) {
        return redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
    }

    public Set<Object> getKeysInRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getAllKeyValuesInRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    public void removeFromZSet(String key, Object... values) {
        redisTemplate.opsForZSet().remove(key, values);
    }

}
