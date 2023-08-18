package com.shangan.trade.coupon.db.mappers;

import com.shangan.trade.coupon.db.model.CouponBatch;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponBatchMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponBatch record);

    int insertSelective(CouponBatch record);

    CouponBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponBatch record);

    int updateByPrimaryKey(CouponBatch record);

    List<CouponBatch> queryCouponBatchList();

    int updateSendCouponBatchCount(Long id);
}