package com.chatchatabc.parking.api.impl.application.service

import com.chatchatabc.parking.api.application.service.KVService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class KVServiceRedisImpl(
    private val redisTemplate: RedisTemplate<String, String>
) : KVService {

    /**
     * Set key value pair
     */
    override fun set(key: String, value: String, ttl: Long?) {
        val ops = redisTemplate.opsForValue()
        if (ttl != null) {
            ops.set(key, value, ttl, TimeUnit.SECONDS)
        } else {
            ops.set(key, value)
        }
    }

    /**
     * Get value by key
     */
    override fun get(key: String): String? {
        val ops = redisTemplate.opsForValue()
        return ops.get(key)
    }

    /**
     * Delete key value pair
     */
    override fun delete(key: String) {
        redisTemplate.delete(key)
    }
}