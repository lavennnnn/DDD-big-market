package cn.hush.domain.task.repository;

import cn.hush.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @author Hush
 * @description 任务仓储接口
 * @create 2024-11-28 下午4:41
 */

public interface ITaskRepository {


    public List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFail(String userId, String messageId);
}
