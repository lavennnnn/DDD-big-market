package cn.hush.domain.strategy.service.rule.tree.factory.engine.impl;

import cn.hush.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.hush.domain.strategy.model.vo.RuleTreeNodeLineVO;
import cn.hush.domain.strategy.model.vo.RuleTreeNodeVO;
import cn.hush.domain.strategy.model.vo.RuleTreeVO;
import cn.hush.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.hush.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.hush.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author Hush
 * @description 决策树引擎
 * @create 2024-10-16 上午1:35
 */
@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {

    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> logicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
        this.ruleTreeVO = ruleTreeVO;
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId) {
        DefaultTreeFactory.StrategyAwardVO strategyAwardVO = null;

        // 获取基础信息
        String currentNode = ruleTreeVO.getTreeRootRuleNode();
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();

        // 获取起始节点「根节点记录了第一个要执行的规则」
        RuleTreeNodeVO ruleTreeNodeVO = treeNodeMap.get(currentNode);
        while (null != currentNode) {
            // 获取决策节点
            ILogicTreeNode logicTreeNode = logicTreeNodeGroup.get(ruleTreeNodeVO.getRuleKey());
            String ruleValue = ruleTreeNodeVO.getRuleValue();

            // 决策节点计算
            DefaultTreeFactory.TreeActionEntity logicEntity = logicTreeNode.logic(userId, strategyId, awardId, ruleValue);
            RuleLogicCheckTypeVO ruleLogicCheckTypeVO = logicEntity.getRuleLogicCheckType();
            strategyAwardVO = logicEntity.getStrategyAwardVO();
            log.info("决策树引擎【{}】treeId:{} node:{} code:{}", ruleTreeVO.getTreeName(), ruleTreeVO.getTreeId(), currentNode, ruleLogicCheckTypeVO.getCode());

            // 获取下个节点
            currentNode = nextNode(ruleLogicCheckTypeVO.getCode(), ruleTreeNodeVO.getTreeNodeLineVOList());
            ruleTreeNodeVO = treeNodeMap.get(currentNode);
        }

        // 返回最终结果
        return strategyAwardVO;

    }

    public String nextNode(String matterValue, List<RuleTreeNodeLineVO> treeNodeLineVOList) {
        if (null == treeNodeLineVOList || treeNodeLineVOList.isEmpty()) return null;
        for (RuleTreeNodeLineVO nodeLine : treeNodeLineVOList) {
            if (decisionLogic(matterValue, nodeLine)) {
                return nodeLine.getRuleNodeTo();
            }
        }
        return null;
    }


    public boolean decisionLogic(String matteValue, RuleTreeNodeLineVO nodeLineVO) {
        switch (nodeLineVO.getRuleLimitType()) {
            case EQUAL:
                return matteValue.equals(nodeLineVO.getRuleLimitValue().getCode());
            case GT:

            case LT:

            case GE:

            case LE:

            case ENUM:

            default:
                return false;

        }
    }
}



















