package com.shangan.trade.coupon.db.dao.impl;

import com.shangan.trade.coupon.db.dao.TaskDao;
import com.shangan.trade.coupon.db.mappers.TaskMapper;
import com.shangan.trade.coupon.db.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskDaoImpl implements TaskDao {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public boolean insertTask(Task task) {
        return taskMapper.insertSelective(task) > 0;
    }

    @Override
    public int update(Task task) {
        return taskMapper.updateByBiz(task);
    }

    @Override
    public Task queryTaskById(Long id) {
        return taskMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Task> queryFailedTasks() {
        return taskMapper.queryFailedTasks();
    }

    @Override
    public List<Task> queryRemindTasks() {
        return taskMapper.queryRemindTasks();
    }
}
