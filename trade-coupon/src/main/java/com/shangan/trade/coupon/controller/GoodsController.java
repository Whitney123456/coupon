package com.shangan.trade.coupon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GoodsController {

    @GetMapping("/coupon/test")
    @ResponseBody
    public String hello() {
        return "Hello World!";
    }
}
