package com.shangan.trade.coupon.db.mappers;

import com.shangan.trade.coupon.db.model.Coupon;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Coupon record);

    int insertSelective(Coupon record);

    Coupon selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Coupon record);

    int updateByPrimaryKey(Coupon record);

    List<Coupon> queryUserCoupons(long userId);

    List<Coupon> queryCouponById(long couponId);
}