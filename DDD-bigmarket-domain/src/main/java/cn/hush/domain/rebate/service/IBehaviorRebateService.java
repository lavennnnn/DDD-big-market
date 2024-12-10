package cn.hush.domain.rebate.service;

import cn.hush.domain.rebate.model.entity.BehaviorEntity;
import cn.hush.domain.rebate.model.entity.BehaviorRebateOrderEntity;

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

    //根据用户id和外部防重id查询订单列表（用于查看用户是否已经签到）
    List<BehaviorRebateOrderEntity> queryOrderByUserIdAndOutBusinessNo(String userId, String outBusinessNo);
}
