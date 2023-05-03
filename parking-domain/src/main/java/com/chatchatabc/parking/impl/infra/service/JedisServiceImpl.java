package com.chatchatabc.parking.impl.infra.service;

import com.chatchatabc.parking.infra.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class JedisServiceImpl implements JedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Set key value to redis
     *
     * @param key   the key
     * @param value the value
     * @param ttl   the ttl
     */
    @Override
    public void set(String key, String value, Long ttl) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        if (ttl != null) {
            valueOperations.set(key, value, ttl, TimeUnit.SECONDS);
        } else {
            valueOperations.set(key, value);
        }
    }

    /**
     * Get value from redis
     *
     * @param key the key
     * @return the value
     */
    @Override
    public String get(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return (String) valueOperations.get(key);
    }

    /**
     * Delete key from redis
     *
     * @param key the key
     */
    @Override
    public void del(String key) {
        redisTemplate.delete(key);
    }
}
