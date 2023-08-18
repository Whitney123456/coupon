package com.shangan.trade.coupon.mq;

import com.alibaba.fastjson.JSONObject;
import com.shangan.trade.coupon.db.dao.IdempotentTaskDao;
import com.shangan.trade.coupon.db.dao.TaskDao;
import com.shangan.trade.coupon.db.model.IdempotentTask;
import com.shangan.trade.coupon.db.model.SendCouponTaskModel;
import com.shangan.trade.coupon.db.model.Task;
import com.shangan.trade.coupon.exceptions.BizException;
import com.shangan.trade.coupon.service.CouponSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MessageReceiver {

    @Autowired
    private CouponSendService couponSendService;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private IdempotentTaskDao idempotentTaskDao;

    @KafkaListener(topics = {"test-topic"})
    public void consumeMessage(String message) {
        log.info("接收到消息, message:{}", message);
    }

    @KafkaListener(topics = {"send-batch-coupon"})
    public void consumeBatchCouponMessage(String message) {
        log.info("接收到 send-batch-coupon message:{}", message);
        //step1:解析消息内容
        Task task = JSONObject.parseObject(message, Task.class);
        SendCouponTaskModel sendCouponTaskModel = JSONObject.parseObject(task.getBizParam(), SendCouponTaskModel.class);
        try {
            //step2:查询幂等表，校验当前的这个业务是否执行执行被执行过了
            IdempotentTask idempotentTaskQuery = idempotentTaskDao.queryIdempotentTaskByBiz(task.getBizType(), task.getBizId());
            if (idempotentTaskQuery != null) {
                //不为空，说明已经执行过了 （消息之前已经被消费过了），不往下继续执行
                log.error("message is consumed  message:{}", message);
                return;
            }

            //step3:给用户发券
            boolean result = couponSendService.sendUserCouponSynWithLock(sendCouponTaskModel.getBatchId(), sendCouponTaskModel.getUserId());
            //step4:更新任务的状态（1.失败了 重试次数+1 2.成功了 把状态改为已完成）
            if (result) {
                // 成功了 把状态改为已完成
                task.setStatus(1);
                log.info("consumeBatchCouponMessage success sendCouponTaskModel:{}", task.getBizParam());

                //当前的这个业务已经执行，存到幂等表中
                IdempotentTask idempotentTask = new IdempotentTask();
                idempotentTask.setBizType(task.getBizType());
                idempotentTask.setBizId(task.getBizId());
                idempotentTask.setCreateTime(new Date());

                //step5:消费过的消息，存入幂等表中
                idempotentTaskDao.insertIdempotentTask(idempotentTask);
            } else {
                throw new BizException("sendUserCouponSynWithLock error");
            }
        } catch (Exception ex) {
            // 失败了 重试次数+1
            task.setStatus(2);
            task.setTryCount(task.getTryCount() + 1);
            log.info("consumeBatchCouponMessage failed sendCouponTaskModel:{} ", task.getBizParam(), ex);
        }

        task.setModifiedTime(new Date());
        taskDao.update(task);
    }

}
