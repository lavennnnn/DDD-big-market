package cn.hush.domain.award.service;

import cn.hush.domain.award.adapter.event.SendAwardMessageEvent;
import cn.hush.domain.award.model.aggregate.UserAwardRecordAggregate;
import cn.hush.domain.award.model.entity.DistributeAwardEntity;
import cn.hush.domain.award.model.entity.TaskEntity;
import cn.hush.domain.award.model.entity.UserAwardRecordEntity;
import cn.hush.domain.award.model.vo.TaskStateVO;
import cn.hush.domain.award.adapter.repository.IAwardRepository;
import cn.hush.domain.award.service.distribute.IDistributeAward;
import cn.hush.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Hush
 * @description 奖品服务
 * @create 2024-11-28 上午12:41
 */
@Service
@Slf4j
public class AwardService implements IAwardService{


    private final IAwardRepository awardRepository;

    private final SendAwardMessageEvent sendAwardMessageEvent;

    private final Map<String, IDistributeAward> distributeAwardMap;

    public AwardService(IAwardRepository awardRepository, SendAwardMessageEvent sendAwardMessageEvent, Map<String, IDistributeAward> distributeAwardMap) {
        this.awardRepository = awardRepository;
        this.sendAwardMessageEvent = sendAwardMessageEvent;
        this.distributeAwardMap = distributeAwardMap;
    }


    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        //构建消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setOrderId(userAwardRecordEntity.getOrderId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        sendAwardMessage.setAwardConfig(userAwardRecordEntity.getAwardConfig());

        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent
                .buildEventMessage(sendAwardMessage);

        //构建任务对象
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessageId(sendAwardMessageEventMessage.getId());
        taskEntity.setMessage(sendAwardMessageEventMessage);
        taskEntity.setState(TaskStateVO.create);


        //构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
                .taskEntity(taskEntity)
                .userAwardRecordEntity(userAwardRecordEntity)
                .build();

        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);
    }

    @Override
    public void distributeAward(DistributeAwardEntity distributeAwardEntity) throws Exception {
        //奖品 key
        String awardKey = awardRepository.queryAwardKey(distributeAwardEntity.getAwardId());
        if (null == awardKey) {
            log.error("分发奖品，奖品id不存在，awardId:{}", awardKey);
            return;
        }

        IDistributeAward distributeAward = distributeAwardMap.get(awardKey);
        if (null == distributeAward) {
            log.error("分发奖品，对应的服务不存在， awardKey:{}", awardKey);
            throw new RuntimeException("分发奖品：" + awardKey + "对应的服务不存在");

        }

        //发放奖品
        distributeAward.giveOutPrizes(distributeAwardEntity);
    }

}
