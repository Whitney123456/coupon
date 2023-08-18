package com.shangan.trade.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.coupon.code.ActivationCodeUtil;
import com.shangan.trade.coupon.db.dao.CouponCodeDao;
import com.shangan.trade.coupon.db.model.CouponCode;
import com.shangan.trade.coupon.exceptions.BizException;
import com.shangan.trade.coupon.service.CouponCodeService;
import com.shangan.trade.coupon.service.CouponSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CouponCodeServiceImpl implements CouponCodeService {
    final static String password = "nihaohello";

    @Autowired
    private CouponCodeDao couponCodeDao;

    @Autowired
    private CouponSendService couponSendService;

    @Override
    public List<String> createCouponCodeList(long batchId, int count) {

        /*
         * step1: 生成券码
         */
        //每执行一次就增加1，可以使用Redis中 数字自增的功能
        int groupId = 88;
        List<String> codes = ActivationCodeUtil.create(groupId, count, 12, password);
        log.info("create codes;{}", JSON.toJSONString(codes));
        /*
         * step2: 生成券码保存到数据库中
         */
        for (String code : codes) {
            CouponCode couponCode = new CouponCode();
            couponCode.setCode(code);
            couponCode.setBatchId(batchId);
            couponCode.setStatus(0);
            couponCode.setCreateTime(new Date());
            couponCode.setModifyTime(new Date());
            couponCodeDao.insertCouponCode(couponCode);
        }
        return codes;
    }

    @Override
    public boolean useCouponCode(long userId, String couponCode) {
        log.info("useCouponCode userId:{} couponCode:{}", userId, couponCode);
        //1. 检验这个优惠券码是不是我们生成的
        boolean res = ActivationCodeUtil.verifyCode(couponCode);
        if (!res) {
            throw new BizException("无效的兑换码");
        }
        //2.查询这个优惠券是不是已经被使用
        CouponCode couponCodeInfo = couponCodeDao.queryCouponByCode(couponCode);
        if (couponCodeInfo == null) {
            throw new BizException("无效的兑换码");
        }

        if (couponCodeInfo.getStatus() != 0) { //判断是否已经使用
            throw new BizException("兑换码已使用");
        }

        /*
         * 1.优惠券 状态改成已使用，记录用户ID
         * 2. 给用户发券
         */
        sendCouponByCode(userId, couponCodeInfo);
        log.info("useCouponCode send success userId:{} couponCode:{}", userId, couponCode);
        return true;
    }

    @Override
    public boolean userCouponCode(long userId, String couponCode) {
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendCouponByCode(long userId, CouponCode couponCodeInfo) {
        //给用户发券
        boolean sendResult = couponSendService.sendUserCouponSynWithLock(couponCodeInfo.getBatchId(), userId);
        if (!sendResult) {
            throw new BizException("给用户发券失败");
        }

        //更新优惠券
        couponCodeInfo.setUserId(userId);
        couponCodeInfo.setStatus(1);
        couponCodeInfo.setModifyTime(new Date());
        couponCodeDao.updateCouponCode(couponCodeInfo);
    }
}
