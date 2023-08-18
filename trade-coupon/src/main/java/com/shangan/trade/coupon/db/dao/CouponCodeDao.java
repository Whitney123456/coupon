package com.shangan.trade.coupon.db.dao;

import com.shangan.trade.coupon.db.model.CouponCode;

public interface CouponCodeDao {
    /**
     * 插入一条记录
     * @param couponCode
     * @return
     */
    boolean insertCouponCode(CouponCode couponCode);

    /**
     * 通过券码查询对应的券码信息
     * @param code
     * @return
     */
    CouponCode queryCouponByCode(String code);

    /**
     * 更新券码信息
     * @param couponCode
     * @return
     */
    boolean updateCouponCode(CouponCode couponCode);
}
