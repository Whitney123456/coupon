package com.shangan.trade.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shangan.trade.coupon.db.dao.CouponDao;
import com.shangan.trade.coupon.db.dao.TaskDao;
import com.shangan.trade.coupon.db.model.Coupon;
import com.shangan.trade.coupon.db.model.CouponRule;
import com.shangan.trade.coupon.db.model.RemindCouponTaskModel;
import com.shangan.trade.coupon.db.model.Task;
import com.shangan.trade.coupon.service.CouponRemindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CouponRemindServiceImpl implements CouponRemindService {

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private TaskDao taskDao;

    @Override
    public boolean insertCouponRemindTask(long userId, long couponId, CouponRule couponRule) {
        Task task = new Task();
        task.setStatus(0);
        task.setTryCount(0);
        task.setBizType("remind_coupon");
        task.setBizId(UUID.randomUUID().toString());
        RemindCouponTaskModel model = new RemindCouponTaskModel();
        model.setUserId(userId);
        model.setCouponId(couponId);
        // 先写今天的时间，为了方便演示
        model.setRemindDate(new Date());
        task.setBizParam(JSON.toJSONString(model));
        task.setScheduleTime(new Date());
        task.setModifiedTime(new Date());
        task.setCreateTime(new Date());
        log.info("insertCouponRemindTask, task:{}", JSON.toJSONString(task));
        return taskDao.insertTask(task);
    }

    @Override
    public void runCouponRemindTask() {
        List<Task> tasks = taskDao.queryRemindTasks();
        for (Task task : tasks) {
            log.info("runCouponRemindTask task:{}", JSON.toJSONString(task));
            RemindCouponTaskModel remindCouponTaskModel = JSONObject.parseObject(task.getBizParam(), RemindCouponTaskModel.class);
            //1.查询这个券是否已经使用
            Coupon coupon = couponDao.queryCouponById(remindCouponTaskModel.getCouponId());
            if (coupon == null) {
                log.error("该优惠券不存在 couponId={}", remindCouponTaskModel.getCouponId());
                continue;
            }
            //0 表示为使用状态，不等于0的时候直接未执行
            if (coupon.getStatus() != 0) {
                continue;
            }
            //2.给用户发提醒，发一个push
            log.info("还有3天券过期了，抓紧时间使用......");

            //3.更新这个状态
            task.setStatus(1);
            task.setTryCount(task.getTryCount() + 1);
            taskDao.update(task);
        }
    }
}
