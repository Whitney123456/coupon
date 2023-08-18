package com.shangan.trade.admin.client;

import com.shangan.trade.admin.model.CouponBatch;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

//FeignClient注解设置服务提供者名字，在consul注册中心注册的服务名字
@FeignClient(name = "trade-coupon")
public interface CouponFeignClient {

    /**
     * 新增一个券批次
     *
     * @param couponBatch
     * @return
     */
    @RequestMapping("/coupon/insertCouponBatch")
    boolean insertCouponBatch(@RequestBody CouponBatch couponBatch);

    /**
     * 查询券批次列表
     *
     * @return
     */
    @RequestMapping("/coupon/queryCouponBatchList")
    List<CouponBatch> queryCouponBatchList();

    /**
     * 将批次信息的规则信息push到Redis中
     *
     * @return
     */
    @RequestMapping("/coupon/pushBatchListRuleToCache")
    boolean pushBatchListRuleToCache();


    /**
     * 同步发券
     *
     * @param batchId
     * @param userId
     * @return
     */
    @RequestMapping("/coupon/sendUserCouponSyn")
    boolean sendUserCouponSyn(@RequestParam("batchId") long batchId, @RequestParam("userId") long userId);

    /**
     * 同步发券(使用了分布式锁)
     *
     * @param batchId
     * @param userId
     * @return
     */
    @RequestMapping("/coupon/sendUserCouponSynWithLock")
    boolean sendUserCouponSynWithLock(@RequestParam("batchId") long batchId, @RequestParam("userId") long userId);


    /**
     * 批量给用户发放优惠券
     *
     * @param batchId
     * @param userIdSet
     * @return
     */
    @RequestMapping("/coupon/sendUserCouponBatch")
    boolean sendUserCouponBatch(@RequestParam("batchId") long batchId, @RequestParam("userIdSet") Set<Long> userIdSet);

}
