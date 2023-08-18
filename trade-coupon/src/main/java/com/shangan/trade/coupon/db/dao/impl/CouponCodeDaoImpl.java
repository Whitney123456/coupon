package com.shangan.trade.coupon.db.dao.impl;

import com.shangan.trade.coupon.db.dao.CouponCodeDao;
import com.shangan.trade.coupon.db.mappers.CouponCodeMapper;
import com.shangan.trade.coupon.db.model.CouponCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponCodeDaoImpl implements CouponCodeDao {

    @Autowired
    private CouponCodeMapper couponCodeMapper;

    @Override
    public boolean insertCouponCode(CouponCode couponCode) {
        return couponCodeMapper.insert(couponCode) > 0;
    }

    @Override
    public CouponCode queryCouponByCode(String code) {
        return couponCodeMapper.queryCouponByCode(code);
    }

    @Override
    public boolean updateCouponCode(CouponCode couponCode) {
        return couponCodeMapper.updateByPrimaryKey(couponCode) > 0;
    }
}
