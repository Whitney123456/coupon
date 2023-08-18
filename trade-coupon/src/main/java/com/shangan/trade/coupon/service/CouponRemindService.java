package com.shangan.trade.coupon.service;

import com.shangan.trade.coupon.db.model.CouponRule;

/**
 * 发券提醒服务
 */
public interface CouponRemindService {
    /**
     * 新增券过期提醒任务
     *
     * @param userId
     * @param couponId
     * @param couponRule
     * @return
     */
    boolean insertCouponRemindTask(long userId, long couponId, CouponRule couponRule);


    /**
     * 执行定时提醒任务
     */
    void runCouponRemindTask();
}
