package com.shangan.trade.coupon;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.coupon.db.dao.TaskDao;
import com.shangan.trade.coupon.db.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskTest {
    @Autowired
    private TaskDao taskDao;

    @Test
    public void queryRemindTest() {
        List<Task> tasks = taskDao.queryRemindTasks();
        if (tasks == null) {
            return;
        }
        for (Task task : tasks) {
            System.out.println(JSON.toJSONString(task));
        }
    }
}

