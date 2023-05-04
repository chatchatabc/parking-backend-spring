package com.chatchatabc.parking.impl.infra.service;

import com.chatchatabc.parking.infra.service.KVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class KVServiceRedisImpl implements KVService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * Set key value pair
     *
     * @param key   the key
     * @param value the value
     * @param ttl   the time to live in seconds
     */
    @Override
    public void set(String key, String value, Long ttl) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        if (ttl != null) {
            valueOperations.set(key, value, ttl, TimeUnit.SECONDS);
        } else {
            valueOperations.set(key, value);
        }
    }

    /**
     * Get value by key
     *
     * @param key the key
     * @return the value
     */
    @Override
    public String get(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * Delete value by key
     *
     * @param key the key
     */
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
