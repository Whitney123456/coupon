package com.shangan.trade.coupon.service;

import com.shangan.trade.coupon.db.model.Coupon;

import java.util.List;

/**
 * 优惠券查询服务
 */
public interface CouponQueryService {
    List<Coupon> queryUserCoupons(long userId);

    List<Coupon> queryUsrCoupons(long userId);
}
