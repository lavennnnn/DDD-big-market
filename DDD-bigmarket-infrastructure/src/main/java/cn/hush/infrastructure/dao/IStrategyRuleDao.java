package cn.hush.infrastructure.dao;

import cn.hush.infrastructure.dao.po.StrategyRulePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 策略规则
 */

@Mapper
public interface IStrategyRuleDao {

    List<StrategyRulePO> queryStrategyRuleList();

    StrategyRulePO queryStrategyRule(StrategyRulePO strategyRuleReq);

    String queryStrategyRuleValue(StrategyRulePO strategyRule);
}
