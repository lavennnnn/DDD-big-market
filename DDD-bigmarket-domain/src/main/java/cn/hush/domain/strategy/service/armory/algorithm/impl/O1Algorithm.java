package cn.hush.domain.strategy.service.armory.algorithm.impl;

import cn.hush.domain.strategy.model.entity.StrategyAwardEntity;
import cn.hush.domain.strategy.service.armory.algorithm.AbstractAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Hush
 * @description
 * @create 2024-12-31 下午5:17
 */
@Component("o1Algorithm")
@Slf4j
public class O1Algorithm extends AbstractAlgorithm {


    @Override
    public void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities,
                                BigDecimal rateRange) {
        log.info("抽奖算法 O(1) 装配 key:{}", key);
        // 1. 生成策略奖品查找表【在 list 集合中，存放上对应的奖品占位，占位越多，概率越高】
        ArrayList<Integer> strategyAwardSearchTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            // 计算出每个概率值需要存放到查找表的数量，循环填充
            for (int i = 0; i < rateRange.multiply(awardRate).intValue(); i++){
                strategyAwardSearchTables.add(awardId);
            }
        }

        // 2. 对存储的奖品进行乱序操作
        Collections.shuffle(strategyAwardSearchTables);

        // 3. 生成 Map 集合，key值对应的就是后续的概率值。通过概率来获得对应的奖品ID
        LinkedHashMap<Integer, Integer> shuffledStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchTables.size(); i++){
            shuffledStrategyAwardSearchRateTable.put(i, strategyAwardSearchTables.get(i));
        }

        // 4. 存放到 Redis
        repository.storeStrategyAwardSearchRateTables(key, shuffledStrategyAwardSearchRateTable.size(), shuffledStrategyAwardSearchRateTable);

    }

    @Override
    public Integer dispatchAlgorithm(String key) {
        log.info("抽奖算法 O(1) 抽奖计算 key:{}", key);
        //分布式部署下，不一定当前应用做策略装配，也就是值不一定会保存到本应用，所以要从 Redis 中获取
        int rateRange = repository.getRateRange(key);
        return repository.getStrategyAwardAssemble(key, secureRandom.nextInt(rateRange));
    }
}
