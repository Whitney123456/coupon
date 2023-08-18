package com.shangan.trade.coupon.service.impl;

import com.shangan.trade.coupon.db.dao.CouponDao;
import com.shangan.trade.coupon.db.model.Coupon;
import com.shangan.trade.coupon.service.CouponQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponQueryServiceImpl implements CouponQueryService {

    @Autowired
    private CouponDao couponDao;

    /**
     * @param userId
     * @return
     */
    @Override
    public List<Coupon> queryUserCoupons(long userId) {
        return couponDao.queryUserCoupons(userId);
    }

    @Override
    public List<Coupon> queryUsrCoupons(long userId) {
        return null;
    }
}
