package com.shangan.trade.coupon.controller;

import com.shangan.trade.coupon.db.model.CouponBatch;
import com.shangan.trade.coupon.service.CouponBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CouponBatchServiceController {

    @Autowired
    private CouponBatchService couponBatchService;

    /**
     * 新增一个券批次
     *
     * @param couponBatch
     * @return
     */
    @PostMapping("/coupon/insertCouponBatch")
    @ResponseBody
    boolean insertCouponBatch(@RequestBody CouponBatch couponBatch) {
        return couponBatchService.insertCouponBatch(couponBatch);
    }

    /**
     * 查询券批次列表
     *
     * @return
     */
    @RequestMapping("/coupon/queryCouponBatchList")
    @ResponseBody
    List<CouponBatch> queryCouponBatchList() {
        return couponBatchService.queryCouponBatchList();
    }

    /**
     * 将批次信息的规则信息push到Redis中
     *
     * @return
     */
    @RequestMapping("/coupon/pushBatchListRuleToCache")
    @ResponseBody
    boolean pushBatchListRuleToCache() {
        return couponBatchService.pushBatchListRuleToCache();
    }

    /**
     * 根据ID查询对应的券批次
     *
     * @param id
     * @return
     */
    @RequestMapping("/coupon/queryCouponBatchById")
    @ResponseBody
    CouponBatch queryCouponBatchById(long id) {
        return couponBatchService.queryCouponBatchById(id);
    }
}
