package com.example.redisdemo2.applications.leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LeaderboardService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addScore(String leaderboardName, String player, double score) {
        redisTemplate.opsForZSet().add(leaderboardName, player, score);
    }

    public Set<Object> getTopScoringPlayers(String leaderboardName, long count) {
        return redisTemplate.opsForZSet().reverseRange(leaderboardName, 0, count - 1);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getTopScoringPlayerAndScores(String leaderboardName, long count) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(leaderboardName, 0, count - 1);
    }

    public Set<Object> getAllPlayers(String leaderboardName) {
        return redisTemplate.opsForZSet().reverseRange(leaderboardName, 0,- 1);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getAllPlayerAndScores(String leaderboardName) {
        return redisTemplate.opsForZSet().rangeWithScores(leaderboardName, 0, -1);
    }

    public Double getPlayerScore(String leaderboardName, String player) {
        return redisTemplate.opsForZSet().score(leaderboardName, player);
    }

    public Long getRank(String leaderboardName, String player) {
        return redisTemplate.opsForZSet().reverseRank(leaderboardName, player);
    }

}
