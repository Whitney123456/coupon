package com.shangan.trade.coupon.exceptions;

/*
* 自定义的业务异常
* */
public class BizException extends RuntimeException {
    public BizException(String message) {
        super(message);
    }
}
