package com.shangan.trade.coupon.db.mappers;

import com.shangan.trade.coupon.db.model.CouponCode;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponCodeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponCode record);

    int insertSelective(CouponCode record);

    CouponCode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponCode record);

    int updateByPrimaryKey(CouponCode record);

    CouponCode queryCouponByCode(String code);
}