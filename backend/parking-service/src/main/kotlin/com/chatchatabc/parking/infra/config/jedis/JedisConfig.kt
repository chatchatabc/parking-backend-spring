package com.chatchatabc.parking.infra.config.jedis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JedisConfig(
    @Value("\${server.jedis.host}")
    private val host: String,

    @Value("\${server.jedis.port}")
    private val port: String,

    @Value("\${server.jedis.name}")
    private val name: String
) {

    /**
     * JedisPoolConfig
     */
//    @Bean
//    fun jedisPool(): JedisPool {
//        val jedisPoolConfig = JedisPoolConfig()
//        jedisPoolConfig.maxTotal = 10
//        jedisPoolConfig.maxIdle = 5
//        jedisPoolConfig.minIdle = 1
//        jedisPoolConfig.jmxNamePrefix = name
//        return JedisPool(jedisPoolConfig, host, port.toInt())
//    }
}