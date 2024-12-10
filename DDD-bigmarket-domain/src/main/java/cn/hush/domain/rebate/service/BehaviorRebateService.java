package cn.hush.domain.rebate.service;

import cn.hush.domain.rebate.event.SendRebateMessageEvent;
import cn.hush.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import cn.hush.domain.rebate.model.entity.BehaviorEntity;
import cn.hush.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.hush.domain.rebate.model.entity.TaskEntity;
import cn.hush.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import cn.hush.domain.rebate.model.valobj.TaskStateVO;
import cn.hush.domain.rebate.repository.IBehaviorRebateRepository;
import cn.hush.types.common.Constants;
import cn.hush.types.event.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Hush
 * @description 行为返利服务
 * @create 2024-12-04 下午9:54
 */
@Service
public class BehaviorRebateService implements IBehaviorRebateService{

    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;

    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    public List<String> createOrder(BehaviorEntity behaviorEntity) {
        // 1. 查询返利配置
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOList = behaviorRebateRepository.queryDailyBehaviorRebateConfig(behaviorEntity.getBehaviorType());
        if (null == dailyBehaviorRebateVOList || dailyBehaviorRebateVOList.isEmpty()) return new ArrayList<String>();

        // 2. 构建聚合对象
        ArrayList<String> orderIds = new ArrayList<>();
        ArrayList<BehaviorRebateAggregate> behaviorRebateAggregateList = new ArrayList<>();
        //遍历配置
        for (DailyBehaviorRebateVO dailyBehaviorRebateVO : dailyBehaviorRebateVOList) {
            //拼装业务id：用户id_返利类型_外部透传业务id
            String bizId = behaviorEntity.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + behaviorEntity.getOutBusinessNo();
            //构建行为返利订单流水实体
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .outBusinessNo(behaviorEntity.getOutBusinessNo())
                    .bizId(bizId)
                    .build();

            orderIds.add(behaviorRebateOrderEntity.getOrderId());

            //构建 MQ 消息对象 —— 用于 Task
            SendRebateMessageEvent.RebateMessage rebateMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .userId(behaviorEntity.getUserId())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .bizId(bizId)
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .build();

            //构建事件消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(rebateMessage);

            //组装 Task 对象
            TaskEntity taskEntity = TaskEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .topic(sendRebateMessageEvent.topic())
                    .messageId(rebateMessageEventMessage.getId())
                    .message(rebateMessageEventMessage)
                    .state(TaskStateVO.create)
                    .build();

            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate.builder()
                    .userId(behaviorEntity.getUserId())
                    .behaviorRebateOrderEntity(behaviorRebateOrderEntity)
                    .taskEntity(taskEntity).build();

            behaviorRebateAggregateList.add(behaviorRebateAggregate);

        }

        // 3. 存储对象聚合数据
        behaviorRebateRepository.saveUserRebateRecord(behaviorEntity.getUserId(), behaviorRebateAggregateList);

        //4. 返回订单 ID 集合
        return orderIds;
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByUserIdAndOutBusinessNo(String userId, String outBusinessNo) {
        return behaviorRebateRepository.queryOrderByUserIdAndOutBusinessNo(userId, outBusinessNo);
    }

}
