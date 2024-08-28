package cn.hush.infrastructure.persistent.dao;

import cn.hush.infrastructure.persistent.po.StrategyAwardPO;
import cn.hush.infrastructure.persistent.po.StrategyRulePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 策略规则
 */

@Mapper
public interface IStrategyRuleDao {

    List<StrategyRulePO> queryStrategyRuleList();

}
