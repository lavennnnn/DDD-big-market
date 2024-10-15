package cn.hush.domain.strategy.service.rule.filter;

import cn.hush.domain.strategy.model.entity.RuleActionEntity;
import cn.hush.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @author Hush
 * @description 抽奖规则过滤接口
 * @create 2024-10-09 下午10:49
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);

}
