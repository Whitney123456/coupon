package com.shangan.trade.coupon.db.model;

import lombok.Data;

import java.util.Date;

@Data
public class RemindCouponTaskModel {

    private long userId;

    private long couponId;

    private Date remindDate;
}