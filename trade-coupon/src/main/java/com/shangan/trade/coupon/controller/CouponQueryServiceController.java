package com.shangan.trade.coupon.controller;

import com.shangan.trade.coupon.db.model.Coupon;
import com.shangan.trade.coupon.service.CouponQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CouponQueryServiceController {
    @Autowired
    private CouponQueryService couponQueryService;

    @RequestMapping("/coupon/queryUserCoupons")
    @ResponseBody
    List<Coupon> queryUserCoupons(long userId){
        return couponQueryService.queryUsrCoupons(userId);
    }
}
