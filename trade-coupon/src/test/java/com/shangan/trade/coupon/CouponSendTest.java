package com.shangan.trade.coupon;

import com.shangan.trade.coupon.service.CouponSendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CouponSendTest {

    @Autowired
    private CouponSendService couponSendService;

    @Test
    public void testSendUserCouponSyn() {
        couponSendService.sendUserCouponSyn(8, 1);
    }
}
