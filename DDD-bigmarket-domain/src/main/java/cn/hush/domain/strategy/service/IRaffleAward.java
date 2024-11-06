package cn.hush.domain.strategy.service;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @author Hush
 * @description
 * @create 2024-11-06 下午7:05
 */

public interface IRaffleAward {

    /**
     * 根据策略ID查询抽奖奖品列表配置
     * @param strategyId 策略ID
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);

}
