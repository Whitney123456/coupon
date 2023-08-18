package com.shangan.trade.coupon.db.dao;

import com.shangan.trade.coupon.db.model.IdempotentTask;

public interface IdempotentTaskDao {

    /**
     * 插入幂等记录
     * @param idempotentTask
     * @return
     */
    boolean insertIdempotentTask(IdempotentTask idempotentTask);

    /**
     * 根据业务数据查询
     * @param bizType
     * @param bizId
     * @return
     */
    IdempotentTask queryIdempotentTaskByBiz(String bizType, String bizId);
}
