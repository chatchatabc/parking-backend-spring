package com.chatchatabc.service.infra.service

import org.springframework.stereotype.Service

@Service
interface JedisService {
    /**
     * Set key-value to Redis
     */
    fun set(key: String, value: String, ttl: Long?)

    /**
     * Get value from Redis
     */
    fun get(key: String): String?

    /**
     * Delete key from Redis
     */
    fun del(key: String)
}