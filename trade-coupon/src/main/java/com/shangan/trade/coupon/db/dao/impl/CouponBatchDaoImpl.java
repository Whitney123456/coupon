package com.shangan.trade.coupon.db.dao.impl;

import com.shangan.trade.coupon.db.dao.CouponBatchDao;
import com.shangan.trade.coupon.db.mappers.CouponBatchMapper;
import com.shangan.trade.coupon.db.model.CouponBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class CouponBatchDaoImpl implements CouponBatchDao {

    @Autowired
    private CouponBatchMapper couponBatchMapper;

    @Override
    public boolean insertCouponBatch(CouponBatch couponBatch) {
        int result = couponBatchMapper.insert(couponBatch);
        return result > 1;
    }

    @Override
    public boolean deleteCouponBatchById(long id) {
        int result = couponBatchMapper.deleteByPrimaryKey(id);
        return result > 0;
    }

    @Override
    public CouponBatch queryCouponBatchById(long id) {
        return couponBatchMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateCouponBatch(CouponBatch couponBatch) {
        int result = couponBatchMapper.updateByPrimaryKey(couponBatch);
        return result > 0;
    }

    @Override
    public List<CouponBatch> queryCouponBatchList() {
        return couponBatchMapper.queryCouponBatchList();
    }

    @Override
    public boolean updateSendCouponBatchCount(Long id) {
        int result = couponBatchMapper.updateSendCouponBatchCount(id);
        return result > 0;
    }
}
