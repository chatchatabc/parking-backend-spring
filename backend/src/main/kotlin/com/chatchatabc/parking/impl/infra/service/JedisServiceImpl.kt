package com.chatchatabc.parking.impl.infra.service

import com.chatchatabc.parking.infra.service.JedisService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import redis.clients.jedis.Jedis

@Service
class JedisServiceImpl (
    @Value("\${server.jedis.host}")
    private val host: String,

    @Value("\${server.jedis.port}")
    private val port: String,
) : JedisService {
    /**
     * Set key-value to Redis
     */
    override fun set(key: String, value: String, ttl: Long?) {
        // TODO: Fix this with Jedis Pool
        val jedis = Jedis(host, port.toInt())
        if (ttl != null) {
            jedis.setex(key, ttl, value)
        } else {
            jedis.set(key, value)
        }
        jedis.close()
    }

    /**
     * Get value from Redis
     */
    override fun get(key: String): String? {
        // TODO: Fix this with Jedis Pool
        val jedis = Jedis(host, port.toInt())
        val value = jedis.get(key)
        jedis.close()
        return value
    }

    /**
     * Delete key from Redis
     */
    override fun del(key: String) {
        // TODO: Fix this with Jedis Pool
        val jedis = Jedis(host, port.toInt())
        jedis.del(key)
        jedis.close()
    }
}