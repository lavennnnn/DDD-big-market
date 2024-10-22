package cn.hush.domain.strategy.service.rule.tree;

import cn.hush.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author Hush
 * @description 规则树接口
 * @create 2024-10-16 上午1:14
 */
public interface ILogicTreeNode {

    DefaultTreeFactory.TreeActionEntity logic(String userId, Long StrategyId, Integer awardId, String ruleValue);

}
