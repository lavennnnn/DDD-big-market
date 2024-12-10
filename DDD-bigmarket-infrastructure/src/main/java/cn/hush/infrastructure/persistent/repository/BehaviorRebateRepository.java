package cn.hush.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import cn.hush.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import cn.hush.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.hush.domain.rebate.model.entity.TaskEntity;
import cn.hush.domain.rebate.model.valobj.BehaviorTypeVO;
import cn.hush.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import cn.hush.domain.rebate.repository.IBehaviorRebateRepository;
import cn.hush.infrastructure.event.EventPublisher;
import cn.hush.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import cn.hush.infrastructure.persistent.dao.ITaskDao;
import cn.hush.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import cn.hush.infrastructure.persistent.po.DailyBehaviorRebatePO;
import cn.hush.infrastructure.persistent.po.TaskPO;
import cn.hush.infrastructure.persistent.po.UserBehaviorRebateOrderPO;
import cn.hush.types.enums.ResponseCode;
import cn.hush.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Hush
 * @description 行为返利服务仓储实现
 * @create 2024-12-04 下午10:07
 */
@Slf4j
@Repository
public class BehaviorRebateRepository implements IBehaviorRebateRepository {

    @Resource
    private IDailyBehaviorRebateDao dailyBehaviorRebateDao;
    @Resource
    private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO) {
        List<DailyBehaviorRebatePO> dailyBehaviorRebatePOList = dailyBehaviorRebateDao.queryDailyBehaviorRebateConfigByBehaviorType(behaviorTypeVO.getCode());
        ArrayList<DailyBehaviorRebateVO> dailyBehaviorRebateVOList = new ArrayList<>(dailyBehaviorRebatePOList.size());
        for (DailyBehaviorRebatePO dailyBehaviorRebatePO : dailyBehaviorRebatePOList) {
            dailyBehaviorRebateVOList.add(DailyBehaviorRebateVO.builder()
                    .behaviorType(dailyBehaviorRebatePO.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebatePO.getRebateDesc())
                    .rebateType(dailyBehaviorRebatePO.getRebateType())
                    .rebateConfig(dailyBehaviorRebatePO.getRebateConfig())
                    .build());
        }
        return dailyBehaviorRebateVOList;
    }

    @Override
    public void saveUserRebateRecord(String userId, ArrayList<BehaviorRebateAggregate> behaviorRebateAggregateList) {
        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    for (BehaviorRebateAggregate aggregate : behaviorRebateAggregateList) {
                        BehaviorRebateOrderEntity behaviorRebateOrderEntity = aggregate.getBehaviorRebateOrderEntity();
                        //用户行为返利订单对象
                        UserBehaviorRebateOrderPO userBehaviorRebateOrder = UserBehaviorRebateOrderPO.builder()
                                .userId(behaviorRebateOrderEntity.getUserId())
                                .orderId(behaviorRebateOrderEntity.getOrderId())
                                .behaviorType(behaviorRebateOrderEntity.getBehaviorType())
                                .rebateDesc(behaviorRebateOrderEntity.getRebateDesc())
                                .rebateType(behaviorRebateOrderEntity.getRebateType())
                                .rebateConfig(behaviorRebateOrderEntity.getRebateConfig())
                                .outBusinessNo(behaviorRebateOrderEntity.getOutBusinessNo())
                                .bizId(behaviorRebateOrderEntity.getBizId())
                                .build();

                        userBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);

                        //任务对象
                        TaskEntity taskEntity = aggregate.getTaskEntity();
                        TaskPO task = new TaskPO();
                        task.setUserId(taskEntity.getUserId());
                        task.setTopic(taskEntity.getTopic());
                        task.setMessageId(taskEntity.getMessageId());
                        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
                        task.setState(taskEntity.getState().getCode());
                        taskDao.insert(task);
                    }
                    return 1;
                }catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入返利记录，唯一索引冲突 userId :{}", userId ,e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode());
                }
            });
        }finally {
            dbRouter.clear();
        }

        // 同步发送mq消息
        for (BehaviorRebateAggregate aggregate : behaviorRebateAggregateList) {
            TaskEntity taskEntity = aggregate.getTaskEntity();
            TaskPO task = new TaskPO();
            task.setUserId(taskEntity.getUserId());
            task.setMessageId(taskEntity.getMessageId());
            try {
                // 发送消息【在事务外执行，如果失败还有任务补偿】
                eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
                // 更新数据库记录， task 表
                taskDao.updateTaskSendMessageCompleted(task);
            } catch (Exception e) {
                log.error("写入返利记录，发送MQ消息失败 userId :{} topic :{}", taskEntity.getUserId(),taskEntity.getTopic(),e);
                taskDao.updateTaskSendMessageFail(task);
            }
        }

    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByUserIdAndOutBusinessNo(String userId, String outBusinessNo) {
        // 1. 请求对象
        UserBehaviorRebateOrderPO userBehaviorRebateOrderPO = new UserBehaviorRebateOrderPO();
        userBehaviorRebateOrderPO.setUserId(userId);
        userBehaviorRebateOrderPO.setOutBusinessNo(outBusinessNo);

        // 2. 查询结果
        List<UserBehaviorRebateOrderPO> userBehaviorRebateOrderList =  userBehaviorRebateOrderDao.queryOrderByOutBusinessNo(userBehaviorRebateOrderPO);
        ArrayList<BehaviorRebateOrderEntity> behaviorRebateOrderEntities = new ArrayList<>(userBehaviorRebateOrderList.size());
        for (UserBehaviorRebateOrderPO rebateOrder : userBehaviorRebateOrderList) {
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(rebateOrder.getUserId())
                    .orderId(rebateOrder.getOrderId())
                    .behaviorType(rebateOrder.getBehaviorType())
                    .rebateDesc(rebateOrder.getRebateDesc())
                    .rebateType(rebateOrder.getRebateType())
                    .rebateConfig(rebateOrder.getRebateConfig())
                    .outBusinessNo(rebateOrder.getOutBusinessNo())
                    .bizId(rebateOrder.getBizId())
                    .build();
            behaviorRebateOrderEntities.add(behaviorRebateOrderEntity);
        }
        return behaviorRebateOrderEntities;


    }

}
