package cn.hush.domain.strategy.service.armory;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;
import cn.hush.domain.strategy.model.entity.StrategyEntity;
import cn.hush.domain.strategy.model.entity.StrategyRuleEntity;
import cn.hush.domain.strategy.repository.IStrategyRepository;
import cn.hush.types.enums.ResponseCode;
import cn.hush.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author Hush
 * @description 策略装配（兵工厂），负责初始化策略计算
 * @create 2024-08-28 下午8:51
 */

@Slf4j
@Service
public class  StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch{

    @Resource
    private IStrategyRepository repository;

    // 装配抽奖策略
    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {

        //1. 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        //2. 权重策略配置 - 适用于 rule-weight 权重规则配置
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
            // 查询 strategyEntity 的 ruleModel 是否包含 rule_weight
        String ruleModel_weight = strategyEntity.getRuleWeight();
        if (null == ruleModel_weight) return true;
            // 根据 strategy_id 和 ruleModel 查询出 strategyRuleEntity，以获得其 ruleValue，从 ruleValue中获得积分对应的可抽奖的奖品id
        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleModel_weight);
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
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);
        }
        return true;
    }

    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        //1. 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream().
                map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //2. 获取概率值的总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //3. 用 1 % 0.0001 获取概率范围
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        //4. 生成策略奖品概率查找表「这里指需要在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高」
        ArrayList<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());

        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();

            //计算每个概率值需要存放到查找表的数量，循环填充
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        //5. 对存储的奖品进行乱序操作
        Collections.shuffle(strategyAwardSearchRateTables);

        //6. 生成出Map集合，key值，对应的就是后续的概率值。通过概率来获得对应的奖品ID
        Map<Integer, Integer> shuffledStrategyAwardSearchRateTables = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffledStrategyAwardSearchRateTables.put(i, strategyAwardSearchRateTables.get(i));
        }

        //7. 存储到redis
        repository.storeStrategyAwardSearchRateTables(key, shuffledStrategyAwardSearchRateTables.size(), shuffledStrategyAwardSearchRateTables);

    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(strategyId);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }


    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(key);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }
}
