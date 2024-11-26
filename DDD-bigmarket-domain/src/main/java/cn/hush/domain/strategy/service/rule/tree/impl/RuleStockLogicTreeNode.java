package cn.hush.domain.strategy.service.rule.tree.impl;

import cn.hush.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.hush.domain.strategy.model.vo.StrategyAwardStockKeyVO;
import cn.hush.domain.strategy.repository.IStrategyRepository;
import cn.hush.domain.strategy.service.armory.IStrategyDispatch;
import cn.hush.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.hush.domain.strategy.service.rule.tree.ILogicTreeNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Hush
 * @description 库存节点
 * @create 2024-10-16 上午1:19
 */
@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyDispatch strategyDispatch;
    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("规则过滤-库存扣减 userId:{}, StrategyId:{}, awardId:{}", userId, strategyId, awardId);

        // 扣减库存
        Boolean status = strategyDispatch.subtractAwardStock(strategyId, awardId);
        // 判断 status : true 扣减成功
        if (status) {
            // 写入延迟队列，延迟消费更新数据库记录。【在 trigger 的 job;UpdateAwardStockJob 下消费队列，更新数据库记录】
            strategyRepository.awardStockConsumeSendQueue(StrategyAwardStockKeyVO.builder()
                    .strategyId(strategyId)
                    .awardId(awardId)
                    .build());
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                    .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                            .awardId(awardId)
                            .awardRuleValue("")
                            .build())
                    .build();
        }
        //如果库存不足，直接返回放行
        log.warn("规则过滤-库存扣减-告警，库存不足。userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}














