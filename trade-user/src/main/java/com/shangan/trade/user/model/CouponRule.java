package com.shangan.trade.user.model;

import lombok.Data;

import java.util.Date;

/**
 * 优惠券规则信息
 */
@Data
public class CouponRule {

    /**
     * 优惠券类型：1-满减，2-满折，3-直减
     */
    private int couponType;

    /**
     * 发放类型：1-用户领取，2-系统自动发放，3-兑换码兑换
     */
    private int grantType;

    /**
     * 优惠券使用有效期开始时间
     */
    private Date startTime;

    /**
     * 优惠券使用有效期结束时间
     */
    private Date endTime;

    /**
     * 优惠券使用的门槛金额
     */
    private int thresholdAmount;

    /**
     * 优惠券使用的优惠金额
     */
    private int discountAmount;
}
