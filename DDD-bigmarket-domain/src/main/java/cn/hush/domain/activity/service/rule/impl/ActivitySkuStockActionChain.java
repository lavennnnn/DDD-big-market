package cn.hush.domain.activity.service.rule.impl;

import cn.hush.domain.activity.model.entity.ActivityCountEntity;
import cn.hush.domain.activity.model.entity.ActivityEntity;
import cn.hush.domain.activity.model.entity.ActivitySkuEntity;
import cn.hush.domain.activity.service.rule.AbstractActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Hush
 * @description 活动规则过滤【日期、状态】
 * @create 2024-11-19 下午8:53
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {


    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。");
        return true;
    }
}
