package com.shangan.trade.coupon.db.dao;

import com.shangan.trade.coupon.db.model.Coupon;

import java.util.List;

public interface CouponDao {
    /*
    * 新增一张券
    * @param coupon
    * @return
    * */
    boolean insertCoupon(Coupon coupon);

    /**
     * 查询用户的优惠券
     * @param userId
     * @return
     */
    List<Coupon> queryUserCoupons(long userId);

    /**
     * 通过券的id查询对应的券
     * @param couponId
     * @return
     */
    Coupon queryCouponById(long couponId);

    /**
     * 更新券的信息
     * @param coupon
     * @return
     */
    boolean updateCoupon(Coupon coupon);

}
