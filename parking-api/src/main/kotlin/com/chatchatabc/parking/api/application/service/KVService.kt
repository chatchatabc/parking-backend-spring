package com.chatchatabc.parking.api.application.service

interface KVService {

    /**
     * Set key value pair
     */
    fun set(key: String, value: String, ttl: Long?)

    /**
     * Get value by key
     */
    fun get(key: String): String?

    /**
     * Delete key value pair
     */
    fun delete(key: String)
}