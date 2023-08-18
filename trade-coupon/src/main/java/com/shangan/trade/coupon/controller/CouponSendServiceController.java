package com.shangan.trade.coupon.controller;

import com.shangan.trade.coupon.service.CouponSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
public class CouponSendServiceController {

    @Autowired
    private CouponSendService couponSendService;

    /**
     * 同步发券
     *
     * @param batchId
     * @param userId
     * @return
     */
    @RequestMapping("/coupon/sendUserCouponSyn")
    @ResponseBody
    Boolean sendUserCouponSyn(long batchId, long userId){
        return couponSendService.sendUserCouponSyn(batchId,userId);
    }

    /**
     * 同步发券(使用了分布式锁)
     *
     * @param batchId
     * @param userId
     * @return
     */
    @RequestMapping("/coupon/sendUserCouponSynWithLock")
    @ResponseBody
    boolean sendUserCouponSynWithLock(long batchId, long userId){
        return couponSendService.sendUserCouponSynWithLock(batchId,userId);
    }

    /**
     * 批量给用户发放优惠券
     *
     * @param batchId
     * @param userIdSet
     * @return
     */
    @RequestMapping("/coupon/sendUserCouponBatch")
    @ResponseBody
    boolean sendUserCouponBatch(long batchId, Set<Long> userIdSet){
        return couponSendService.sendUserCouponBatch(batchId,userIdSet);
    }
}
