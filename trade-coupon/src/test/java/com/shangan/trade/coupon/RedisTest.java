package com.shangan.trade.coupon;

import com.shangan.trade.coupon.utils.RedisLockUtil;
import com.shangan.trade.coupon.utils.RedisWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisWorker redisWorker;

    @Autowired
    private RedisLockUtil redisLock;

    @Test
    public void setValueTest() {
        redisWorker.setValue("name", "hello world");
    }

    @Test
    public void getValueTest() {
        String value = redisWorker.getValueByKey("name");
        System.out.println(value);
    }

    @Test
    public void redisLockTest() {
        boolean result1 = redisLock.tryGetLock("redisLock", UUID.randomUUID().toString(), 1000 * 100);
        System.out.println("boolean result1=" + result1);
        boolean result2 = redisLock.tryGetLock("redisLock", UUID.randomUUID().toString(), 1000 * 100);
        System.out.println("boolean result2=" + result2);
    }
}
