package com.chatchatabc.parking.impl.infra.service;

import com.chatchatabc.parking.infra.service.JedisService;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;

public class JedisServiceImpl implements JedisService {
    @Value("${server.jedis.host}")
    private String host;

    @Value("${server.jedis.port}")
    private String port;

    /**
     * Set key value to redis
     *
     * @param key   the key
     * @param value the value
     * @param ttl   the ttl
     */
    @Override
    public void set(String key, String value, Long ttl) {
        // TODO: Fix this with Jedis Pool
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        if (ttl != null) {
            jedis.setex(key, ttl, value);
        } else {
            jedis.set(key, value);
        }
        jedis.close();
    }

    /**
     * Get value from redis
     *
     * @param key the key
     * @return the value
     */
    @Override
    public String get(String key) {
        // TODO: Fix this with Jedis Pool
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    /**
     * Delete key from redis
     *
     * @param key the key
     */
    @Override
    public void del(String key) {
        // TODO: Fix this with Jedis Pool
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        jedis.del(key);
        jedis.close();
    }
}
