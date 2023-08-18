package com.shangan.trade.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.coupon.db.dao.CouponBatchDao;
import com.shangan.trade.coupon.db.dao.TaskDao;
import com.shangan.trade.coupon.db.model.CouponBatch;
import com.shangan.trade.coupon.db.model.SendCouponTaskModel;
import com.shangan.trade.coupon.db.model.Task;
import com.shangan.trade.coupon.mq.MessageSender;
import com.shangan.trade.coupon.service.CouponBatchService;
import com.shangan.trade.coupon.utils.RedisWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class CouponBatchServiceImpl implements CouponBatchService {
    @Autowired
    private CouponBatchDao couponBatchDao;

    @Autowired
    private RedisWorker redisWorker;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private TaskDao taskDao;

    @Override
    public boolean insertCouponBatch(CouponBatch couponBatch) {
        return couponBatchDao.insertCouponBatch(couponBatch);
    }

    @Override
    public List<CouponBatch> queryCouponBatchList() {
        return couponBatchDao.queryCouponBatchList();
    }

    /**
     * 将批次信息的规则信息push到Redis中
     */
    @Override
    public boolean pushBatchListRuleToCache() {
        List<CouponBatch> couponBatches = couponBatchDao.queryCouponBatchList();
        for (CouponBatch couponBatch : couponBatches) {
            redisWorker.setValue("couponBatchRule:" + couponBatch.getId(), couponBatch.getRule());
        }
        return true;
    }

    @Override
    public boolean sendUserCouponBatch(long batchId, Set<Long> userIdSet) {
        for (Long userId : userIdSet) {
            // 插入任务表
            Task task = new Task();
            task.setStatus(0);
            task.setTryCount(0);
            task.setBizType("send_coupon");
            // UUID
            task.setBizId(UUID.randomUUID().toString());

            SendCouponTaskModel sendCouponTaskModel = new SendCouponTaskModel();
            sendCouponTaskModel.setBatchId(batchId);
            sendCouponTaskModel.setUserId(userId);
            task.setBizParam(JSON.toJSONString(sendCouponTaskModel));
            task.setModifiedTime(new Date());
            task.setCreateTime(new Date());

            boolean res = taskDao.insertTask(task);
            if (res) {
                // 插入任务记录，成功再发送消息
                messageSender.sendMessage("send-batch-coupon", JSON.toJSONString(task));
            } else {
                log.error("insert task table error userId:{} batchId:{}", userId, batchId);
            }
        }

        return false;
    }

    @Override
    public CouponBatch queryCouponBatchById(long id) {
        return couponBatchDao.queryCouponBatchById(id);
    }


}
