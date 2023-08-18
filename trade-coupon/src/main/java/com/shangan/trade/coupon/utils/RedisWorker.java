package com.shangan.trade.coupon.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Service
public class RedisWorker {
    @Autowired
    private JedisPool jedisPool;

    /*
    * 向Redis中设置key-value值
    * @param key
    * @param value
    * */
    public void setValue(String key, String value) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.set(key, value);
        jedisClient.close();
    }

    /*
    * 根据key从Redis中获取对应的值
    * @param key
    * @return
    * */
    public String getValueByKey(String key) {
        Jedis jedisClient = jedisPool.getResource();
        String value = jedisClient.get(key);
        jedisClient.close();
        return value;
    }

    /*
     * 向Redis中设置key-value值
     * @param key
     * @param value
     * */
    public void setValue(String key, Long value) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.set(key, value.toString());
        jedisClient.close();
    }
}
