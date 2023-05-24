package com.chatchatabc.parking.impl.infra.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class KVServiceRedisImplTest extends TestContainersBaseTest {
    @Autowired
    private KVServiceRedisImpl kvServiceRedis;

    @Test
    void testSet_ShouldSetKVToRedis() {
        String key = "testKey";
        String value = "testValue";
        kvServiceRedis.set(key, value, 900L);
        assertEquals(value, kvServiceRedis.get(key));
    }

    @Test
    void testGet_ShouldGetKVFromRedis() {
        String key = "testKey";
        String value = "testValue";
        kvServiceRedis.set(key, value, 900L);
        assertEquals(value, kvServiceRedis.get(key));
    }

    @Test
    void testDelete_ShouldDeleteKVFromRedis() {
        String key = "testKey";
        String value = "testValue";
        kvServiceRedis.set(key, value, 900L);
        kvServiceRedis.delete(key);
        assertNull(kvServiceRedis.get(key));
    }
}