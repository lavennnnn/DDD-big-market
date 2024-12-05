package cn.hush.trigger.listener;

import cn.hush.domain.activity.model.entity.SkuRechargeEntity;
import cn.hush.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.hush.domain.rebate.event.SendRebateMessageEvent;
import cn.hush.domain.rebate.model.valobj.RebateTypeVO;
import cn.hush.types.event.BaseEvent;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description 监听：行为返利消息
 * @create 2024-12-05 下午10:30
 */
@Slf4j
@Component
public class RebateMessageCostumer {

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private  String topic;

    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message) {
        try {
            log.info("监听用户行为返利 topic:{} message:{}", topic, message);
            //1. 转换消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
            }.getType());

            SendRebateMessageEvent.RebateMessage rebateMessage = eventMessage.getData();

            // todo
            if (!RebateTypeVO.SKU.getCode().equals(rebateMessage.getRebateType())) {
                log.info("监听用户行为返利消息 - 非 sku 奖励暂不处理 topic:{} message:{}", topic, message);
                return;
            }

            // 2. 入账奖励
            SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
            skuRechargeEntity.setUserId(rebateMessage.getUserId());
            skuRechargeEntity.setSku(Long.valueOf(rebateMessage.getRebateConfig()));
            skuRechargeEntity.setOutBusinessNo(rebateMessage.getBizId());
            raffleActivityAccountQuotaService.createOrder(skuRechargeEntity);
        }catch (Exception e) {
            log.error("监听用户行为返利，消息失败 topic:{} message:{}", topic, message, e);
            throw e;
        }

    }
}



















