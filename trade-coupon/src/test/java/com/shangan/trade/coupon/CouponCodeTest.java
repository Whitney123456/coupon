package com.shangan.trade.coupon;

import com.shangan.trade.coupon.db.dao.CouponCodeDao;
import com.shangan.trade.coupon.db.model.CouponCode;
import com.shangan.trade.coupon.service.CouponCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CouponCodeTest {
    @Autowired
    private CouponCodeDao couponCodeDao;

    @Autowired
    private CouponCodeService couponCodeService;

    @Test
    public void createCodeTest() {
        List<String> couponCodeList = couponCodeService.createCouponCodeList(9, 10);
        for (String code : couponCodeList) {
            System.out.println(code);
        }
    }

//    @Test
//    public void insertCouponCodeTest() {
//        CouponCode couponCode = new CouponCode();
//        couponCode.setCode("abcd");
//        couponCode.setBatchId(10L);
//        couponCode.setUserId(123L);
//        couponCode.setStatus(1);
//        couponCode.setCreateTime(new Date());
//        couponCode.setModifyTime(new Date());
//        couponCodeDao.insertCouponCode(couponCode);
//    }
}
