package com.shangan.trade.coupon.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

@Slf4j
@Service
public class RedisLockUtil {
    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final int MAX_TRY_COUNT = 10;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryGetLock(String lockKey, String requestId, int expireTime) {
        Jedis jedisClient = jedisPool.getResource();
        //重试的次数
        int tryCount = 0;
        SetParams setParams = SetParams.setParams().ex(expireTime);

        try {
            do {
                String result = jedisClient.set(lockKey, requestId, setParams);
                if (LOCK_SUCCESS.equals(result)) {
                    return true;
                }
                Thread.sleep(100);
                tryCount++;
                // 当第一次加锁失败后，再次尝试获取锁，这里不能超过最大的次数
            } while (tryCount < MAX_TRY_COUNT);
            log.error("tryLock error,lockKey:{}", lockKey);
        } catch (Throwable e) {
            log.error("tryLock error,lockKey:{}", lockKey, e);
        } finally {
            jedisClient.close();
        }
        return false;
    }

    /**
     * 解锁
     * 用lua 脚本保证 一致性
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return
     */
    public boolean releaseLock(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Jedis jedis = jedisPool.getResource();
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        jedis.close();
        if (RELEASE_SUCCESS.equals(result))
            return true;
        return false;
    }
}
