package cn.hush.domain.activity.service.quota.rule;

import cn.hush.domain.activity.model.entity.ActivityCountEntity;
import cn.hush.domain.activity.model.entity.ActivityEntity;
import cn.hush.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author Hush
 * @description 下单规则过滤接口
 * @create 2024-11-19 下午8:43
 */
public interface IActionChain extends IActionChainArmory{

    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

}
