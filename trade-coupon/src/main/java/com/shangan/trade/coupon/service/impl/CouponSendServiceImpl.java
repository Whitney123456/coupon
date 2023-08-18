package com.shangan.trade.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.util.StringUtils;
import com.shangan.trade.coupon.db.dao.CouponBatchDao;
import com.shangan.trade.coupon.db.dao.CouponDao;
import com.shangan.trade.coupon.db.dao.TaskDao;
import com.shangan.trade.coupon.db.mappers.CouponBatchMapper;
import com.shangan.trade.coupon.db.model.*;
import com.shangan.trade.coupon.exceptions.BizException;
import com.shangan.trade.coupon.mq.MessageSender;
import com.shangan.trade.coupon.service.CouponRemindService;
import com.shangan.trade.coupon.service.CouponSendService;
import com.shangan.trade.coupon.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class CouponSendServiceImpl implements CouponSendService {

    @Autowired
    private CouponBatchMapper couponBatchMapper;

    @Autowired
    private CouponBatchDao couponBatchDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private CouponRemindService couponRemindService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean sendUserCouponSyn(long batchId, long userId) {
        //1.查询批次信息
        CouponBatch couponBatch = couponBatchMapper.selectByPrimaryKey(batchId);
        //2.判断该batchId，对应的券批次信息是否存储在
        if (couponBatch == null) {
            log.error("sendUserCouponSyn error batchId:{} userId:{}", batchId, userId);
            throw new BizException("批次信息不存在");
        }
        //3. 判断该批次的券余量是否大于0
        if (couponBatch.getAvailableCount() <= 0) {
            log.error("sendUserCouponSyn error availableCount is not enough batchId:{} userId:{}", batchId, userId);
            throw new BizException("该批次可发放数量不足");
        }
        //4.更新券余量和券发送数量
        boolean updateSendCouponBatchRes = couponBatchDao.updateSendCouponBatchCount(batchId);
        log.info("updateSendCouponBatchCoutn batchId={} userId={}", batchId, userId);
        if (!updateSendCouponBatchRes) {
            log.error("couponBatch updateSendCouponBatchRes error batchId={} userId={}", batchId, userId);
            throw new BizException("更新券批次数量失败");
        }
        //5.给用户表中的优惠券增加一条记录
        Coupon coupon = createCoupon(couponBatch, userId);
        boolean insertCouponRes = couponDao.insertCoupon(coupon);
        if (!insertCouponRes) {
            log.error("couponBatch insertCoupon error batchId={} userId={}", batchId, userId);
            throw new BizException("用户发券失败");
        }
        //6. 插入一个券过期提醒任务
        //将JSON中的rule规则，转化成CouponRule对象
        CouponRule couponRule = JSON.parseObject(couponBatch.getRule(), CouponRule.class);
        couponRemindService.insertCouponRemindTask(userId, coupon.getId(), couponRule);
        log.info("sendUserCouponSyn success coupon info: {}", JSON.toJSONString(coupon));
        return true;
    }

    /**
     * 同步发券(使用了分布式锁)
     *
     * @param batchId
     * @param userId
     * @return
     */
    @Override
    public boolean sendUserCouponSynWithLock(long batchId, long userId) {
        // 获取锁
        // 执行业务 sendUserCouponSyn
        // 释放锁

        // 设置锁的名称(相同的业务用一个就可以了)
        String lockKeyName = "sendCoupon";

        // 设置每一个request 的 requestID, 用UUID来实现
        String requestId = UUID.randomUUID().toString();

        if (redisLockUtil.tryGetLock(lockKeyName, requestId, 200)) {
            try {
                // 执行业务 sendUserCouponSyn
                return sendUserCouponSyn(batchId, userId);
            } catch (Exception ex) {
                log.error("sendUserCouponSynWithLock error, batchId={} userId={}", batchId, userId, ex);
                throw new BizException(ex.getMessage());
            } finally {
                // 最后一定要释放锁
                redisLockUtil.releaseLock(lockKeyName, requestId); // 同个请求，加锁和解锁都是同一个requestID
            }
        }
        return false;
    }

    @Override
    public boolean sendUserCouponBatch(long batchId, Set<Long> userIdSet) {
        for (Long userId : userIdSet) {
            //插入任务表
            Task task = new Task();
            task.setStatus(0);
            task.setTryCount(0);
            task.setBizType("send_coupon");
            //UUID
            task.setBizId(UUID.randomUUID().toString());

            SendCouponTaskModel sendCouponTaskModel = new SendCouponTaskModel();
            sendCouponTaskModel.setBatchId(batchId);
            sendCouponTaskModel.setUserId(userId);
            task.setBizParam(JSON.toJSONString(sendCouponTaskModel));

            task.setModifiedTime(new Date());
            task.setCreateTime(new Date());

            boolean res = taskDao.insertTask(task);
            if (res) {
                //插入任务记录，成功再发送消息
                messageSender.sendMessage("send-batch-coupon", JSON.toJSONString(task));
            } else {
                log.error("insert task table error userId:{} batchId:{}", userId, batchId);
            }
        }
        return false;
    }

    private Coupon createCoupon(CouponBatch couponBatch, long userId) {
        Coupon coupon = new Coupon();
        coupon.setUserId(userId);
        coupon.setCouponName(coupon.getCouponName());
        coupon.setCreateTime(new Date());
        coupon.setBatchId(couponBatch.getId());
        String rule = couponBatch.getRule();
        if (StringUtils.isNullOrEmpty(rule)) {
            log.error("couponBatch rule is empty batchId={} userId={}", couponBatch.getId(), userId);
            throw new BizException("券批次信息中规则信息为空");
        }

        /*
         * 将JSON中的rule规则，转化为CouponRule对象
         * */
        CouponRule couponRule = JSON.parseObject(rule, CouponRule.class);
        // 设置到期时间
        coupon.setValidateTime(couponRule.getEndTime());
        coupon.setCouponName(couponBatch.getCouponName());
        // 默认为1 有效
        coupon.setStatus(0);
        coupon.setCreateTime(new Date());
//        coupon.setId(99l);
        return coupon;
    }
}
