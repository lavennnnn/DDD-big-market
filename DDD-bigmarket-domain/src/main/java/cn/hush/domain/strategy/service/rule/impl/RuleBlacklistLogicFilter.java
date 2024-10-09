package cn.hush.domain.strategy.service.rule.impl;

import cn.hush.domain.strategy.model.entity.RuleActionEntity;
import cn.hush.domain.strategy.model.entity.RuleMatterEntity;
import cn.hush.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.hush.domain.strategy.repository.IStrategyRepository;
import cn.hush.domain.strategy.service.annotation.LogicStrategy;
import cn.hush.domain.strategy.service.rule.ILogicFilter;
import cn.hush.domain.strategy.service.rule.factory.DefaultLogicFactory;
import cn.hush.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description 黑名单用户规则过滤
 * @create 2024-10-10 上午12:36
 */
@Slf4j
@Component
@LogicStrategy(logicModel = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBlacklistLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository repository;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-黑名单 userId:{} strategyId:{} ruleModel:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());

        String userId = ruleMatterEntity.getUserId();

        // 查询规则值配置
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),
                ruleMatterEntity.getAwardId(),
                ruleMatterEntity.getRuleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        //100:user001,user002,user003
        // 过滤其他规则
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                        .data(RuleActionEntity.RaffleBeforeEntity.builder()
                                .strategyId(ruleMatterEntity.getStrategyId())
                                .awardId(awardId)
                                .build())
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                        .build();
            }
        }

        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();

        }


    }














