package com.shangan.trade.coupon.service;

import java.util.List;

public interface CouponCodeService {

    /**
     * 生成券码
     * @param batchId  批次号
     * @param count  生成的数量
     * @return
     */
    List<String> createCouponCodeList(long batchId, int count);

    /**
     * 使用优惠券码
     * @param userId
     * @param couponCode
     * @return
     */
    boolean useCouponCode(long userId, String couponCode);

    boolean userCouponCode(long userId, String couponCode);
}
