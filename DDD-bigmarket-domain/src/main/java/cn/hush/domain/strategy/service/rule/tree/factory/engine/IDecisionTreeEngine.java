package cn.hush.domain.strategy.service.rule.tree.factory.engine;

import cn.hush.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

/**
 * @author Hush
 * @description 规则树组合接口
 * @create 2024-10-16 上午1:33
 */
public interface IDecisionTreeEngine {

    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId, Date endDateTime);

}
