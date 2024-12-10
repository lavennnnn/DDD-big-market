package cn.hush.domain.strategy.service;

import cn.hush.domain.strategy.model.vo.RuleWeightVO;

import java.util.List;
import java.util.Map;

/**
 * @author Hush
 * @description 抽奖规则接口
 * @create 2024-12-01 上午10:25
 */
public interface IRaffleRule {

    //查询解锁次数
    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);

    List<RuleWeightVO> queryAwardRuleWeightByActivityId(Long activityId);

    //内部使用
    List<RuleWeightVO> queryAwardRuleWeight(Long strategyId);
}
