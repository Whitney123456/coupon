package com.shangan.trade.order.service.impl;

import com.shangan.trade.coupon.service.UseCouponTransactionService;
import com.shangan.trade.order.service.OrderService;
import com.shangan.trade.goos2.service.GoodsTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private GoodsTransactionService goodsTransactionService;

    @Autowired
    private UseCouponTransactionService useCouponTransactionService;

    @Override
    public void createOrder(long userId, long couponId, long orderId) {
        // 1.try 先文档库存
        boolean tryGoodsStockResult = goodsTransactionService.tryGoodsStock();
        // 2. try 锁定优惠
        boolean tryGouponResult = useCouponTransactionService.tryCoupon(userId, couponId, orderId);
        // 3.
        if (!tryGoodsStockResult || !tryGouponResult) {
            log.error("rollback tryGoodsStockResult:{}, tryCouponResult:{}", tryGouponResult, tryGouponResult);
            goodsTransactionService.cancleGoodsStock();
            useCouponTransactionService.cancelCoupon(userId, couponId, orderId);
        }

        log.info("order create success waiting for pay...");
    }

    @Override
    public void paySuccess(long userId, long couponId, long orderId) {
        goodsTransactionService.commitGoodsStock();
        useCouponTransactionService.commitCoupon(userId, couponId, orderId);
    }

    @Override
    public void closeOrder(long userId, long couponId, long orderId) {
        // 关闭订单的时候，要把前面所有try 的资源都回滚掉
        goodsTransactionService.cancleGoodsStock();
        useCouponTransactionService.cancelCoupon(userId, couponId, orderId);
    }
}
