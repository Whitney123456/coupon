package com.shangan.trade.coupon.service;

import java.util.Set;

public interface CouponSendService {
    /*
    * 同步发券
    * @param batchId
    * @param userId
    * @return
    * */
    boolean sendUserCouponSyn(long batchId, long userId);

    /**
     * 同步发券(使用了分布式锁)
     * @param batchId
     * @param userId
     * @return
     */
    boolean sendUserCouponSynWithLock(long batchId, long userId);

    /**
     * 批量给用户发放优惠券
     * @param batchId
     * @param userIdSet
     * @return
     */
    boolean sendUserCouponBatch(long batchId, Set<Long> userIdSet);
}
