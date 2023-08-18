package com.shangan.trade.coupon.controller;

import com.shangan.trade.coupon.mq.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class TestController {

    @Autowired
    private MessageSender messageSender;

    @RequestMapping("/sendMessage/{message}")
    @ResponseBody
    public String sendMessage(@PathVariable("message") String message) {
        try {
            messageSender.sendMessage("test-topic", message);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "发送消息:" + message;
    }
}
