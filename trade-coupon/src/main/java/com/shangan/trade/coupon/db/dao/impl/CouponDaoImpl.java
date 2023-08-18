package com.shangan.trade.coupon.db.dao.impl;

import com.shangan.trade.coupon.db.dao.CouponDao;
import com.shangan.trade.coupon.db.mappers.CouponMapper;
import com.shangan.trade.coupon.db.model.Coupon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CouponDaoImpl implements CouponDao {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public boolean insertCoupon(Coupon coupon) {
        int result = couponMapper.insert(coupon);
        // 大于 0 表示插入成功
        return result > 0;
    }

    /**
     * 查询用户的优惠券
     *
     * @param userId
     * @return
     */
    @Override
    public List<Coupon> queryUserCoupons(long userId) {
        return couponMapper.queryUserCoupons(userId);
    }

    @Override
    public Coupon queryCouponById(long couponId) {
        return couponMapper.selectByPrimaryKey(couponId);
    }

    @Override
    public boolean updateCoupon(Coupon coupon) {
        return couponMapper.updateByPrimaryKey(coupon) > 0;
    }
}
