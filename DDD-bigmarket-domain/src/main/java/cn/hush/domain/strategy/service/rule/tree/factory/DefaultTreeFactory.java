package cn.hush.domain.strategy.service.rule.tree.factory;

import cn.hush.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.hush.domain.strategy.model.vo.RuleTreeVO;
import cn.hush.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import cn.hush.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.hush.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Hush
 * @description 规则树工厂
 * @create 2024-10-16 上午1:22
 */
@Service
public class DefaultTreeFactory {

    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    public DefaultTreeFactory(Map<String, ILogicTreeNode> logicTreeNodeGroup) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
    }

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVO);
    }

    /**
     * 决策树动作实体
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeActionEntity {
        //接管或放行
        private RuleLogicCheckTypeVO ruleLogicCheckType;
        //供决策树工厂使用的数据
        private StrategyAwardVO strategyAwardVO;
    }

    /**
     * 供决策树工厂使用的数据
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        /** 抽奖奖品ID - 内部流转使用 */
        private Integer awardId;
        /** 抽奖奖品规则 */
        private String awardRuleValue;
    }


}



















