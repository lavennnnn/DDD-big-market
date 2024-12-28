package cn.hush.infrastructure.adapter.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import cn.hush.domain.award.model.aggregate.GiveOutPrizesAggregate;
import cn.hush.domain.award.model.aggregate.UserAwardRecordAggregate;
import cn.hush.domain.award.model.entity.TaskEntity;
import cn.hush.domain.award.model.entity.UserAwardRecordEntity;
import cn.hush.domain.award.model.entity.UserCreditAwardEntity;
import cn.hush.domain.award.model.vo.AccountStatusVO;
import cn.hush.domain.award.repository.IAwardRepository;
import cn.hush.infrastructure.dao.*;
import cn.hush.infrastructure.event.EventPublisher;
import cn.hush.infrastructure.dao.po.TaskPO;
import cn.hush.infrastructure.dao.po.UserAwardRecordPO;
import cn.hush.infrastructure.dao.po.UserCreditAccountPO;
import cn.hush.infrastructure.dao.po.UserRaffleOrderPO;
import cn.hush.types.enums.ResponseCode;
import cn.hush.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description 奖品仓储服务
 * @create 2024-11-28 上午1:13
 */
@Slf4j
@Repository
public class AwardRepository implements IAwardRepository {


        @Resource
        private ITaskDao taskDao;
        @Resource
        private IUserAwardRecordDao userAwardRecordDao;
        @Resource
        private IDBRouterStrategy dbRouter;
        @Resource
        private TransactionTemplate transactionTemplate;
        @Resource
        private EventPublisher eventPublisher;
        @Resource
        private IUserRaffleOrderDao userRaffleOrderDao;
        @Resource
        private IAwardDao awardDao;
        @Resource
        private IUserCreditAccountDao userCreditAccountDao;

        @Override
        public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {

            //从聚合对象中获取需要的信息
            UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
            TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
            String userId = userAwardRecordEntity.getUserId();
            Long activityId = userAwardRecordEntity.getActivityId();
            Integer awardId = userAwardRecordEntity.getAwardId();

            //转化为数据库支持的对象类型（PO）
            UserAwardRecordPO userAwardRecord = new UserAwardRecordPO();
            userAwardRecord.setUserId(userAwardRecordEntity.getUserId());
            userAwardRecord.setActivityId(userAwardRecordEntity.getActivityId());
            userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());
            userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
            userAwardRecord.setAwardId(userAwardRecordEntity.getAwardId());
            userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
            userAwardRecord.setAwardTime(userAwardRecordEntity.getAwardTime());
            userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

            //转换task表
            TaskPO task = new TaskPO();
            task.setUserId(taskEntity.getUserId());
            task.setTopic(taskEntity.getTopic());
            task.setMessageId(taskEntity.getMessageId());
            task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
            task.setState(taskEntity.getState().getCode());

            UserRaffleOrderPO userRaffleOrderReq = new UserRaffleOrderPO();
            userRaffleOrderReq.setUserId(userAwardRecordEntity.getUserId());
            userRaffleOrderReq.setOrderId(userAwardRecordEntity.getOrderId());

            try {
                // 路由
                dbRouter.doRouter(userId);
                //开启事务
                transactionTemplate.execute(status -> {
                    try {
                        // 写入记录
                        userAwardRecordDao.insert(userAwardRecord);
                        // 写入任务
                        taskDao.insert(task);
                        //更新抽奖单
                        int count = userRaffleOrderDao.updateUserRaffleOrderStateUsed(userRaffleOrderReq);
                        if (1 != count) {
                            status.setRollbackOnly();
                            log.error("写入中奖记录，用户抽奖单已使用过，不可重复抽奖 userId: {} activityId: {} awardId: {}", userId, activityId, awardId);
                            throw new AppException(ResponseCode.ACCOUNT_ORDER_ERROR.getCode(), ResponseCode.ACCOUNT_ORDER_ERROR.getInfo());
                        }
                        return 1;
                    } catch (DuplicateKeyException e) {
                        status.setRollbackOnly();
                        log.error("写入中奖记录，唯一索引冲突 userId: {} activityId: {} awardId: {}", userId, activityId, awardId, e);
                        throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                    }
                });
            } finally {
                dbRouter.clear();
            }

            try {
                // 发送消息【在事务外执行，如果失败还有任务补偿】
                eventPublisher.publish(task.getTopic(), task.getMessage());
                // 更新数据库记录，task 任务表
                taskDao.updateTaskSendMessageCompleted(task);
            } catch (Exception e) {
                log.error("写入中奖记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
                taskDao.updateTaskSendMessageFail(task);
            }

        }

    @Override
    public String queryAwardConfig(Integer awardId) {
        return awardDao.queryAwardConfigByAwardId(awardId);
    }

    @Override
    public void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate) {
        String userId = giveOutPrizesAggregate.getUserId();
        UserCreditAwardEntity userCreditAwardEntity = giveOutPrizesAggregate.getUserCreditAwardEntity();
        UserAwardRecordEntity userAwardRecordEntity = giveOutPrizesAggregate.getUserAwardRecordEntity();

        //更新发奖记录
        UserAwardRecordPO userAwardRecordReq = new UserAwardRecordPO();
        userAwardRecordReq.setUserId(userId);
        userAwardRecordReq.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecordReq.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        //更新用户积分 【首次则插入数据】
        UserCreditAccountPO userCreditAccountReq = new UserCreditAccountPO();
        userCreditAccountReq.setUserId(userId);
        userCreditAccountReq.setTotalAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccountReq.setAvailableAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccountReq.setAccountStatus(AccountStatusVO.open.getCode());

        try {
            // 路由
            dbRouter.doRouter(userId);
            //开启事务
            transactionTemplate.execute(status -> {
                try {
                    //更新积分 or 创建积分账户
                    int updateAccountCount = userCreditAccountDao.updateAmountAdd(userCreditAccountReq);
                    if (0 == updateAccountCount) {
                        userCreditAccountDao.insert(userCreditAccountReq);
                    }

                    //更新奖品记录
                    int updateAwardCount = userAwardRecordDao.updateAwardRecordCompletedState(userAwardRecordReq);
                    if (0 == updateAwardCount) {
                        log.warn("更新中奖纪录，重复更新拦截 userId:{} giveOutPrizesAggregate:{}", userId, JSON.toJSONString(giveOutPrizesAggregate));
                        status.setRollbackOnly();
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入中奖记录，唯一索引冲突 userId: {} ", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
        }
    }

    @Override
    public String queryAwardKey(Integer awardId) {
        return awardDao.queryAwardKeyByAwardId(awardId);
    }


}
























