package com.shangan.trade.coupon.db.mappers;

import com.shangan.trade.coupon.db.model.IdempotentTask;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotentTaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(IdempotentTask record);

    int insertSelective(IdempotentTask record);

    IdempotentTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IdempotentTask record);

    int updateByPrimaryKey(IdempotentTask record);

    IdempotentTask queryIdempotentTaskByBiz(String bizType, String bizId);
}