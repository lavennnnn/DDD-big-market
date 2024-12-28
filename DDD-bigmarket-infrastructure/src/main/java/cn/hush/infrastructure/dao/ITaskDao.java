package cn.hush.infrastructure.dao;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.hush.infrastructure.dao.po.TaskPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Hush
 * @description 任务表，发送MQ
 * @create 2024-11-14 下午10:47
 */

@Mapper
public interface ITaskDao {

    void insert(TaskPO task);

    @DBRouter
    void updateTaskSendMessageCompleted(TaskPO task);
    @DBRouter
    void updateTaskSendMessageFail(TaskPO task);

    List<TaskPO> queryNoSendMessageTaskList();
}
