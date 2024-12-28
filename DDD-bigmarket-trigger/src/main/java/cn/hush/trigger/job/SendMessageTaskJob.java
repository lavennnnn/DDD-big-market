package cn.hush.trigger.job;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import cn.hush.domain.task.ITaskService;
import cn.hush.domain.task.model.entity.TaskEntity;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Hush
 * @description 发送mq消息任务队列
 * @create 2024-11-28 下午4:29
 */
@Slf4j
@Component()
public class SendMessageTaskJob {

    @Resource
    private ITaskService taskService;
    @Resource
    private ThreadPoolExecutor executor;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 本地化任务注解；@Scheduled(cron = "0/5 * * * * ?")
     * 分布式任务注解；@XxlJob("SendMessageTaskJob")
     */
    @XxlJob("sendMessageTaskJob_DB1")
    public void exec_db01() {
        // 为什么加锁？分布式应用N台机器部署互备，任务调度会有N个同时执行，那么这里需要增加抢占机制，谁抢占到谁就执行。完毕后，下一轮继续抢占。
        RLock lock = redissonClient.getLock("big-market_SendMessageTaskJob_DB1");
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(3, 0, TimeUnit.MINUTES);
            if (!isLocked) return;

            // 获取分库数量
            int dbCount = dbRouter.dbCount();

            // 逐个库扫描表【每个库一个任务表】
            for (int dbIdx = 1; dbIdx <= dbCount; dbIdx++) {
                int finalDbIdx = dbIdx;
                executor.execute(() -> {
                    try {
                        dbRouter.setDBKey(finalDbIdx);
                        dbRouter.setTBKey(0);
                        List<TaskEntity> taskEntities = taskService.queryNoSendMessageTaskList();
                        if (taskEntities.isEmpty()) return;
                        // 发送MQ消息
                        for (TaskEntity taskEntity : taskEntities) {
                            // 开启线程发送，提高发送效率。配置的线程池策略为 CallerRunsPolicy，在 ThreadPoolConfig 配置中有4个策略，面试中容易对比提问。可以检索下相关资料。
                            executor.execute(() -> {
                                try {
                                    taskService.sendMessage(taskEntity);
                                    taskService.updateTaskSendMessageCompleted(taskEntity.getUserId(), taskEntity.getMessageId());
                                } catch (Exception e) {
                                    log.error("定时任务，发送MQ消息失败 userId: {} topic: {}", taskEntity.getUserId(), taskEntity.getTopic());
                                    taskService.updateTaskSendMessageFail(taskEntity.getUserId(), taskEntity.getMessageId());
                                }
                            });
                        }
                    } finally {
                        dbRouter.clear();
                    }
                });
            }
        } catch (Exception e) {
            log.error("定时任务，扫描MQ任务表发送消息失败。", e);
        } finally {
            dbRouter.clear();
        }
    }

    /**
     * 本地化任务注解；@Scheduled(cron = "0/5 * * * * ?")
     * 分布式任务注解；@XxlJob("SendMessageTaskJob_DB2")
     */
    @XxlJob("SendMessageTaskJob_DB2")
    public void exec_db02() {
        // 为什么加锁？分布式应用N台机器部署互备，任务调度会有N个同时执行，那么这里需要增加抢占机制，谁抢占到谁就执行。完毕后，下一轮继续抢占。
        RLock lock = redissonClient.getLock("big-market-SendMessageTaskJob_DB2");
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(3, 0, TimeUnit.SECONDS);
            if (!isLocked) return;

            // 设置库表
            dbRouter.setDBKey(2);
            dbRouter.setTBKey(0);
            // 查询未发送的任务
            List<TaskEntity> taskEntities = taskService.queryNoSendMessageTaskList();
            if (taskEntities.isEmpty()) return;
            // 发送MQ消息
            for (TaskEntity taskEntity : taskEntities) {
                try {
                    taskService.sendMessage(taskEntity);
                    taskService.updateTaskSendMessageCompleted(taskEntity.getUserId(), taskEntity.getMessageId());
                } catch (Exception e) {
                    log.error("定时任务，发送MQ消息失败 userId: {} topic: {}", taskEntity.getUserId(), taskEntity.getTopic());
                    taskService.updateTaskSendMessageFail(taskEntity.getUserId(), taskEntity.getMessageId());
                }
            }
        } catch (Exception e) {
            log.error("定时任务，扫描MQ任务表发送消息失败。", e);
        } finally {
            dbRouter.clear();
            if (isLocked) {
                lock.unlock();
            }
        }
    }

}
