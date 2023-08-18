package com.shangan.trade.coupon.service.impl;

import com.shangan.trade.coupon.db.dao.CouponDao;
import com.shangan.trade.coupon.db.model.Coupon;
import com.shangan.trade.coupon.service.UseCouponTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class UseCouponTransactionServiceImpl implements UseCouponTransactionService {
    @Autowired
    private CouponDao couponDao;

    @Override
    public boolean tryCoupon(long userId, long couponId, long orderId) {
        Coupon coupon = couponDao.queryCouponById(couponId);
        // 检查这个优惠券是否存在
        if (coupon == null) {
            log.error("coupon not exist couponId:{}", couponId);
            return false;
        }

        /**
         * 判断券是否已经使用了
         * 0-未使用; 1-已使用; 2-已过期; 3-冻结
         */
        if (coupon.getStatus() != 0) {
            log.error("coupon not exist couponId:{}", couponId);
            return false;
        }

        /**
         * 1. 把券的状态改为冻结状态
         * 2. 记录对应的订单号
         */
        coupon.setStatus(3);
        coupon.setOrderId(orderId);

        boolean updateRes = couponDao.updateCoupon(coupon);
        if (updateRes) {
            log.info("coupon lock success couponId:{}", couponId);
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelCoupon(long userId, long couponId, long orderId) {
        log.info("cancelCoupon couponId:{}", couponId);
        Coupon coupon = couponDao.queryCouponById(couponId);
        // 检查这个优惠券是否存在
        if (coupon == null) {
            log.error("coupon not exist couponId:{}", couponId);
            return false;
        }
        // 把订单状态从冻结改为未使用， 把订单对应的的OrderId设置为空
        coupon.setStatus(0);
        coupon.setOrderId(0L);
        couponDao.updateCoupon(coupon);
        return true;
    }

    @Override
    public boolean commitCoupon(long userId, long couponId, long orderId) {
        log.info("commitCoupon couponId:{}", couponId);
        Coupon coupon = couponDao.queryCouponById(couponId);
        // 检查这个优惠券是否存在
        if (coupon == null) {
            log.error("coupon not exist couponId:{}", couponId);
            return false;
        }
        coupon.setStatus(1);
        coupon.setUsedTime(new Date());
        // 把订单状态改成已使用，同时设置上使用的时间
        couponDao.updateCoupon(coupon);
        return true;
    }
}
