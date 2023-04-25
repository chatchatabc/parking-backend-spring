package com.chatchatabc.parking.infra.service;

public interface JedisService {

    /**
     * Set key value to redis
     *
     * @param key   the key
     * @param value the value
     * @param ttl   the ttl
     */
    void set(String key, String value, Long ttl);

    /**
     * Get value from redis
     *
     * @param key the key
     * @return the value
     */
    String get(String key);

    /**
     * Delete key from redis
     *
     * @param key the key
     */
    void del(String key);
}
