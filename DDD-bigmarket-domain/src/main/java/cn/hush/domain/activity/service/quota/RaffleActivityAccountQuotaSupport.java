package cn.hush.domain.activity.service.quota;

import cn.hush.domain.activity.model.entity.ActivityCountEntity;
import cn.hush.domain.activity.model.entity.ActivityEntity;
import cn.hush.domain.activity.model.entity.ActivitySkuEntity;
import cn.hush.domain.activity.repository.IActivityRepository;
import cn.hush.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;

/**
 * @author Hush
 * @description 抽奖活动的支撑类
 * @create 2024-11-20 上午1:06
 */

public class RaffleActivityAccountQuotaSupport {

    protected DefaultActivityChainFactory defaultActivityChainFactory;

    protected IActivityRepository activityRepository;

    public RaffleActivityAccountQuotaSupport(IActivityRepository activityRepository, DefaultActivityChainFactory defaultactivityChainFactory) {
        this.activityRepository = activityRepository;
        this.defaultActivityChainFactory= defaultactivityChainFactory;
    }

    public ActivitySkuEntity queryActivitySku(Long sku) {
        return activityRepository.queryActivitySku(sku);
    }

    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        return activityRepository.queryRaffleActivityByActivityId(activityId);
    }

    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        return activityRepository.queryRaffleActivityCountByActivityCountId(activityCountId);
    }

}
