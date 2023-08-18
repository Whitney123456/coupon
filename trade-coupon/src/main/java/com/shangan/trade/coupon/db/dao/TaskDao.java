package com.shangan.trade.coupon.db.dao;

import com.shangan.trade.coupon.db.model.Task;

import java.util.List;

public interface TaskDao {

    /**
     * 新增一个任务记录
     * @param task
     * @return
     */
    boolean insertTask(Task task);

    /**
     * 更新任务信息
     * @param task
     * @return
     */
    int update(Task task);

    /**
     * 查询任务信息
     * @param id
     * @return
     */
    Task queryTaskById(Long id);

    /**
     * 查询执行失败的任务
     * @return
     */
    List<Task> queryFailedTasks();

    /**
     * 查询提醒任务数据
     * @return
     */
    List<Task> queryRemindTasks();
}
