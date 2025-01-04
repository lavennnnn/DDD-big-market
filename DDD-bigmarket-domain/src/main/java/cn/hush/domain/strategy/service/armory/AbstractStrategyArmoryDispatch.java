package cn.hush.domain.strategy.service.armory;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;
import cn.hush.domain.strategy.model.entity.StrategyEntity;
import cn.hush.domain.strategy.model.entity.StrategyRuleEntity;
import cn.hush.domain.strategy.repository.IStrategyRepository;
import cn.hush.types.common.Constants;
import cn.hush.types.enums.ResponseCode;
import cn.hush.types.exception.AppException;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Hush
 * @description
 * @create 2025-01-03 下午11:11
 */

public abstract class AbstractStrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {

    @Resource
    protected IStrategyRepository repository;


    @Override
    public boolean assembleLotteryStrategyByActivityId(Long activityId) {
        Long strategyId = repository.queryStrategyIdByActivityId(activityId);
        return assembleLotteryStrategy(strategyId);
    }

    // 装配抽奖策略
    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {

        // 1. 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

        // 2. 缓存奖品库存【用于decr扣减库存使用】
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Integer awardId = strategyAward.getAwardId();
            Integer awardCount = strategyAward.getAwardCount();
            cacheStrategyAwardCount(strategyId, awardId, awardCount);
        }

        // 3.1 默认装配配置【全量抽奖概率】
        armoryAlgorithm(String.valueOf(strategyId), strategyAwardEntities);

        //3.2 权重策略配置 - 适用于 rule-weight 权重规则配置
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        // 查询 strategyEntity 的 ruleModel 是否包含 rule_weight
        String ruleModel_weight = strategyEntity.getRuleWeight();
        if (null == ruleModel_weight) return true;
        // 根据 strategy_id 和 ruleModel 查询出 strategyRuleEntity，以获得其 ruleValue，从 ruleValue中获得积分对应的可抽奖的奖品id
        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleModel_weight);
        // 业务异常，策略规则中 rule_weight 权重规则已适用但未配置
        if (null == strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        // 获得 Map : KEY: 4000:102,103,104,105 (String ruleValueGroup)  VALUE: {102,103,104,105} (List values)
        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValueMap.keySet();
        for (String key : keys) {
            List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
            //获得 strategyAwardEntities 的一个深克隆，包含了全部的奖品
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            //删去深克隆中的多余奖品
            strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
            //装配到 redis 中
            armoryAlgorithm(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);
        }
        return true;
    }


    /**
     * 转换计算，只根据小数位来计算。如【0.01返回100】、【0.009返回1000】、【0.0018返回10000】
     */
    protected double convert(double min) {
        if (min == 0) return 1D;

        String minStr = String.valueOf(min);
        
        //小数点前
        String beginVal = minStr.substring(0, minStr.indexOf("."));
        int beginLength = 0;
        if (Double.parseDouble(beginVal) > 0) {
            beginLength = minStr.substring(0, minStr.indexOf(".")).length();
        }

        //小数点后
        String endVal = minStr.substring(minStr.indexOf(".") + 1);
        int endLength = 0;
        if (Double.parseDouble(endVal) > 0) {
            endLength = minStr.substring(minStr.indexOf(".") + 1).length();
        }
        return Math.pow(10, beginLength + endLength);
    }


    /**
     * 缓存奖品库存到Redis
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @param awardCount 奖品库存
     */
    private void cacheStrategyAwardCount(Long strategyId, Integer awardId, Integer awardCount) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        repository.cacheStrategyAwardCount(cacheKey, awardCount);
    }



    @Override
    public Integer getRandomAwardId(Long strategyId) {

        return dispatchAlgorithm(String.valueOf(strategyId));

    }


    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        return dispatchAlgorithm(key);
    }

    @Override
    public Integer getRandomAwardId(String key){
        return dispatchAlgorithm(key);
    }


    /**
     * 扣减库存
     * @param strategyId 策略id
     * @param awardId 奖品id
     * @param endDateTime 结束时间
     * @return 扣减结果
     */
    @Override
    public Boolean subtractAwardStock(Long strategyId, Integer awardId, Date endDateTime) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        return repository.subtractAwardStock(cacheKey);
    }

    /**
     * 求最小奖品概率
     * @param strategyAwardEntities 奖品实体列表
     * @return 最小奖品概率
     */
    protected BigDecimal minAwardRate (List<StrategyAwardEntity> strategyAwardEntities) {
        return strategyAwardEntities.stream().
                map(StrategyAwardEntity :: getAwardRate).
                min(BigDecimal :: compareTo).
                orElse(BigDecimal.ZERO);
    }

    /**
     * 装配算法
     * @param key 为策略ID、权重ID
     * @param strategyAwardEntities 对应的奖品概率
     */
    protected abstract void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities);

    protected abstract Integer dispatchAlgorithm(String key);

}
