package com.shangan.trade.order.service;

public interface OrderService {
    /**
     * 创建订单
     * @param userId
     * @param couponId
     * @param orderId
     */
    void  createOrder(long userId,long couponId,long orderId);

    /**
     * 支付成功
     * @param userId
     * @param couponId
     * @param orderId
     */
    void  paySuccess(long userId,long couponId,long orderId);

    /**
     * 关闭订单
     * @param userId
     * @param couponId
     * @param orderId
     */
    void closeOrder(long userId,long couponId,long orderId);
}
