package com.shangan.trade.coupon.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shangan.trade.coupon.db.dao.IdempotentTaskDao;
import com.shangan.trade.coupon.db.dao.TaskDao;
import com.shangan.trade.coupon.db.model.SendCouponTaskModel;
import com.shangan.trade.coupon.db.model.Task;
import com.shangan.trade.coupon.service.CouponRemindService;
import com.shangan.trade.coupon.service.CouponSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration  //1.主要用于标记配置类
@EnableScheduling   //2.开启定时任务
public class ScheduleTask {
    @Autowired
    private CouponSendService couponSendService;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private IdempotentTaskDao idempotentTaskDao;

    @Autowired
    private CouponRemindService couponRemindService;

//    //3.添加定时任务
//    @Scheduled(cron = "0/5 * * * * ?")
//    private void configureTasks() {
//        System.err.println("执行定时任务时间:" + LocalDateTime.now());
//    }

    /**
     * 发券失败任务重试
     */
    @Scheduled(cron = "0/30 * * * * ?")
    private void couponFailedTaskRetry() {
        System.err.println("执行定时任务时间:" + LocalDateTime.now());
        List<Task> tasks = taskDao.queryFailedTasks();
        for (Task task : tasks) {
            processFailedTask(task);
        }
    }

    /**
     * 发券提醒任务
     */
    @Scheduled(cron = "0/30 * * * * ?")
    private void couponRemindTask() {
        System.err.println("" + LocalDateTime.now());
        couponRemindService.runCouponRemindTask();
    }
    public void processFailedTask(Task task) {
        log.info("processFailedTask task:{}", JSON.toJSONString(task));
        SendCouponTaskModel sendCouponTaskModel = JSONObject.parseObject(task.getBizParam(), SendCouponTaskModel.class);
        //给用户发券
        boolean result = couponSendService.sendUserCouponSynWithLock(sendCouponTaskModel.getBatchId(), sendCouponTaskModel.getUserId());
        //更新任务的状态(1.失败了 重试次数+1 2.成功了 把状态改为已完成)
        if (result) {
            //成功了 把状态改为已完成
            task.setStatus(1);
            log.info("consumeBatchCouponMessage success sendCouponTaskModel:{}", task.getBizParam());
        } else {
            //失败了 重试次数+1
            task.setStatus(2);
            task.setTryCount(task.getTryCount() + 1);
            log.info("consumeBatchCouponMessage failed sendCouponTaskModel:{}", task.getBizParam());
        }
        task.setModifiedTime(new Date());
        taskDao.update(task);
    }
}
