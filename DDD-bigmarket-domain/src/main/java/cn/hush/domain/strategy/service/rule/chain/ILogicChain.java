package cn.hush.domain.strategy.service.rule.chain;

import cn.hush.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * @author Hush
 * @description 责任链接口
 * @create 2024-10-14 下午8:11
 */

public interface ILogicChain extends ILogicChainArmory {

    /**
     * 责任链接口
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);


}
