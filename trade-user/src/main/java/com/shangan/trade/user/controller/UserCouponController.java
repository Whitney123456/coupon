package com.shangan.trade.user.controller;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.user.model.Coupon;
import com.shangan.trade.user.model.CouponBatch;
import com.shangan.trade.user.model.CouponRule;
import com.shangan.trade.user.client.CouponFeignClient;
import com.shangan.trade.user.utils.RedisWorker;
import com.shangan.trade.user.vo.CouponVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class UserCouponController {

    @Autowired
    private CouponFeignClient couponFeignClient;

    @Autowired
    private RedisWorker redisWorker;

    /**
     * 跳转用户的优惠券列表页
     * @param userId
     * @return
     */
    @RequestMapping("/coupon/list/{userId}")
    public ModelAndView userCouponList(@PathVariable("userId") long userId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("coupon_list");

        List<Coupon> coupons = couponFeignClient.queryUserCoupons(userId);

        List<CouponVO> notUsedList = new ArrayList<>();
        List<CouponVO> usedList = new ArrayList<>();
        List<CouponVO> expiredList = new ArrayList<>();

        for (Coupon coupon : coupons) {
            CouponVO couponVo = createCouponVo(coupon);
            if (coupon.getStatus() == 0) {
                notUsedList.add(couponVo);
            } else if (coupon.getStatus() == 1) {
                usedList.add(couponVo);
            } else if (coupon.getStatus() == 2) {
                expiredList.add(couponVo);
            }
        }

        modelAndView.addObject("notUsedList", notUsedList);
        modelAndView.addObject("usedList", usedList);
        modelAndView.addObject("expiredList", expiredList);
        return modelAndView;
    }

    /**
     * 兑换优惠券码
     * @param userId
     * @param couponCode
     * @return
     */
    @RequestMapping("/coupon/useCouponCode/{userId}")
    public String useCouponCode(@PathVariable("userId") long userId,
                                @RequestParam("couponCode") String couponCode) {
        try {
            log.info("userCouponCode Req userId:{} couponCode:{}", userId, couponCode);
            couponFeignClient.userCouponCode(userId, couponCode);
            return "redirect:/coupon/list/" + userId;
        } catch (Exception e) {
            log.error("useCouponCode error", e);
            return e.getMessage();
        }
    }

    private CouponVO createCouponVo(Coupon coupon) {
        CouponVO couponVO = new CouponVO();
        String batchRule = redisWorker.getValue("couponBatchRule:" + coupon.getBatchId());
        if (StringUtils.isEmpty(batchRule)) {
            CouponBatch couponBatch = couponFeignClient.queryCouponBatchById(coupon.getBatchId());
            batchRule = couponBatch.getRule();
        }
        /**
         * 将JSON中的rule规则，转化成CouponRule对象
         *  {"couponType":1,"discountAmount":20,"endTime":1689855540000,"grantType":2,"startTime":1678969140000,"thresholdAmount":100}
         */
        CouponRule couponRule = JSON.parseObject(batchRule, CouponRule.class);

        if (couponRule.getCouponType() == 1) {
            // 满减券
            couponVO.setThresholdAmountStr("满" + couponRule.getThresholdAmount() + "元可用");
        }

        couponVO.setDiscountAmount(couponRule.getDiscountAmount());
        couponVO.setStatus(coupon.getStatus());
        Date startTime = couponRule.getStartTime();
        Date endTime = couponRule.getEndTime();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startTimeStr = dateFormat.format(startTime);
        String endTimeStr = dateFormat.format(endTime);
        couponVO.setStartTimeToEndTime(startTimeStr+"~"+endTimeStr);
        couponVO.setCouponName(coupon.getCouponName());
        return couponVO;
    }
}
