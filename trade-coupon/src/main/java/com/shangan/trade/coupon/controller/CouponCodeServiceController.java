package com.shangan.trade.coupon.controller;

import com.shangan.trade.coupon.service.CouponCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CouponCodeServiceController {
    @Autowired
    private CouponCodeService couponCodeService;

    /**
     *  生成券码
     * @param batchId 批次号
     * @param count 生成的数量
     * @return
     */
    @RequestMapping("/coupon/createCouponCodeList")
    @ResponseBody
    List<String> createCouponCodeList(long batchId, int count){
        return  couponCodeService.createCouponCodeList(batchId,count);
    }

    /**
     * 使用优惠券码
     * @param userId
     * @param couponCode
     * @return
     */
    @RequestMapping("/coupon/userCouponCode")
    @ResponseBody
    boolean  userCouponCode(long userId ,String couponCode){
        return  couponCodeService.userCouponCode(userId,couponCode);
    }
}
