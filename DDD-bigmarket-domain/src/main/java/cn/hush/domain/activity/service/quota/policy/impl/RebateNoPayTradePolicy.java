package cn.hush.domain.activity.service.quota.policy.impl;

import cn.hush.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import cn.hush.domain.activity.model.vo.OrderStateVO;
import cn.hush.domain.activity.repository.IActivityRepository;
import cn.hush.domain.activity.service.quota.policy.ITradePolicy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Hush
 * @description
 * @create 2024-12-16 下午5:54
 */
@Service("rebate_no_pay_trade")
public class RebateNoPayTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public RebateNoPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.setOrderState(OrderStateVO.completed);
        createQuotaOrderAggregate.getActivityOrderEntity().setPayPriceAmount(BigDecimal.ZERO);
        activityRepository.doSaveNoPayOrder(createQuotaOrderAggregate);
    }

}
