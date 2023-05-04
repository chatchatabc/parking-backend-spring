package com.chatchatabc.parking.infra.service;

public interface KVService {

    /**
     * Set key value pair
     *
     * @param key   the key
     * @param value the value
     * @param ttl   the time to live in seconds
     */
    void set(String key, String value, Long ttl);

    /**
     * Get value by key
     *
     * @param key the key
     * @return the value
     */
    String get(String key);

    /**
     * Delete value by key
     *
     * @param key the key
     */
    void delete(String key);
}
