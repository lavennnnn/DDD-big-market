package cn.hush.domain.rebate.event;

import cn.hush.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Hush
 * @description 发送返利消息事件
 * @create 2024-12-04 下午10:20
 */
@Service
public class SendRebateMessageEvent extends BaseEvent<SendRebateMessageEvent.RebateMessage> {

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @Override
    public EventMessage<RebateMessage> buildEventMessage(RebateMessage data) {
        return EventMessage.<RebateMessage>builder()
                .data(data)
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RebateMessage {

        /** 用户ID */
        private String userId;
        /** 返利描述 */
        private String rebateDesc;
        /** 返利类型（sku 活动库存充值商品、integral 用户活动积分） */
        private String rebateType;
        /** 返利配置 */
        private String rebateConfig;
        /** 唯一业务id */
        private String bizId;

    }
}
