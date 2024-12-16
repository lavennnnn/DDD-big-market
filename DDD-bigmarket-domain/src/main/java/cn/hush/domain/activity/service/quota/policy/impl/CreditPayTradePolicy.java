package cn.hush.domain.activity.service.quota.policy.impl;

import cn.hush.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import cn.hush.domain.activity.model.vo.OrderStateVO;
import cn.hush.domain.activity.repository.IActivityRepository;
import cn.hush.domain.activity.service.quota.policy.ITradePolicy;
import org.springframework.stereotype.Service;

/**
 * @author Hush
 * @description
 * @create 2024-12-16 下午5:54
 */
@Service("credit_pay_trade")
public class CreditPayTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public CreditPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.setOrderState(OrderStateVO.wait_pay);
        activityRepository.doSaveCreditPayOrder(createQuotaOrderAggregate);
    }

}
