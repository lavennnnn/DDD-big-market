package cn.hush.trigger.listener;

import cn.hush.domain.award.event.SendAwardMessageEvent;
import cn.hush.domain.award.model.entity.DistributeAwardEntity;
import cn.hush.domain.award.service.IAwardService;
import cn.hush.types.event.BaseEvent;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Hush
 * @description
 * @create 2024-11-28 下午6:34
 */
@Slf4j
@Component
public class SendAwardCustomer {

    private final IAwardService awardService;
    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;

    public SendAwardCustomer(IAwardService awardService) {
        this.awardService = awardService;
    }

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_award}", autoDelete = "true"))
    public void listener(String msg) {
        try {
            log.info("监听用户奖品发送消息 topic: {} message: {}", topic, msg);

            BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage = JSON.parseObject(msg, new TypeReference<BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>>() {
            }.getType());

            SendAwardMessageEvent.SendAwardMessage sendAwardMessage = eventMessage.getData();

            //发放奖品
            DistributeAwardEntity distributeAwardEntity = new DistributeAwardEntity();
            distributeAwardEntity.setUserId(sendAwardMessage.getUserId());
            distributeAwardEntity.setOrderId(sendAwardMessage.getOrderId());
            distributeAwardEntity.setAwardId(sendAwardMessage.getAwardId());
            distributeAwardEntity.setAwardConfig(sendAwardMessage.getAwardConfig());
            awardService.distributeAward(distributeAwardEntity);

        } catch (Exception e) {
            log.error("监听用户奖品发送消息，消费失败 topic: {} message: {}", topic, msg);
            throw e;
        }
    }

}
