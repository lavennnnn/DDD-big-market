package cn.hush.domain.rebate.service;

import cn.hush.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

/**
 * @author Hush
 * @description 行为返利服务接口
 * @create 2024-12-04 下午8:43
 */
public interface IBehaviorRebateService {

    /**
     * @param behaviorEntity 行为实体
     * @return 单号列表
     */
    List<String> createOrder(BehaviorEntity behaviorEntity);

}
