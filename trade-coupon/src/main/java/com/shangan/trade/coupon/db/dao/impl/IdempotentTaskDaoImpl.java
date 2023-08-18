package com.shangan.trade.coupon.db.dao.impl;

import com.shangan.trade.coupon.db.dao.IdempotentTaskDao;
import com.shangan.trade.coupon.db.mappers.IdempotentTaskMapper;
import com.shangan.trade.coupon.db.model.IdempotentTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdempotentTaskDaoImpl implements IdempotentTaskDao {
    @Autowired
    private IdempotentTaskMapper idempotentTaskMapper;

    @Override
    public boolean insertIdempotentTask(IdempotentTask idempotentTask) {
        return idempotentTaskMapper.insert(idempotentTask) > 0;
    }

    @Override
    public IdempotentTask queryIdempotentTaskByBiz(String bizType, String bizId) {
        return idempotentTaskMapper.queryIdempotentTaskByBiz(bizType, bizId);
    }
}
