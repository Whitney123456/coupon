package com.shangan.trade.user.vo;

import lombok.Data;

@Data
public class CouponVO {

    private Long id;

    private Long userId;

    private Long batchId;

    private String couponName;

    private Integer status;

    /**
     * 优惠券类型：1-满减，2-满折，3-直减
     */
    private int couponType;

    /**
     * 发放类型：1-用户领取，2-系统自动发放，3-兑换码兑换
     */
    private int grantType;

    /**
     * 优惠券使用的门槛金额文案
     */
    private String  thresholdAmountStr;


    /**
     * 优惠券使用的优惠金额
     */
    private int discountAmount;

    /**
     * 页面上展示的有效范围
     */
    private String  startTimeToEndTime;
}
