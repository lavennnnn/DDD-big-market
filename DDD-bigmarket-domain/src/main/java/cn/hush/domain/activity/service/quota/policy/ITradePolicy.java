package cn.hush.domain.activity.service.quota.policy;

import cn.hush.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

/**
 * @author Hush
 * @description 交易策略接口，包括；返利兑换（不用支付），积分订单（需要支付）
 * @create 2024-12-15 下午12:30
 */
public interface ITradePolicy {

    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);

}
