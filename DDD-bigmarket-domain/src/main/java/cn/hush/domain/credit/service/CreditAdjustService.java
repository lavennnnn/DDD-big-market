package cn.hush.domain.credit.service;

import cn.hush.domain.credit.model.aggregate.TradeAggregate;
import cn.hush.domain.credit.model.entity.CreditAccountEntity;
import cn.hush.domain.credit.model.entity.CreditOrderEntity;
import cn.hush.domain.credit.model.entity.TradeEntity;
import cn.hush.domain.credit.repository.ICreditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description 积分调额服务【正逆向，增减积分】
 * @create 2024-12-13 下午6:52
 */
@Slf4j
@Service
public class CreditAdjustService implements ICreditAdjustService{

    @Resource
    private ICreditRepository creditRepository;

    @Override
    public String createOrder(TradeEntity tradeEntity) {
        log.info("增加账户积分额度开始 userId:{} tradeName:{} amount:{}",
                tradeEntity.getUserId(), tradeEntity.getTradeName(), tradeEntity.getAmount());

        // 1.创建账户积分实体
        CreditAccountEntity creditAccountEntity = TradeAggregate.createCreditAccountEntity(
                tradeEntity.getUserId(),
                tradeEntity.getAmount()
        );

        // 2.创建账户订单实体
        CreditOrderEntity creditOrderEntity = TradeAggregate.createCreditOrderEntity(
                tradeEntity.getUserId(),
                tradeEntity.getTradeName(),
                tradeEntity.getTradeType(),
                tradeEntity.getAmount(),
                tradeEntity.getOutBusinessNo()
        );

        // 3.构建聚合对象
        TradeAggregate tradeAggregate = TradeAggregate.builder()
                .userId(tradeEntity.getUserId())
                .creditAccountEntity(creditAccountEntity)
                .creditOrderEntity(creditOrderEntity)
                .build();

        // 4. 保存积分交易订单
        creditRepository.saveUserCreditTradeOrder(tradeAggregate);
        log.info("增加账户积分额度完成 userId:{} orderId:{}", tradeEntity.getUserId(), creditOrderEntity.getOrderId());

        return creditOrderEntity.getOrderId();
    }

}