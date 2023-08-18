package com.shangan.trade.coupon.service;

/**
 * 定义TCC接口
 * try
 * commit
 * cancel
 */
public interface UseCouponTransactionService {
    /**
     * 冻结优惠券
     * @param userId
     * @param couponId
     * @param orderId
     * @return
     */
    boolean tryCoupon(long userId, long couponId, long orderId);

    /**
     * 业务回滚
     *      (把在tryCoupon 中冻结的券，恢复到未使用)
     * @param userId
     * @param couponId
     * @param orderId
     * @return
     */
    boolean cancelCoupon(long userId, long couponId, long orderId);

    /**
     * 把已经冻结的券核销(把订单状态改成已使用)
     * @param userId
     * @param couponId
     * @param orderId
     * @return
     */
    boolean commitCoupon(long userId, long couponId, long orderId);
}
